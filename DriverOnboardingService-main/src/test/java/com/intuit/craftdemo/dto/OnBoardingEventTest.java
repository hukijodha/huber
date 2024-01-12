package com.intuit.craftdemo.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OnBoardingEventTest {

    @Test
    public void testOnBoardingEventGetterSetter() {
        OnBoardingEvent onBoardingEvent = new OnBoardingEvent(
                "John",
                "Doe",
                Arrays.asList("License", "Insurance"),
                "1234567890",
                "123 Main St",
                "Apt 456"
        );
        String firstName = onBoardingEvent.getFirstName();
        String lastName = onBoardingEvent.getLastName();
        String phoneNumber = onBoardingEvent.getPhoneNumber();
        List<String> documentsShared = onBoardingEvent.getDocumentsShared();
        String address1 = onBoardingEvent.getAddress1();
        String address2 = onBoardingEvent.getAddress2();
        assertNotNull(onBoardingEvent);
        assertEquals("John", firstName);
        assertEquals("Doe", lastName);
        assertEquals("1234567890", phoneNumber);
        assertEquals(Arrays.asList("License", "Insurance"), documentsShared);
        assertEquals("123 Main St", address1);
        assertEquals("Apt 456", address2);
    }
}

