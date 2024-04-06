package com.learning.kafka.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "learning.kafka")
@Data // generate getter, setters, hashcode and equals
@AllArgsConstructor // constructor with all of our arguments
@NoArgsConstructor // without any arguments
@Builder
public class KafkaConfigProps {

    private String topic;

}
