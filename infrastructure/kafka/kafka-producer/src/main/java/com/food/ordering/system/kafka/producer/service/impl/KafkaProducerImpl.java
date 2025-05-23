package com.food.ordering.system.kafka.producer.service.impl;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;
import java.io.Serializable;

@Slf4j
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, K key, V value, ListenableFutureCallback<SendResult<K, V>> callback) {
        log.info("Sending record to topic {} with key {} and value {}", topicName, key, value);
        try {
            ListenableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, value);
            kafkaResultFuture.addCallback(callback);
        } catch (KafkaException e){
            log.error("Error on kafka producer with key {} and value {}", key, value, e);
            throw new KafkaProducerException("Error on kafka producer with key: " + key + " and value: " + value);
        }
    }

    @PreDestroy
    public void close(){
        if(kafkaTemplate!=null){
            log.info("Closing kafka producer");
            kafkaTemplate.destroy();
        }
    }

}
