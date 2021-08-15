package com.app.task;

import org.apache.kafka.clients.producer.Producer;
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


    @Autowired
    DataEnricherService dataProcessing;

    private CountDownLatch latch = new CountDownLatch(1);
    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    @KafkaListener(topics = "output_topic", groupId = "task")
    public void consumeDemographyData(String message) throws IOException {
        logger.info(String.format("Consumed message -> %s", message));
        dataProcessing.addData(message);
        latch.countDown();
    }


    public CountDownLatch getLatch() {
        return latch;
    }


}
