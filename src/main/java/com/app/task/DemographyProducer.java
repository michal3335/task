package com.app.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DemographyProducer {

    private static final Logger logger = LoggerFactory.getLogger(DemographyProducer.class);


    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public DemographyProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        logger.info(String.format("Producing message -> %s", message));
        this.kafkaTemplate.send(topic, message);
    }
}