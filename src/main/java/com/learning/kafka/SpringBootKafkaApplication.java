package com.learning.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.config.KafkaConfigProps;
import com.learning.kafka.domain.CustomerVisitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
public class SpringBootKafkaApplication {

    @Autowired
    private ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKafkaApplication.class, args);
    }


    /**
     *
     * @param kafkaTemplate : is the abstraction that Spring uses in order to interact with Kafka
     * with the type of the key that Kafka uses and the payload
     * @param kafkaConfigProps : configuration class of kafka
     * @return
     */
    @Bean
    public ApplicationRunner runner(final KafkaTemplate<String, String> kafkaTemplate, final KafkaConfigProps kafkaConfigProps) throws JsonProcessingException {
        final CustomerVisitEvent event = CustomerVisitEvent.builder()
                .customerId(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now())
                .build();

        final String payload = objectMapper.writeValueAsString(event);

        return args -> {
            kafkaTemplate.send(kafkaConfigProps.getTopic(), payload);
        };

    }

    @KafkaListener(topics = "customer.visit")
    public String listens(final String in) {
        System.out.println(in);
        return in;
    }

}
