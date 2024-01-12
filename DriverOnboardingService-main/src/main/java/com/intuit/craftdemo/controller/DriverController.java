package com.intuit.craftdemo.controller;

import com.intuit.craftdemo.dto.ApplicationStatusResponse;
import com.intuit.craftdemo.dto.driver.RequestDriver;
import com.intuit.craftdemo.dto.driver.ResponseDriver;
import com.intuit.craftdemo.enums.ApplicationNextSteps;
import com.intuit.craftdemo.enums.DriverStatus;
import com.intuit.craftdemo.service.IDocumentService;
import com.intuit.craftdemo.service.IDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("driver")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class DriverController {

    private final IDriverService driverService;

    private final IDocumentService documentService;

    @Autowired
    public DriverController(IDriverService driverService, IDocumentService documentService) {
        this.driverService = driverService;
        this.documentService = documentService;
    }

    private static final Logger log = LoggerFactory.getLogger(DriverController.class);

    @PostMapping("/onboard")
    public ResponseEntity<ResponseDriver> onboardDriver(@RequestBody @Valid RequestDriver driverDetails, BindingResult bindingResult) {
        log.debug("Driver onboarding request Received {}",driverDetails);
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }
        ResponseDriver response = driverService.onBoardDriver(driverDetails);
        return response.isStatus()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/checkStatus/{licenseNumber}")
    public ResponseEntity<ApplicationStatusResponse> checkApplicationStatus(@PathVariable String licenseNumber) {
        String status = driverService.checkApplicationStatus(licenseNumber);
        log.debug("Status for driver with licenseNumber {} is {}", licenseNumber, status);
        if (status == null) {
            return ResponseEntity.notFound().build();
        }
        ApplicationStatusResponse.ApplicationStatusResponseBuilder responseBuilder = ApplicationStatusResponse.builder().status(status);

        if (DriverStatus.APPLICATION_SUBMITTED.toString().equalsIgnoreCase(status)) {
            responseBuilder
                    .nextStep(ApplicationNextSteps.UPLOAD_DOCUMENTS.toString())
                    .nextStepsProperties(documentService.getNonSubmittedDocumentsList(licenseNumber));
        } else if (DriverStatus.DOCUMENTATION_VERIFICATION_FAILED.toString().equalsIgnoreCase(status)) {
            responseBuilder
                    .nextStep(ApplicationNextSteps.UPLOAD_DOCUMENTS.toString())
                    .nextStepsProperties(documentService.getVerificationFailedDocumentList(licenseNumber));
        }
        return ResponseEntity.ok(responseBuilder.build());
    }


    public ResponseEntity<ResponseDriver> handleValidationErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ResponseDriver response = new ResponseDriver();
        response.setErrorMessage(errors.toString());
        response.setStatus(false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

