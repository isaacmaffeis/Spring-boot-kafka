package com.learning.kafka.application.listener.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.application.listener.NotifyEvent;
import com.learning.kafka.domain.dto.CustomerVisitEventDto;
import com.learning.kafka.domain.entities.CustomerVisitEventEntity;
import com.learning.kafka.domain.mapper.impl.CustomerEventVisitMapper;
import java.util.concurrent.CountDownLatch;
import lombok.Getter;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Log
public class Listener implements NotifyEvent {

    private final ObjectMapper objectMapper;

    @Getter
    private CountDownLatch latch = new CountDownLatch(1);

    private final Logger logger = LoggerFactory.getLogger(Listener.class);

    private CustomerVisitEventEntity lastMessageReceived;

    private final CustomerEventVisitMapper customerEventVisitMapper;

    @Autowired
    public Listener(final ObjectMapper objectMapper,
            final CustomerEventVisitMapper customerEventVisitMapper) {
        this.objectMapper = objectMapper;
        this.customerEventVisitMapper = customerEventVisitMapper;
    }

    @Override
    public void receive(@Payload String message,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
        @Header(KafkaHeaders.OFFSET) Long offset) throws JsonProcessingException {
        logger.info("Received a message {}, from {} topic, " +
            "{} partition, and {} offset", message, topic, partition, offset);
        CustomerVisitEventDto customerVisitEventDto = objectMapper.readValue(message, CustomerVisitEventDto.class);
        lastMessageReceived = customerEventVisitMapper.mapFrom(customerVisitEventDto);
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

    @Override
    public CustomerVisitEventEntity getLastMessageReceived() {
        return lastMessageReceived;
    }
}
