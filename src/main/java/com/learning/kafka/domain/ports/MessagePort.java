package com.learning.kafka.domain.ports;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessagePort<T> {

  /**
   * Sends a message of type {@code T}.
   *
   * @param t the message to be sent
   * @throws JsonProcessingException if there is an error processing the message to JSON format
   */
  void sendMessage(T t) throws JsonProcessingException;
}