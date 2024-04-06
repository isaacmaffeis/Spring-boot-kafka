package com.learning.kafka.infrastructure.messageBroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.config.KafkaConfigProps;
import com.learning.kafka.domain.CustomerVisitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class Producer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfigProps kafkaConfigProps;
    private final ObjectMapper objectMapper;

    /**
     *
     * @param kafkaTemplate is the abstraction that Spring uses in order to interact with Kafka
     * @param kafkaConfigProps the configuration class for kafka
     * @param objectMapper to converting Java objects to JSON
     */
    @Autowired
    public Producer (final KafkaTemplate<String, String> kafkaTemplate, final KafkaConfigProps kafkaConfigProps, final ObjectMapper objectMapper){
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaConfigProps = kafkaConfigProps;
        this.objectMapper = objectMapper;
    }

    public void SendDefaultMessage() throws JsonProcessingException {
        final CustomerVisitEvent event = CustomerVisitEvent.builder()
                .customerId(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now())
                .build();

        final String payload = objectMapper.writeValueAsString(event);

        kafkaTemplate.send(kafkaConfigProps.getTopic(), payload);
    }

}
