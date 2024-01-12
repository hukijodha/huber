package com.intuit.craftdemo.service.impl;

import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DriverDetailsServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverDetailsService driverDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUsername_ValidEmail_Success() {
        String email = "test@example.com";
        Driver mockDriver = new Driver();
        mockDriver.setEmail(email);
        mockDriver.setPassword("hashedPassword");

        when(driverRepository.findByEmail(email)).thenReturn(mockDriver);

        UserDetails userDetails = driverDetailsService.loadUserByUsername(email);

        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_InvalidEmail_UsernameNotFoundException() {
        String email = "nonexistent@example.com";

        when(driverRepository.findByEmail(email)).thenReturn(null);

        UsernameNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> driverDetailsService.loadUserByUsername(email)
        );
    }
}
