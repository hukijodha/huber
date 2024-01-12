package com.intuit.craftdemo.controller;

import com.intuit.craftdemo.dto.ApplicationStatusResponse;
import com.intuit.craftdemo.dto.driver.RequestDriver;
import com.intuit.craftdemo.dto.driver.ResponseDriver;
import com.intuit.craftdemo.enums.ApplicationNextSteps;
import com.intuit.craftdemo.enums.DriverStatus;
import com.intuit.craftdemo.service.IDocumentService;
import com.intuit.craftdemo.service.IDriverService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class DriverControllerTest {

    @Mock
    private IDriverService driverService;

    @Mock
    private IDocumentService documentService;

    @InjectMocks
    private DriverController driverController;

    @Test
    void onboardDriver_ValidInput_Success() {
        RequestDriver requestDriver = new RequestDriver();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        ResponseDriver responseDriver = new ResponseDriver();
        responseDriver.setStatus(true);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(driverService.onBoardDriver(requestDriver)).thenReturn(responseDriver);
        ResponseEntity<ResponseDriver> response = driverController.onboardDriver(requestDriver, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDriver, response.getBody());
        verify(driverService).onBoardDriver(requestDriver);
    }

    @Test
    void onboardDriver_InvalidInput_BadRequest() {
        RequestDriver requestDriver = new RequestDriver();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        ResponseEntity<ResponseDriver> response = driverController.onboardDriver(requestDriver, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(false, response.getBody().isStatus());
    }

    @Test
    void checkApplicationStatus_ApplicationSubmitted_Success() {
        String licenseNumber = "123";
        String status = DriverStatus.APPLICATION_SUBMITTED.toString();

        when(driverService.checkApplicationStatus(licenseNumber)).thenReturn(status);
        when(documentService.getNonSubmittedDocumentsList(licenseNumber)).thenReturn(Collections.singletonList("Document1"));
        ResponseEntity<ApplicationStatusResponse> response = driverController.checkApplicationStatus(licenseNumber);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(status, response.getBody().getStatus());
        assertEquals(ApplicationNextSteps.UPLOAD_DOCUMENTS.toString(), response.getBody().getNextStep());
        assertEquals(Collections.singletonList("Document1"), response.getBody().getNextStepsProperties());
    }

    @Test
    void checkApplicationStatus_DocumentationVerificationFailed_Success() {
        String licenseNumber = "123";
        String status = DriverStatus.DOCUMENTATION_VERIFICATION_FAILED.toString();

        when(driverService.checkApplicationStatus(licenseNumber)).thenReturn(status);
        when(documentService.getVerificationFailedDocumentList(licenseNumber)).thenReturn(Collections.singletonList("Document2"));

        ResponseEntity<ApplicationStatusResponse> response = driverController.checkApplicationStatus(licenseNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(status, response.getBody().getStatus());
        assertEquals(ApplicationNextSteps.UPLOAD_DOCUMENTS.toString(), response.getBody().getNextStep());
        assertEquals(Collections.singletonList("Document2"), response.getBody().getNextStepsProperties());
    }

    @Test
    void checkApplicationStatus_UnknownStatus_NotFound() {
        String licenseNumber = "123";
        when(driverService.checkApplicationStatus(licenseNumber)).thenReturn(null);

        ResponseEntity<ApplicationStatusResponse> response = driverController.checkApplicationStatus(licenseNumber);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleValidationErrors_ReturnsBadRequest() {
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("fieldName", "errorMessage","")));
        ResponseEntity<ResponseDriver> response = driverController.handleValidationErrors(bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(false, response.getBody().isStatus());
        assertEquals("{errorMessage=}", response.getBody().getErrorMessage());
    }
}


