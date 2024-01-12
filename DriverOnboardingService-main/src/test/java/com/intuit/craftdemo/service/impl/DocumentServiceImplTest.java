package com.intuit.craftdemo.service.impl;

import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.enums.DriverStatus;
import com.intuit.craftdemo.repository.DocumentRepository;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.service.IFileService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private IFileService fileService;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @SneakyThrows
    @Test
    void uploadDocument_DocumentExists_Success() {
        String licenseNumber = "123";
        String documentType = "License";
        MockMultipartFile document = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setStatus(DriverStatus.APPLICATION_SUBMITTED);
        Map<String, String> documents = new HashMap<>();
        documents.put(documentType, "existingFileName.txt");
        driver.setDocuments(documents);

        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        when(fileService.uploadFile(any())).thenReturn("newFileName.txt");

        assertTrue(documentService.uploadDocument(licenseNumber, document, documentType));

        verify(fileService, times(1)).uploadFile(document);
        verify(documentRepository, times(1)).updateDocumentName(1L, documentType, "newFileName.txt");
        verify(driverRepository, times(1)).updateDriverStatusById(1L, DriverStatus.ALL_DOCUMENTS_SUBMITTED.toString());
    }

    @SneakyThrows
    @Test
    void uploadDocument_DocumentDoesNotExist_Success() {
        String licenseNumber = "123";
        String documentType = "License";
        MockMultipartFile document = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setStatus(DriverStatus.APPLICATION_SUBMITTED);
        Map<String, String> documents = new HashMap<>();
        driver.setDocuments(documents);

        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        when(fileService.uploadFile(any())).thenReturn("newFileName.txt");

        assertFalse(documentService.uploadDocument(licenseNumber, document, documentType));

        verify(fileService, times(0)).uploadFile(document);
        verify(documentRepository, times(0)).updateDocumentName(1L, documentType, "newFileName.txt");
        verify(driverRepository, times(0)).updateDriverStatusById(any(), any());
    }

    @Test
    void downloadDocument_DocumentExists_Success() throws IOException {
        String licenseNumber = "123";
        String documentType = "License";
        Driver driver = new Driver();
        driver.setId(1L);
        Map<String, String> documents = new HashMap<>();
        documents.put(documentType, "existingFileName.txt");
        driver.setDocuments(documents);

        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        when(documentRepository.findDocumentFileNameByDriverIdAndDocumentType(1L, documentType)).thenReturn(java.util.Optional.of("existingFileName.txt"));
        when(fileService.downloadFile("existingFileName.txt")).thenReturn("Hello, World!".getBytes());

        byte[] result = documentService.downloadDocument(licenseNumber, documentType);

        assertNotNull(result);
        assertArrayEquals("Hello, World!".getBytes(), result);
    }

    @Test
    void downloadDocument_DocumentDoesNotExist_Success() {
        String licenseNumber = "123";
        String documentType = "License";
        Driver driver = new Driver();
        driver.setId(1L);
        Map<String, String> documents = new HashMap<>();
        driver.setDocuments(documents);

        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        when(documentRepository.findDocumentFileNameByDriverIdAndDocumentType(1L, documentType)).thenReturn(java.util.Optional.empty());

        byte[] result = documentService.downloadDocument(licenseNumber, documentType);

        assertNull(result);
    }

    @Test
    void getAllDocuments_Success() {
        String licenseNumber = "123";
        Driver driver = new Driver();
        driver.setId(1L);
        Map<String, String> documents = new HashMap<>();
        documents.put("License", "licenseFileName.txt");
        driver.setDocuments(documents);

        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);

        Map<String, String> result = documentService.getAllDocuments(licenseNumber);

        assertNotNull(result);
        assertEquals(documents, result);
    }

    @Test
    void getNonSubmittedDocumentsList_Success() {
        String licenseNumber = "123";
        Driver driver = new Driver();
        driver.setId(1L);
        Map<String, String> documents = new HashMap<>();
        documents.put("License", "licenseFileName.txt");
        documents.put("Insurance", "");
        driver.setDocuments(documents);

        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);

        assertEquals(List.of("Insurance"), documentService.getNonSubmittedDocumentsList(licenseNumber));
    }

    @Test
    void getVerificationFailedDocumentList_Success() {
        String licenseNumber = "123";
        Driver driver = new Driver();
        driver.setId(1L);

        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        when(documentRepository.findDocumentTypesWithVerificationFailedStatusByDriverId(1L)).thenReturn(List.of("License"));

        assertEquals(List.of("License"), documentService.getVerificationFailedDocumentList(licenseNumber));
    }
}

