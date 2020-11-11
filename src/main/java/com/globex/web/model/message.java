package com.globex.web.model;

import com.globex.web.kafkaclient.config.ConsumerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class message {

    @Autowired
    private ConsumerConfiguration consumer;
    private Date prev_date = new Date();
    private String prev_message = prev_date.toString();
    public String message = "no Message yet";

    public message(){
         prev_message = prev_date.toString();
         message = "no Message yet";
    };

    public String getMessage() {
        String[] args = {};
        if (message.equals("no Message yet"))
           consumer.consumerFactory();
        return message;
    }

    public void setMessage(String message) {
        Date date = new Date();

        if (date.toString().equals(this.prev_date.toString())){
           this.message =  this.prev_message + message.replaceAll("},","}\n");
        }
        else{
             this.prev_message = "";
             this.message = "";
;            this.message = date.toString() +  message.replaceAll("},","}\n");
        }
        this.prev_date = date;
        this.prev_message = this.message;
    }

}
