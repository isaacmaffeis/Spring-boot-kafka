## How to Start the application
Open Docker Desktop, go to the root directory of the project in the terminal and in order to run the containers of Kafka and Zookeeper digit:
```shell
docker compose up
```
run the project using any IDE or use maven with the following instruction:
```shell
mvn clean install
mvn spring-boot:run
```
use [Postman](https://web.postman.co//) to test the API, some examples:
```http request
POST /visit HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Content-Length: 49

{
    "customerId":null,
    "dateTime":null
}
``` 
```http request
POST /visit HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Content-Length: 108

{
    "customerId":"webID001-bf3c-4b21-8ea4-8bcc99a634d4",
    "dateTime":"2024-04-06T17:36:55.1313334"
}
```

## Kafka Basics
🔗 Kafka Docker Compose Quickstart: https://developer.confluent.io/quickstart/kafka-local/
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

## Kafka Web UI
[Kafdrop](https://github.com/obsidiandynamics/kafdrop) is a web UI for viewing Kafka topics and browsing consumer groups. The tool displays information such as 
brokers, topics, partitions, consumers, and lets you view messages.


Open a browser and navigate to http://localhost:9000.

![KafdropImage](..%2F..%2F..%2FPictures%2FScreenshots%2FScreenshot%202024-04-08%20103550.png)
## Dependencies

### Jackson
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

### Model Mapper
In order to use the model mapper module for implementing the mappers, use the
following dependency:
```xml
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.0.0</version>
</dependency>
```