package com.learning.kafka.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.kafka.infrastructure.service.CustomerVisitService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class CustomerController {

    private final CustomerVisitService customerVisitService;

    @Autowired
    public CustomerController(CustomerVisitService customerVisitService) {
        this.customerVisitService = customerVisitService;
    }

    @PostMapping(path = "/visit")
    public void createCustomerVisit(@RequestBody String visit) throws JsonProcessingException {
        log.info("create customer visit request received");
        customerVisitService.CreateDefaultVisit();
    }

}
