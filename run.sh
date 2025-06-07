#!/bin/bash

# # Builda o projeto (opcional, pode comentar se não quiser buildar sempre)
# ./mvnw clean package -DskipTests

# # Executa o JAR gerado
# java -jar target/UserServiceAPI-0.0.1-SNAPSHOT.jar


# SEED=always ./mvnw spring-boot:run
SEED=never ./mvnw spring-boot:run

