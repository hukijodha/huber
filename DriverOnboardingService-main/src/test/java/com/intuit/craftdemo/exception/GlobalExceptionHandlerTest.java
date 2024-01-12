package com.intuit.craftdemo.exception;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleException_ShouldReturnInternalServerError() {
        ResponseEntity<String> response = exceptionHandler.handleException(new Exception());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleDriverNotFoundException_ShouldReturnNotFound() {
        ResponseEntity<String> response = exceptionHandler.handleDriverNotFoundException(new DriverNotFoundException(""));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleDocumentsNotFoundException_ShouldReturnNotFound() {
        ResponseEntity<byte[]> response = exceptionHandler.handleDocumentsNotFoundException(new DocumentsNotFoundException(""));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void handleDocumentUploadException_ShouldReturnInternalServerError() {
        DocumentUploadException ex = mock(DocumentUploadException.class);
        when(ex.getMessage()).thenReturn("Failed to upload document");
        ResponseEntity<String> response = exceptionHandler.handleDocumentUploadException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleDocumentUploadException_ShouldReturnBadRequest() {
        DocumentUploadException ex = mock(DocumentUploadException.class);
        when(ex.getMessage()).thenReturn("Validation failed");
        ResponseEntity<String> response = exceptionHandler.handleDocumentUploadException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleHttpMessageNotReadableException_ShouldReturnBadRequest() {
        ResponseEntity<String> response = exceptionHandler.handleHttpMessageNotReadableException(new HttpMessageNotReadableException("error"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleHttpRequestMethodNotSupportedException_ShouldReturnMethodNotAllowed() {
        ResponseEntity<String> response = exceptionHandler.handleHttpRequestMethodNotSupportedException(new HttpRequestMethodNotSupportedException("GET"));
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleAuthenticationFailedException_ShouldReturnForbidden() {
        ResponseEntity<String> response = exceptionHandler.handleAuthenticationFailedException(new AuthenticationFailedException("Authentication failed"));
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleBadCredentialsException_ShouldReturnUnauthorized() {
        ResponseEntity<String> response = exceptionHandler.handleBadCredentialsException(new BadCredentialsException("Bad credentials"));
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private static List< org.springframework.validation.FieldError> createFieldErrors() {
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("field1", "Error1",""));
        fieldErrors.add(new FieldError("field2", "Error2",""));
        return fieldErrors;
    }
}

