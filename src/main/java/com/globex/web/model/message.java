package com.globex.web.model;

import com.globex.web.kafkaclient.config.ConsumerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class message {

    public String message = "Mo Message yet";
    @Autowired
    private ConsumerConfiguration consumer;

    public message(){};

    public String getMessage() {
        String[] args = {};
        consumer.consumerFactory();
        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

}
