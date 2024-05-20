package com.learning.kafka.infrastructure.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.domain.entities.CustomerVisitEventEntity;
import com.learning.kafka.infrastructure.messageBroker.Producer;
import com.learning.kafka.infrastructure.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CustomerVisitService implements CustomerService {

    private final Producer producer;

    @Autowired
    public CustomerVisitService(final Producer producer) {
        this.producer = producer;
    }


    @Override
    public CustomerVisitEventEntity save(CustomerVisitEventEntity customerVisitEventEntity) {
        // TO-DO add the entity to the DB
        if(customerVisitEventEntity.getCustomerId() == null)
            customerVisitEventEntity.setCustomerId(UUID.randomUUID().toString());
        if(customerVisitEventEntity.getDateTime() == null)
            customerVisitEventEntity.setDateTime(LocalDateTime.now());
        return customerVisitEventEntity;
    }

    @Override
    public void send(CustomerVisitEventEntity customerVisitEventEntity) throws JsonProcessingException {
        producer.sendMessage(customerVisitEventEntity);
    }
}
