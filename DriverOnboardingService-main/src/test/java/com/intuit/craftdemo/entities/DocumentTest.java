package com.intuit.craftdemo.entities;

import com.intuit.craftdemo.enums.DocumentStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DocumentTest {

    @Test
    public void testDocumentEntity() {
        DocumentId documentId = new DocumentId(1L, "License");
        Driver driver = new Driver();
        driver.setId(1L);
        Document document = new Document(documentId, driver, "license_file.pdf", DocumentStatus.VERIFIED);
        assertNotNull(document);
        assertEquals(documentId, document.getDocumentId());
        assertEquals(driver, document.getDriver());
        assertEquals("license_file.pdf", document.getDocumentFile());
        assertEquals(DocumentStatus.VERIFIED, document.getDocumentStatus());
    }
}
