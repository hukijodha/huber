package com.intuit.craftdemo.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.craftdemo.dto.jwt.JwtRequest;
import com.intuit.craftdemo.dto.jwt.JwtResponse;
import com.intuit.craftdemo.service.ITokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TokenControllerTest {

    @Mock
    private ITokenService tokenService;

    @InjectMocks
    private TokenController tokenController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void generateToken_ValidRequest_Success() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("test@example.com", "password");
        String mockToken = "mockedToken";
        JwtResponse responseToken = new JwtResponse(mockToken);
        when(tokenService.generateToken(any(), any())).thenReturn(responseToken);
        ResponseEntity<?> responseEntity = tokenController.generateToken(jwtRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseToken, responseEntity.getBody());
    }

}

