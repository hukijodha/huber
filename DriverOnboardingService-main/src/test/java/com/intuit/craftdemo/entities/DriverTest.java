package com.intuit.craftdemo.entities;

import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.enums.DriverStatus;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DriverTest {

    @Test
    public void testGetterAndSetter() {
        // Given
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setFirstName("John");
        driver.setLastName("Doe");
        driver.setPhoneNumber("123456789");
        driver.setAddressLine1("Address Line 1");
        driver.setLicenseNumber("ABC123");
        driver.setAddressLine2("Address Line 2");
        driver.setCountry("Country");
        driver.setCity("City");
        driver.setState("State");
        driver.setPinCode("12345");
        driver.setHighestQualification("PhD");
        driver.setEmail("john.doe@example.com");
        driver.setPassword("password");
        driver.setDocuments(Collections.singletonMap("DocumentType", "DocumentFile"));
        driver.setKnownLanguages(Arrays.asList("English", "Spanish"));
        driver.setHasDrivingExperience(true);
        driver.setLastEmployerDetails("Previous Employer");
        driver.setAcceptTerms(true);
        driver.setStatus(DriverStatus.READY_TO_DRIVE);

        // Then
        assertEquals(1L, driver.getId());
        assertEquals("John", driver.getFirstName());
        assertEquals("Doe", driver.getLastName());
        assertEquals("123456789", driver.getPhoneNumber());
        assertEquals("Address Line 1", driver.getAddressLine1());
        assertEquals("ABC123", driver.getLicenseNumber());
        assertEquals("Address Line 2", driver.getAddressLine2());
        assertEquals("Country", driver.getCountry());
        assertEquals("City", driver.getCity());
        assertEquals("State", driver.getState());
        assertEquals("12345", driver.getPinCode());
        assertEquals("PhD", driver.getHighestQualification());
        assertEquals("john.doe@example.com", driver.getEmail());
        assertEquals("password", driver.getPassword());
        assertEquals(Collections.singletonMap("DocumentType", "DocumentFile"), driver.getDocuments());
        assertEquals(Arrays.asList("English", "Spanish"), driver.getKnownLanguages());
        assertTrue(driver.isHasDrivingExperience());
        assertEquals("Previous Employer", driver.getLastEmployerDetails());
        assertTrue(driver.isAcceptTerms());
        assertEquals(DriverStatus.READY_TO_DRIVE, driver.getStatus());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Given
        Driver driver1 = new Driver(1L, "John", "Doe", "123456789", "Address1", "ABC123",
                "Address2", "Country", "City", "State", "12345", "PhD",
                "john.doe@example.com", "password", Collections.singletonMap("DocumentType", "DocumentFile"),
                Arrays.asList("English", "Spanish"), true, "Previous Employer", true, DriverStatus.READY_TO_DRIVE, null);

        Driver driver2 = new Driver(1L, "John", "Doe", "123456789", "Address1", "ABC123",
                "Address2", "Country", "City", "State", "12345", "PhD",
                "john.doe@example.com", "password", Collections.singletonMap("DocumentType", "DocumentFile"),
                Arrays.asList("English", "Spanish"), true, "Previous Employer", true, DriverStatus.READY_TO_DRIVE, null);

        Driver driver3 = new Driver(2L, "Jane", "Doe", "987654321", "Address1", "XYZ789",
                "Address2", "Country", "City", "State", "54321", "MS",
                "jane.doe@example.com", "password", Collections.singletonMap("DocumentType", "DocumentFile"),
                Arrays.asList("French", "German"), false, "Current Employer", false, DriverStatus.APPLICATION_SUBMITTED, null);

        assertEquals(driver1, driver2);
        assertNotEquals(driver1, driver3);
        assertNotEquals(driver2, driver3);
        assertEquals(driver1.hashCode(), driver2.hashCode());
        assertNotEquals(driver1.hashCode(), driver3.hashCode());
        assertNotEquals(driver2.hashCode(), driver3.hashCode());
    }

    @Test
    public void testToString() {
        // Given
        Driver driver = new Driver(1L, "John", "Doe", "123456789", "Address1", "ABC123",
                "Address2", "Country", "City", "State", "12345", "PhD",
                "john.doe@example.com", "password", Collections.singletonMap("DocumentType", "DocumentFile"),
                Arrays.asList("English", "Spanish"), true, "Previous Employer", true, DriverStatus.READY_TO_DRIVE, null);

        // Then
        assertEquals("Driver(id=1, firstName=John, lastName=Doe, phoneNumber=123456789, " +
                "addressLine1=Address1, licenseNumber=ABC123, addressLine2=Address2, country=Country, " +
                "city=City, state=State, pinCode=12345, highestQualification=PhD, " +
                "email=john.doe@example.com, password=password, documents={DocumentType=DocumentFile}, " +
                "knownLanguages=[English, Spanish], hasDrivingExperience=true, " +
                "lastEmployerDetails=Previous Employer, acceptTerms=true, status=READY_TO_DRIVE, tokens=null)", driver.toString());
    }

}
