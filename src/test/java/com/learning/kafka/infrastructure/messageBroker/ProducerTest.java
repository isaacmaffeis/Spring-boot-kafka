package com.learning.kafka.infrastructure.messageBroker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.kafka.domain.dto.CustomerVisitEventDto;
import com.learning.kafka.domain.entities.CustomerVisitEventEntity;
import com.learning.kafka.domain.mapper.impl.CustomerEventVisitMapper;
import com.learning.kafka.util.TestAppender;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

@EnableKafka
@SpringBootTest()
@DirtiesContext
@EmbeddedKafka(partitions = 1,
    controlledShutdown = false,
    brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
    topics = {"${spring.kafka.topic}"})
class ProducerTest {

  @Autowired
  private Producer producer;
  @Autowired
  private EmbeddedKafkaBroker embeddedKafka;
  @Autowired
  private ObjectMapper objectMapper;
  @Value("${spring.kafka.topic}")
  private String topic;
  private Logger logger;
  private TestAppender testAppender;
  @Autowired
  private CustomerEventVisitMapper customerEventVisitMapper;

  @BeforeEach
  public void setup() {
    logger = (Logger) LoggerFactory.getLogger(Producer.class);
    testAppender = new TestAppender();
    testAppender.start();
    logger.addAppender(testAppender);
  }

  @Test
  public void testSendingMessage() throws JsonProcessingException {

    CustomerVisitEventEntity customerVisitEventEntity = CustomerVisitEventEntity.builder().build();
    customerVisitEventEntity.setCustomerId(UUID.randomUUID().toString());
    customerVisitEventEntity.setDateTime(LocalDateTime.now());

    producer.sendMessage(customerVisitEventEntity);
    String message = objectMapper.writeValueAsString(customerVisitEventEntity);

    // configurazione del consumer di Embedded Kafka
    Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testT", "false", embeddedKafka);
    DefaultKafkaConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
    Consumer<Integer, String> consumer = cf.createConsumer();
    embeddedKafka.consumeFromAnEmbeddedTopic(consumer, topic);
    ConsumerRecord<Integer, String> received = KafkaTestUtils.getSingleRecord(consumer, topic);

    // testo il corretto invio
    assertFalse(testAppender.events.isEmpty());
    assertEquals("Sent Message=[" + message + "] with offset=[0]", testAppender.events.get(0).getFormattedMessage());

    // testo la corretta ricezione
    assertThat(received.offset()).isEqualTo(0);
    assertThat(received.topic()).isEqualTo(topic);
    assertThat(received.partition()).isEqualTo(0);
    CustomerVisitEventDto customerVisitEventDto = objectMapper.readValue(received.value(),CustomerVisitEventDto.class);
    assertThat(customerEventVisitMapper.mapFrom(customerVisitEventDto)).isEqualTo(customerVisitEventEntity);

    logger.detachAppender(testAppender);
  }

}