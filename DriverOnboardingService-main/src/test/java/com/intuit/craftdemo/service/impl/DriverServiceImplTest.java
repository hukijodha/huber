package com.intuit.craftdemo.service.impl;
import com.intuit.craftdemo.dto.driver.RequestDriver;
import com.intuit.craftdemo.dto.driver.ResponseDriver;
import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.enums.DriverStatus;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.service.impl.DriverServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DriverServiceImpl driverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void onBoardDriver_Success() {
        RequestDriver requestDriver = new RequestDriver();
        requestDriver.setLicenseNumber("123");
        requestDriver.setPassword("password");
        Driver savedDriver = new Driver();
        savedDriver.setLicenseNumber("123");
        savedDriver.setStatus(DriverStatus.APPLICATION_SUBMITTED);
        ResponseDriver responseDriver = new ResponseDriver();
        responseDriver.setStatus(true);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(driverRepository.save(any())).thenReturn(savedDriver);
        ResponseDriver result = driverService.onBoardDriver(requestDriver);
        assertTrue(result.isStatus());
    }

    @Test
    void onBoardDriver_DataIntegrityViolationException_Failure() {
        RequestDriver requestDriver = new RequestDriver();
        requestDriver.setLicenseNumber("123");
        requestDriver.setPassword("password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(driverRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
        ResponseDriver result = driverService.onBoardDriver(requestDriver);
        assertFalse(result.isStatus());
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void checkApplicationStatus_DriverExists_Success() {
        String licenseNumber = "123";
        Driver driver = new Driver();
        driver.setStatus(DriverStatus.APPLICATION_SUBMITTED);
        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(driver);
        String result = driverService.checkApplicationStatus(licenseNumber);
        assertEquals(DriverStatus.APPLICATION_SUBMITTED.toString(), result);
    }

    @Test
    void checkApplicationStatus_DriverDoesNotExist_Success() {
        String licenseNumber = "123";
        when(driverRepository.findByLicenseNumber(licenseNumber)).thenReturn(null);
        String result = driverService.checkApplicationStatus(licenseNumber);
        assertNull(result);
    }
}

