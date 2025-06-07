package com.consumerservice.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaListenerConfig {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        // 10 segundos de espera, 100 tentativas
        return new DefaultErrorHandler(new FixedBackOff(10_000L, 100L));
    }
} 