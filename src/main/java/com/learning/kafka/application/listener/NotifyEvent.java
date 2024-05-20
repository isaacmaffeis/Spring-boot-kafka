package com.learning.kafka.application.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.kafka.domain.entities.CustomerVisitEventEntity;
import org.springframework.kafka.annotation.KafkaListener;

public interface NotifyEvent {

  /**
   * Receives a message from a Kafka topic.
   * <p>
   * This method is annotated with {@link KafkaListener} to automatically
   * subscribe to the Kafka topic specified by the {@code topics} property and
   * the consumer group specified by the {@code id} property in the Spring
   * configuration.
   * </p>
   * @param message   the received message as a string
   * @param topic     the name of the Kafka topic from which the message was received
   * @param partition the partition number of the Kafka topic from which the message was received
   * @param offset    the offset of the message in the Kafka topic partition
   * @throws JsonProcessingException if there is an error processing the message from JSON format
   */
  @KafkaListener(id = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic}")
  void receive(String message, String topic, Integer partition, Long offset) throws JsonProcessingException;


  /**
   * Gets the last message received from the Kafka topic.
   *
   * @return the last received message encapsulated in a {@link CustomerVisitEventEntity} object
   */
  CustomerVisitEventEntity getLastMessageReceived();

}