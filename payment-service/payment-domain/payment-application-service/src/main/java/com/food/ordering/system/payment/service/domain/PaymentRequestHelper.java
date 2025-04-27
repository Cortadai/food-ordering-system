package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.exception.PaymentNotFoundException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.outbox.scheduler.OrderOutboxHelper;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final OrderOutboxHelper orderOutboxHelper;
    private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;

    public PaymentRequestHelper(CreditEntryRepository creditEntryRepository,
                                PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository,
                                CreditHistoryRepository creditHistoryRepository,
                                OrderOutboxHelper orderOutboxHelper,
                                PaymentResponseMessagePublisher paymentResponseMessagePublisher) {
        this.creditEntryRepository = creditEntryRepository;
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.orderOutboxHelper = orderOutboxHelper;
        this.paymentResponseMessagePublisher = paymentResponseMessagePublisher;
    }

    @Transactional
    public void persistPayment(PaymentRequest paymentRequest) {
        if(publishIfOutboxMessageProcessesForPayment(paymentRequest, PaymentStatus.COMPLETED)){
            log.info("An outbox message with saga id: {} is already saved to database!", paymentRequest.getSagaId());
            return;
        }
        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestToPayment(paymentRequest);
        PaymentEvent paymentEvent = handlePayment(payment, PaymentAction.INIT);
        OrderEventPayload orderEventPayload = paymentDataMapper.paymentEventToOrderEventpayload(paymentEvent);
        orderOutboxHelper.saveOrderOutboxMessage(
                orderEventPayload,
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId())
        );
    }

    @Transactional
    public void persistCancelPayment(PaymentRequest paymentRequest) {
        if(publishIfOutboxMessageProcessesForPayment(paymentRequest, PaymentStatus.CANCELLED)){
            log.info("An outbox message with saga id: {} is already saved to database!", paymentRequest.getSagaId());
            return;
        }
        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()))
                .orElseThrow(() -> {
                    log.error("Payment not found for order id: {}", paymentRequest.getOrderId());
                    return new PaymentNotFoundException(
                            "Payment with order id: " + paymentRequest.getOrderId() + " not found!");
                });
        PaymentEvent paymentEvent = handlePayment(payment, PaymentAction.CANCEL);
        OrderEventPayload orderEventPayload = paymentDataMapper.paymentEventToOrderEventpayload(paymentEvent);
        orderOutboxHelper.saveOrderOutboxMessage(
                orderEventPayload,
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId())
        );
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
                    .validateAndInitPayment(payment, creditEntry, creditHistories, failureMessages);
            case CANCEL -> paymentDomainService
                    .validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages);
        };

        persistDbObject(payment, failureMessages, creditEntry, creditHistories);
        return paymentEvent;
    }

    private enum PaymentAction {
        INIT,
        CANCEL
    }

    private boolean publishIfOutboxMessageProcessesForPayment(PaymentRequest paymentRequest,
                                                              PaymentStatus paymentStatus) {
        Optional<OrderOutboxMessage> orderOutboxMessage = orderOutboxHelper
                .getCompleteOrderOutboxMessageBySagaIdAndPaymentStatus(
                        UUID.fromString(paymentRequest.getSagaId()), paymentStatus
                );
        if (orderOutboxMessage.isPresent()) {
            paymentResponseMessagePublisher.publish(
                    orderOutboxMessage.get(),
                    (orderOutboxMessage1, outboxStatus) -> orderOutboxHelper
                            .updateOutboxMessage(orderOutboxMessage1, outboxStatus));
            return true;
        }
        return false;
    }

}
