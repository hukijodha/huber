package com.intuit.craftdemo.controller;

import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.exception.DocumentUploadException;
import com.intuit.craftdemo.exception.DriverNotFoundException;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.service.IDocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DocumentControllerTest {

    @Mock
    private IDocumentService documentService;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private Model model;

    @InjectMocks
    private DocumentController documentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void uploadDocument_Success() {
        String documentType = "license";
        String licenseNumber = "123";
        MultipartFile documentFile = mock(MultipartFile.class);
        Driver driver = new Driver();
        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        when(documentService.uploadDocument(anyString(), any(MultipartFile.class), anyString())).thenReturn(true);
        ResponseEntity<String> response = documentController.uploadDocument(documentType, licenseNumber, documentFile, model);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Document uploaded successfully", response.getBody());
    }

    /*@Test
    void uploadDocument_LargerThanExpectedSize(
     //

    )*/

    @Test
    void uploadDocument_DriverNotFound() {
        String documentType = "license";
        String licenseNumber = "123";
        MultipartFile documentFile = mock(MultipartFile.class);
        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(null);
        assertThrows(DriverNotFoundException.class, () -> documentController.uploadDocument(documentType, licenseNumber, documentFile, model));
    }

    @Test
    void uploadDocument_DocumentEmpty() {
        String documentType = "license";
        String licenseNumber = "123";
        MultipartFile documentFile = mock(MultipartFile.class);
        Driver driver = new Driver();
        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        when(documentFile.isEmpty()).thenReturn(true);
        assertThrows(DocumentUploadException.class, () -> documentController.uploadDocument(documentType, licenseNumber, documentFile, model));
    }

    @Test
    void uploadDocument_FailedUpload() {
        String documentType = "license";
        String licenseNumber = "123";
        MultipartFile documentFile = mock(MultipartFile.class);
        Driver driver = new Driver();
        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        when(documentService.uploadDocument(anyString(), any(MultipartFile.class), anyString())).thenReturn(false);
        assertThrows(DocumentUploadException.class, () -> documentController.uploadDocument(documentType, licenseNumber, documentFile, model));
    }

    @Test
    void downloadDocument_Success() throws IOException {
        String licenseNumber = "123";
        String documentType = "license";
        Driver driver = new Driver();
        byte[] fileContent = "Sample content".getBytes();
        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        when(documentService.downloadDocument(licenseNumber, documentType)).thenReturn(fileContent);
        ResponseEntity<byte[]> response = documentController.downloadDocument(licenseNumber, documentType);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(fileContent, response.getBody());
    }

    @Test
    void downloadDocument_DriverNotFound() {
        String licenseNumber = "123";
        String documentType = "license";
        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(null);
        assertThrows(DriverNotFoundException.class, () -> documentController.downloadDocument(licenseNumber, documentType));
    }

    @Test
    void downloadDocument_NullFileContent() throws IOException {
        // Mock data
        String licenseNumber = "123";
        String documentType = "license";
        Driver driver = new Driver();
        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        when(documentService.downloadDocument(licenseNumber, documentType)).thenReturn(null);
        ResponseEntity<byte[]> response = documentController.downloadDocument(licenseNumber, documentType);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void downloadAllDocuments_Success() throws IOException {
        // Mock data
        String licenseNumber = "123";
        Map<String, String> documentMap = new HashMap<>();
        documentMap.put("license", "license.pdf");
        byte[] fileContent = "Sample content".getBytes();
        when(documentService.getAllDocuments(licenseNumber)).thenReturn(documentMap);
        when(documentService.downloadDocument(eq(licenseNumber), anyString())).thenReturn(fileContent);

        // Perform the test
        ResponseEntity<byte[]> response = documentController.downloadAllDocuments(licenseNumber);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void downloadAllDocuments_NoDocuments() throws IOException {
        // Mock data
        String licenseNumber = "123";
        when(documentService.getAllDocuments(licenseNumber)).thenReturn(new HashMap<>());

        // Perform the test
        ResponseEntity<byte[]> response = documentController.downloadAllDocuments(licenseNumber);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
