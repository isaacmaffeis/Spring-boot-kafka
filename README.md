🔗 Kafka Docker Compose Quickstart: https://developer.confluent.io/quickstart/kafka-local/

## Kafka Basics

### Create and start containers
```shell
docker compose up
```

### Create Topic
```shell
docker exec broker kafka-topics --bootstrap-server broker:9092 --create --topic "customer.visit"
```

### Command Line Consumer
```shell
docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic "customer.visit" --from-beginning
```

### Command Line Producer
```shell
docker exec --interactive --tty broker kafka-console-producer --bootstrap-server broker:9092 --topic "customer.visit"
```

## Jackson Dependencies
In order to use Jackson and the module for Java8 DateTimes, use the
following dependencies:
```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.datatype</groupId>
  <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
```