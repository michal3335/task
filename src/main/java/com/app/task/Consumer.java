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
public class Consumer {

    @Autowired
    private com.app.task.Producer producer;

    @Autowired
    DataProcessing dataProcessing;

    private CountDownLatch latch = new CountDownLatch(1);
    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    @KafkaListener(topics = "input_topic", groupId = "task")
    public void consume(String message) throws IOException {
        logger.info(String.format("Consumed message -> %s", message));
        JsonObject output = dataProcessing.addData(message);
        producer.sendMessage("output_topic", output.toString());
        latch.countDown();
    }

    @KafkaListener(topics = "output_topic", groupId = "task")
    public void consume2(String message) throws IOException {
        logger.info(String.format("Consumed message -> %s", message));
        latch.countDown();
    }


    public CountDownLatch getLatch() {
        return latch;
    }


}