package com.learning.kafka.infrastructure.messageBroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.domain.dto.CustomerVisitEventDto;
import com.learning.kafka.domain.entities.CustomerVisitEventEntity;
import com.learning.kafka.domain.mapper.impl.CustomerEventVisitMapper;
import com.learning.kafka.domain.ports.MessagePort;
import java.util.concurrent.CompletableFuture;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@Log
public class Producer implements MessagePort<CustomerVisitEventEntity> {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final CustomerEventVisitMapper customerEventVisitMapper;
    @Value("${spring.kafka.topic}")
    private String topic;

    /**
     *
     * @param kafkaTemplate is the abstraction that Spring uses in order to interact with Kafka
     * @param objectMapper to converting Java objects to JSON
     * @param customerEventVisitMapper to map entities into DTO
     */
    @Autowired
    public Producer (final KafkaTemplate<String, String> kafkaTemplate,
            final ObjectMapper objectMapper,
            final CustomerEventVisitMapper customerEventVisitMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.customerEventVisitMapper = customerEventVisitMapper;
    }

    @Override
    public void sendMessage(CustomerVisitEventEntity customerVisitEventEntity) throws JsonProcessingException {

        CustomerVisitEventDto customerVisitEventDto = customerEventVisitMapper.mapTo(customerVisitEventEntity);

        final String payload = objectMapper.writeValueAsString(customerVisitEventDto);

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, payload);
        future.whenComplete((result,ex)->{
            if(ex == null){
                log.info("Sent Message=[" + payload + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            else{
                log.info("Unable to send message=[" + payload + "] due to : " + ex.getMessage());
            }
        });
    }

}
