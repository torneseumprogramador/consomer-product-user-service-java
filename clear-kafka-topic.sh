#!/bin/bash

# Script para limpar todas as mensagens do tópico product-user-topic
# Deleta e recria o tópico no Kafka rodando no container Docker 'kafka'

TOPIC="product-user-topic"
KAFKA_CONTAINER="kafka"
KAFKA_PORT="9092"

# Deletar o tópico
echo "Deletando o tópico $TOPIC..."
docker exec -it $KAFKA_CONTAINER kafka-topics.sh --bootstrap-server localhost:$KAFKA_PORT --delete --topic $TOPIC

# Recriar o tópico
sleep 2
echo "Recriando o tópico $TOPIC..."
docker exec -it $KAFKA_CONTAINER kafka-topics.sh --bootstrap-server localhost:$KAFKA_PORT --create --topic $TOPIC --partitions 1 --replication-factor 1

echo "Tópico $TOPIC limpo com sucesso!" 