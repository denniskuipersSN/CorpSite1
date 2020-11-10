package com.globex.web.kafkaclient;

import com.globex.web.kafkaclient.config.MyKafkaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties(value = {MyKafkaProperties.class})
public class KafkaConsumerApplication {

    public static void main(String[] args) {
        log.info("Start");
        SpringApplication.run(KafkaConsumerApplication.class, args);

    }

}
