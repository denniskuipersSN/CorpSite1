package com.globex.web.kafkaclient.consumer;

import com.globex.web.kafkaclient.config.MyKafkaProperties;
import com.globex.web.model.message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {

    @Autowired
    private message Message;

    @KafkaListener(id="test" , topics = "MetricTopic", groupId = MyKafkaProperties.CONSUMER_GROUP_ID)
    public void consumeMessages(String messageTopic){
        Message.setMessage(messageTopic);
        log.info("CONSUMER: We received a message!!! {}", messageTopic);
    }
}

