package com.globex.web.kafkaclient.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableKafka
@RequiredArgsConstructor

public class ConsumerConfiguration {

        private final MyKafkaProperties kafkaProperties;


        @Bean
        public ConcurrentKafkaListenerContainerFactory containerFactory(){
            ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory
                    = new ConcurrentKafkaListenerContainerFactory();
            kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
            log.info("Container Properties : " + kafkaListenerContainerFactory.getContainerProperties().toString());
            log.info("Consumer Properties : " + kafkaListenerContainerFactory.getConsumerFactory().getConfigurationProperties());
            return kafkaListenerContainerFactory;
        }

        @Bean
        public ConsumerFactory consumerFactory() {
            Map<String, Object> config = new HashMap<>();
            config.put(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    kafkaProperties.getBootstrapAddress());
            config.put(
                    ConsumerConfig.GROUP_ID_CONFIG,
                    MyKafkaProperties.CONSUMER_GROUP_ID);
            config.put(
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer.class);
            config.put(
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer.class);
            config.put(
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                    "earliest");
            ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(config);
            log.info("Kafka Server : " + kafkaProperties.getBootstrapAddress());
            return consumerFactory;
        }

}