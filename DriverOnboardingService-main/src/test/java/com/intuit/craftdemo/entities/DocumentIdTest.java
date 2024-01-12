package com.intuit.craftdemo.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DocumentIdTest {

    @Test
    public void testEqualsAndHashCode() {
        DocumentId documentId1 = new DocumentId(1L, "License");
        DocumentId documentId2 = new DocumentId(1L, "License");
        DocumentId documentId3 = new DocumentId(2L, "Insurance");
        assertEquals(documentId1, documentId2);
        assertNotEquals(documentId1, documentId3);
        assertNotEquals(documentId2, documentId3);
        assertEquals(documentId1.hashCode(), documentId2.hashCode());
        assertNotEquals(documentId1.hashCode(), documentId3.hashCode());
        assertNotEquals(documentId2.hashCode(), documentId3.hashCode());
    }

    @Test
    public void testToString() {
        DocumentId documentId = new DocumentId(1L, "License");
        assertEquals("DocumentId(driverId=1, documents=License)", documentId.toString());
    }
}
