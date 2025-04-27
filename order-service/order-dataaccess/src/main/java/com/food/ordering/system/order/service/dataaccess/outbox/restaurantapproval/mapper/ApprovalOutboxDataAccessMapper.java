package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper;

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class ApprovalOutboxDataAccessMapper {

    public ApprovalOutboxEntity orderCreatedOutboxMessageToOutboxEntity(OrderApprovalOutboxMessage message) {
        return ApprovalOutboxEntity.builder()
                .id(message.getId())
                .sagaId(message.getSagaId())
                .createdAt(message.getCreatedAt())
                .type(message.getType())
                .payload(message.getPayload())
                .orderStatus(message.getOrderStatus())
                .sagaStatus(message.getSagaStatus())
                .outboxStatus(message.getOutboxStatus())
                .version(message.getVersion())
                .build();
    }

    public OrderApprovalOutboxMessage approvalOutboxEntityToOrderApprovalOutboxMessage(ApprovalOutboxEntity entity) {
        return OrderApprovalOutboxMessage.builder()
                .id(entity.getId())
                .sagaId(entity.getSagaId())
                .createdAt(entity.getCreatedAt())
                .type(entity.getType())
                .payload(entity.getPayload())
                .orderStatus(entity.getOrderStatus())
                .sagaStatus(entity.getSagaStatus())
                .outboxStatus(entity.getOutboxStatus())
                .version(entity.getVersion())
                .build();
    }
}
