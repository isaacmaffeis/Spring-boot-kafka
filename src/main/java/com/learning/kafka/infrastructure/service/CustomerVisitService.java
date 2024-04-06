package com.learning.kafka.infrastructure.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.infrastructure.messageBroker.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerVisitService {

    private final Producer producer;

    @Autowired
    public CustomerVisitService(final Producer producer) {
        this.producer = producer;
    }

    public void CreateDefaultVisit() throws JsonProcessingException {
        producer.SendDefaultMessage();
    }
}
