package com.food.ordering.system.saga;

import com.food.ordering.system.domain.events.DomainEvent;

public interface SagaStep<T> {

    void process(T data);

    void rollback(T data);

}
