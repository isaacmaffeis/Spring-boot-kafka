package com.learning.kafka.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.kafka.domain.dto.CustomerVisitEventDto;
import com.learning.kafka.domain.entities.CustomerVisitEventEntity;
import com.learning.kafka.domain.mapper.Mapper;
import com.learning.kafka.infrastructure.service.impl.CustomerVisitService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class CustomerController {

    private final CustomerVisitService customerVisitService;
    private Mapper<CustomerVisitEventEntity, CustomerVisitEventDto> mapper;

    @Autowired
    public CustomerController(CustomerVisitService customerVisitService, Mapper<CustomerVisitEventEntity, CustomerVisitEventDto> mapper) {
        this.customerVisitService = customerVisitService;
        this.mapper = mapper;
    }

    @PostMapping(path = "/visit")
    public ResponseEntity<CustomerVisitEventDto> createAuthor(@RequestBody CustomerVisitEventDto customerVisitEvent) throws JsonProcessingException {
        CustomerVisitEventEntity customerVisitEventEntity = mapper.mapFrom(customerVisitEvent);
        CustomerVisitEventEntity savedCustomerVisitEntity = customerVisitService.save(customerVisitEventEntity);
        customerVisitService.send(customerVisitEventEntity);
        return new ResponseEntity<>(mapper.mapTo(savedCustomerVisitEntity), HttpStatus.CREATED);
    }

}
