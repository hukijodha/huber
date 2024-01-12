package com.intuit.craftdemo.service.impl;

import com.intuit.craftdemo.dto.driver.PublishingEvent;
import com.intuit.craftdemo.entities.Document;
import com.intuit.craftdemo.entities.Driver;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class EventProducerServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.producer.topic}")
    private String kafkaTopic;

    @InjectMocks
    private EventProducerService eventProducerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void producePublishingEvent_Success() {
        Driver driverDto = new Driver(/* provide necessary details */);
        Document documentDto = new Document(/* provide necessary details */);
        List<Document> list = new ArrayList<>();
        list.add(documentDto);
        eventProducerService.producePublishingEvent(driverDto, list);
        verify(kafkaTemplate, times(1)).send(eq(kafkaTopic), any(PublishingEvent.class));
    }

}
