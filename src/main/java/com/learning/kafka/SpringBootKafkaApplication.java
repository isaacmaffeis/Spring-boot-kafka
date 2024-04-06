package com.learning.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.config.KafkaConfigProps;
import com.learning.kafka.domain.CustomerVisitEvent;
import com.learning.kafka.infrastructure.messageBroker.Producer;
import lombok.extern.java.Log;
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
@Log
public class SpringBootKafkaApplication {

    public static void main(String[] args) {SpringApplication.run(SpringBootKafkaApplication.class, args);}

    @Bean
    public ApplicationRunner runner(final Producer producer) {
        return args -> {
            producer.SendDefaultMessage();
        };
    }

}
