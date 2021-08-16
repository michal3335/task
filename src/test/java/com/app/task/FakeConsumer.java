package com.app.task;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Profile("integration")
@Service
public class FakeConsumer {




    private CountDownLatch latch = new CountDownLatch(1);
    private final Logger logger = LoggerFactory.getLogger(Producer.class);
    private  JsonObject message;



    @KafkaListener(topics = "output_topic", groupId = "task")
    public void consumeDemographyData(String message) throws IOException {
            logger.info(String.format("Consumed message -> %s", message));
            JsonObject parsed = getDemography(message);
            this.message = parsed;


        latch.countDown();
    }


    public CountDownLatch getLatch() {
        return latch;
    }


    public JsonObject getDemography(String data){
        JsonObject demography  = new JsonParser().parse(data).getAsJsonObject();


        return demography;
    }


    public JsonObject getMessage() {
        return message;
    }
}
