# Kafka Connect Source & Sink Example

## Requirements
- Java 11 or maybe 17 (tested with 11)
- Docker Compose

## Instructions
1. Build the jar:
```
$ ./gradlew jar
```
2. Launch the services:
``` 
$ docker-compose up
```
3. Open Redpanda Console and Play Around: 
> http://localhost:8080/connect-clusters/kafka-connect

## ⚠️ Heads Up!
The dummy source connector will rapidly fill a target topic with garbage data 😬