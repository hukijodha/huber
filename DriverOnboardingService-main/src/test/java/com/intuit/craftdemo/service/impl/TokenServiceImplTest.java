package com.intuit.craftdemo.service.impl;

import com.intuit.craftdemo.dto.jwt.JwtResponse;
import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.entities.Token;
import com.intuit.craftdemo.enums.TokenType;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.repository.TokenRepository;
import com.intuit.craftdemo.service.ITokenService;
import com.intuit.craftdemo.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TokenServiceImplTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void generateToken_ValidCredentials() throws Exception {
        String email = "test@example.com";
        String password = "password";
        String jwtToken = "testJwtToken";
        Driver driver = new Driver();
        driver.setId(1L);
        when(driverRepository.findByEmail(email)).thenReturn(driver);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(new User(email, password, new ArrayList<>()));
        when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn(jwtToken);
        JwtResponse response = tokenService.generateToken(email, password);
        assertNotNull(response);
        assertEquals(jwtToken, response.getJwttoken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(driverRepository, times(1)).findByEmail(email);
        verify(userDetailsService, times(1)).loadUserByUsername(email);
        verify(jwtTokenUtil, times(1)).generateToken(any(UserDetails.class));
        verify(tokenRepository, times(1)).save(any(Token.class));
        verify(tokenRepository, times(1)).findAllValidTokenByDriver(any(Long.class));
        verify(tokenRepository, times(0)).saveAll(anyList());
    }
}

