package com.learning.kafka.application.listener;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learning.kafka.application.listener.impl.Listener;
import com.learning.kafka.domain.dto.CustomerVisitEventDto;
import com.learning.kafka.domain.entities.CustomerVisitEventEntity;
import com.learning.kafka.domain.mapper.impl.CustomerEventVisitMapper;
import com.learning.kafka.util.TestAppender;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@EnableKafka
@SpringBootTest
@DirtiesContext
@Log
@EmbeddedKafka(partitions = 1,
    controlledShutdown = false,
    brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
    topics = {"${spring.kafka.topic}"})
public class ListenerTest {

  @Autowired
  private EmbeddedKafkaBroker embeddedKafka;
  @Autowired
  private Listener listener; // kafka consumer
  @Value("${spring.kafka.topic}")
  private String topic;
  private Logger logger;
  private TestAppender testAppender;
  @Autowired
  private CustomerEventVisitMapper customerEventVisitMapper;
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    listener.resetLatch();

    logger = (Logger) LoggerFactory.getLogger(Listener.class);
    testAppender = new TestAppender();
    testAppender.start();
    logger.addAppender(testAppender);
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
  }

  @Test
  public void testOutput1() throws Exception {

    CustomerVisitEventDto customerVisitEventDto = CustomerVisitEventDto.builder().build();
    customerVisitEventDto.setCustomerId(UUID.randomUUID().toString());
    customerVisitEventDto.setDateTime(LocalDateTime.now());

    String message = objectMapper.writeValueAsString(customerVisitEventDto);

    Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafka);
    DefaultKafkaProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(producerProps);
    KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf, true);
    template.setDefaultTopic(topic);
    CompletableFuture<SendResult<Integer, String>> future = template.send(topic,message);
    log.info("Sent Message=[" + customerVisitEventDto + "] with offset=[0]");

    boolean messageConsumed = listener.getLatch().await(10, TimeUnit.SECONDS);

    future.whenComplete((result,ex)->{
      assertThat(ex).isNull();
    });

    assertTrue(messageConsumed);
    assertFalse(testAppender.events.isEmpty());
    assertEquals("Received a message " + message + ", from " + topic + " topic, 0 partition, and 0 offset", testAppender.events.get(0).getFormattedMessage());

    CustomerVisitEventEntity customerVisitEventEntityReceived = listener.getLastMessageReceived();
    assertEquals(customerEventVisitMapper.mapFrom(customerVisitEventDto), customerVisitEventEntityReceived);
    logger.detachAppender(testAppender);
  }

}