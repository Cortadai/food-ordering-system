package com.food.ordering.system.payment.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderEventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentDataMapper {


    public Payment paymentRequestToPayment(PaymentRequest paymentRequest) {

        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();

    }

    public OrderEventPayload paymentEventToOrderEventpayload(PaymentEvent paymentevent){
        return OrderEventPayload.builder()
                .paymentId(paymentevent.getPayment().getId().getValue().toString())
                .customerId(paymentevent.getPayment().getCustomerId().getValue().toString())
                .orderId(paymentevent.getPayment().getOrderId().getValue().toString())
                .price(paymentevent.getPayment().getPrice().getAmount())
                .createdAt(paymentevent.getCreatedAt())
                .paymentStatus(paymentevent.getPayment().getPaymentStatus().name())
                .failureMessages(paymentevent.getFailureMessages())
                .build();
    }

}
