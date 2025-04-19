package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentCompletedMessagePublisher paymentCompletedEventDomainEventPublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher;
    private final PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher;

    public PaymentRequestHelper(CreditEntryRepository creditEntryRepository,
                                PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository,
                                CreditHistoryRepository creditHistoryRepository,
                                PaymentCompletedMessagePublisher paymentCompletedEventDomainEventPublisher,
                                PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher,
                                PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher) {
        this.creditEntryRepository = creditEntryRepository;
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.paymentCompletedEventDomainEventPublisher = paymentCompletedEventDomainEventPublisher;
        this.paymentCancelledEventDomainEventPublisher = paymentCancelledEventDomainEventPublisher;
        this.paymentFailedEventDomainEventPublisher = paymentFailedEventDomainEventPublisher;
    }

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestToPayment(paymentRequest);
        return handlePayment(payment, PaymentAction.INIT);
        /*
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService
                .validateAndInitPayment(payment, creditEntry, creditHistories, failureMessages);
        persistDbObject(payment, failureMessages, creditEntry, creditHistories);
        return paymentEvent;
         */
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()))
                .orElseThrow(() -> {
                    log.error("Payment not found for order id: {}", paymentRequest.getOrderId());
                    return new PaymentApplicationServiceException("Payment not found for order id: " + paymentRequest.getOrderId());
                });
        return handlePayment(payment, PaymentAction.CANCEL);
        /*
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService
                .validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages);
        persistDbObject(payment, failureMessages, creditEntry, creditHistories);
        return paymentEvent;
         */
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
        if (creditEntry.isEmpty()) {
            log.error("Credit entry not found for customer id: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Credit entry not found for customer id: " + customerId.getValue());
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistories(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistory = creditHistoryRepository.findByCustomerId(customerId);
        if (creditHistory.isEmpty()) {
            log.error("Credit history not found for customer id: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Credit history not found for customer id: " + customerId.getValue());
        }
        return creditHistory.get();
    }

    private void persistDbObject(Payment payment, List<String> failureMessages,
                                CreditEntry creditEntry, List<CreditHistory> creditHistories) {
        paymentRepository.save(payment);
        if(failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }

    private PaymentEvent handlePayment(Payment payment, PaymentAction action) {
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistories(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = switch (action) {
            case INIT -> paymentDomainService
                    .validateAndInitPayment(payment, creditEntry, creditHistories, failureMessages,
                            paymentCompletedEventDomainEventPublisher, paymentFailedEventDomainEventPublisher);
            case CANCEL -> paymentDomainService
                    .validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages,
                            paymentCancelledEventDomainEventPublisher, paymentFailedEventDomainEventPublisher);
        };

        persistDbObject(payment, failureMessages, creditEntry, creditHistories);
        return paymentEvent;
    }

    private enum PaymentAction {
        INIT,
        CANCEL
    }

}
