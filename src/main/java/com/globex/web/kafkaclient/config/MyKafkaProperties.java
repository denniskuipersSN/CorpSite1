package com.globex.web.kafkaclient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mykafka")
@Data
public class MyKafkaProperties {

    public static final String CONSUMER_GROUP_ID = "demo-group";

    private String bootstrapAddress;

    public String getBootstrapAddress() {
        return "ec2-3-15-108-48.us-east-2.compute.amazonaws.com:9092";
    }

    public void setBootstrapAddress(String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
    }

}
