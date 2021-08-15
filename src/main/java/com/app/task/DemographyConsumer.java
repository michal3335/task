package com.app.task;

import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


@Component
public class DemographyConsumer {


    private  DataEnricherService dataEnricherService;
    private CountDownLatch latch = new CountDownLatch(1);
    private final Logger logger = LoggerFactory.getLogger(Producer.class);
    
    @Autowired
    public DemographyConsumer(DataEnricherService dataEnricherService){
        this.dataEnricherService = dataEnricherService;
    }

    @KafkaListener(topics = "input_topic", groupId = "task")
    public void consumeDemographyData(String message) throws IOException {
        logger.info(String.format("Consumed message -> %s", message));
        dataEnricherService.addData(message);
        latch.countDown();
    }


    public CountDownLatch getLatch() {
        return latch;
    }


}