package com.learning.kafka;

import com.learning.kafka.infrastructure.messageBroker.Producer;
import lombok.extern.java.Log;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
