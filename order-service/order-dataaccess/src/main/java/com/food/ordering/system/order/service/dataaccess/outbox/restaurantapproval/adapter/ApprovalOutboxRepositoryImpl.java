package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.adapter;

import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.entity.ApprovalOutboxEntity;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.exception.ApprovalOutboxNotFoundException;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.mapper.ApprovalOutboxDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.repository.ApprovalOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ApprovalOutboxRepositoryImpl implements ApprovalOutboxRepository {

    private final ApprovalOutboxJpaRepository approvalOutboxJpaRepository;
    private final ApprovalOutboxDataAccessMapper approvalOutboxDataAccessMapper;

    public ApprovalOutboxRepositoryImpl(ApprovalOutboxDataAccessMapper approvalOutboxDataAccessMapper,
                                        ApprovalOutboxJpaRepository approvalOutboxJpaRepository) {
        this.approvalOutboxDataAccessMapper = approvalOutboxDataAccessMapper;
        this.approvalOutboxJpaRepository = approvalOutboxJpaRepository;
    }

    @Override
    public OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage message) {
        ApprovalOutboxEntity entity = approvalOutboxDataAccessMapper.orderCreatedOutboxMessageToOutboxEntity(message);
        ApprovalOutboxEntity savedEntity = approvalOutboxJpaRepository.save(entity);
        return approvalOutboxDataAccessMapper.approvalOutboxEntityToOrderApprovalOutboxMessage(savedEntity);
    }

    @Override
    public Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type,
                                                                                            OutboxStatus outboxStatus,
                                                                                            SagaStatus... sagaStatus) {
        return Optional.of(approvalOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(type,
                        outboxStatus,
                        Arrays.asList(sagaStatus))
                .orElseThrow(() -> new ApprovalOutboxNotFoundException("Approval outbox object " +
                        "could not be found for saga type " + type))
                .stream()
                .map(entity -> approvalOutboxDataAccessMapper.approvalOutboxEntityToOrderApprovalOutboxMessage(entity))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String type,
                                                                                UUID sagaId,
                                                                                SagaStatus... sagaStatus) {
        return approvalOutboxJpaRepository
                .findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.asList(sagaStatus))
                .map(entity -> approvalOutboxDataAccessMapper.approvalOutboxEntityToOrderApprovalOutboxMessage(entity));
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String type,
                                                         OutboxStatus outboxStatus,
                                                         SagaStatus... sagaStatus) {
        approvalOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type,
                outboxStatus, Arrays.asList(sagaStatus));
    }

}
