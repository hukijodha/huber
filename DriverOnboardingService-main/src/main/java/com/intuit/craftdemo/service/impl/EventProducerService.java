package com.intuit.craftdemo.service.impl;

import com.intuit.craftdemo.dto.driver.PublishingEvent;
import com.intuit.craftdemo.entities.Document;
import com.intuit.craftdemo.entities.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventProducerService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.producer.topic}")
    private String kafkaTopic;

    public void producePublishingEvent(Driver driverDto, List<Document> documentDto) {
        kafkaTemplate.send(kafkaTopic, new PublishingEvent(driverDto, documentDto));
    }
}
