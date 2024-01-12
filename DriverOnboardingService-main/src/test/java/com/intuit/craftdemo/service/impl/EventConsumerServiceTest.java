package com.intuit.craftdemo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.craftdemo.repository.DocumentRepository;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.service.IDocumentService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventConsumerServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private IDocumentService documentService;

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private EventConsumerService eventConsumerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void consumeDriverDocumentEvent_DriverStatusEvent_Success() throws Exception {
        String event = "{\"driverId\":\"1\",\"status\":\"APPROVED\"}";
        eventConsumerService.consumeDriverDocumentEvent(event);
        verify(driverRepository, times(1)).updateDriverStatusById(1L, "APPROVED");
        verify(documentRepository, never()).updateDocumentStatusByFileName(any(), any());
    }

    @Test
    void consumeDriverDocumentEvent_DocumentStatusEvent_Success() throws Exception {
        String event = "{\"documentName\":\"document.txt\",\"status\":\"VERIFIED\"}";
        eventConsumerService.consumeDriverDocumentEvent(event);
        verify(documentRepository, times(1)).updateDocumentStatusByFileName("VERIFIED", "document.txt");
        verify(driverRepository, never()).updateDriverStatusById(any(), any());
    }

    @SneakyThrows
    @Test
    void consumeDriverDocumentEvent_InvalidJson_ExceptionThrown() {
        String event = "invalid-json";
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readTree(event)).thenThrow(new RuntimeException("JsonProcessingException"));
        assertThrows(RuntimeException.class, () -> eventConsumerService.consumeDriverDocumentEvent(event));
        verifyZeroInteractions(driverRepository);
        verifyZeroInteractions(documentRepository);
    }
}

