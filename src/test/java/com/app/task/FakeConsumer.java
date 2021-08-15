package com.app.task;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.producer.Producer;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Profile("integration")
@Component
public class FakeConsumer {



    private  DataEnricherService dataEnricherService;
    private CountDownLatch latch = new CountDownLatch(1);
    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    public FakeConsumer(DataEnricherService dataEnricherService){
        this.dataEnricherService = dataEnricherService;
    }

    @KafkaListener(topics = "output_topic", groupId = "task")
    public void consumeDemographyData(String message) throws IOException {
        logger.info(String.format("Consumed message -> %s", message));
        latch.countDown();

        JsonObject expectedOutput = new JsonObject();
        expectedOutput.addProperty("city", "BERLIN");
        expectedOutput.addProperty("country","GERMANY");
        expectedOutput.addProperty("population","3645000");

        Assertions.assertEquals(expectedOutput,getDemography(message));
    }


    public CountDownLatch getLatch() {
        return latch;
    }


    public JsonObject getDemography(String data){
        JsonObject demography  = new JsonParser().parse(data).getAsJsonObject();


        return demography;
    }
}
