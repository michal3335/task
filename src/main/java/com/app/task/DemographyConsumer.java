package com.app.task;

import com.google.gson.JsonObject;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


@Service
public class DemographyConsumer {

    @Autowired
    private com.app.task.Producer producer;

    @Autowired
    DataEnricherService dataProcessing;

    private CountDownLatch latch = new CountDownLatch(1);
    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    @KafkaListener(topics = "input_topic", groupId = "task")
    public void consumeDemographyData(String message) throws IOException {
        logger.info(String.format("Consumed message -> %s", message));
        dataProcessing.addData(message);
        latch.countDown();
    }



    public CountDownLatch getLatch() {
        return latch;
    }


}