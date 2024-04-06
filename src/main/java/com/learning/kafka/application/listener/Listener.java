package com.learning.kafka.application.listener;

import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Log
public class Listener {

    @KafkaListener(topics = "customer.visit")
    public String listens(final String in) {
        log.info("Consumed: " + in);
        return in;
    }

}
