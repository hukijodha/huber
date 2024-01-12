package com.intuit.craftdemo.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.craftdemo.controller.DriverController;
import com.intuit.craftdemo.dto.HuberEvent;
import com.intuit.craftdemo.dto.driver.PublishingEvent;
import com.intuit.craftdemo.entities.Document;
import com.intuit.craftdemo.enums.DriverStatus;
import com.intuit.craftdemo.repository.DocumentRepository;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.service.IDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@KafkaListener(topics = "${kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}")
public class EventConsumerService {
    @Autowired
    DriverRepository driverRepository;

    private static final Logger log = LoggerFactory.getLogger(EventConsumerService.class);

    @Autowired
    IDocumentService documentService;


    @Autowired
    DocumentRepository documentRepository;

    @KafkaHandler
    @Transactional
    public void consumeDriverDocumentEvent(String event) {
        log.debug("Event Incoming {} ", event);
        System.out.println("INCOMING EVENT "+ event);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException");
        }
        boolean isDocumentVerifiedEvent = true;
        if(jsonNode.get("driverId") !=null && jsonNode.get("status")!=null)
        {
            isDocumentVerifiedEvent=false;
            String driverId = jsonNode.get("driverId").asText();
            String status = jsonNode.get("status").asText();
            Long driverIdLong = Long.parseLong(driverId);
            UpdateDriverStatus(driverIdLong,status);
        }
        if(isDocumentVerifiedEvent && jsonNode.get("documentName")!=null && jsonNode.get("status")!=null )
        {
            String filename = jsonNode.get("documentName").asText();
            String status = jsonNode.get("status").asText();
            documentRepository.updateDocumentStatusByFileName(status, filename);
        }
        log.debug("Event consumer successfully Event {}", event);
    }


    public void UpdateDriverStatus(Long driverId, String status)
   {
       System.out.println("UPDATING THE DRIVER with driverID "+driverId + " Status : "+ status);
           driverRepository.updateDriverStatusById(driverId, status);
       System.out.println("UPDATED THE DRIVER");
   }



}
