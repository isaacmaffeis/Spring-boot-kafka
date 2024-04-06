package com.learning.kafka.infrastructure.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.kafka.domain.entities.CustomerVisitEventEntity;

public interface CustomerService {

    CustomerVisitEventEntity save(CustomerVisitEventEntity customerVisitEventEntity);

    void send(CustomerVisitEventEntity customerVisitEventEntity) throws JsonProcessingException;

}
