package com.intuit.craftdemo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuberEventTest {

    @Test
    public void testHuberEventGetterSetter() {
        HuberEvent huberEvent = new HuberEvent("ABC123", "ACTIVE");
        String licenseNumber = huberEvent.getLicenseNumber();
        String status = huberEvent.getStatus();
        assertEquals("ABC123", licenseNumber);
        assertEquals("ACTIVE", status);
    }
}
