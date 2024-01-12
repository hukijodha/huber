package com.intuit.craftdemo.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class RequestDriver {
    @NotBlank(message="firstName can't be empty or null")
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String addressLine1;
    @NotBlank(message="License Number can't be empty or null")
    private String licenseNumber;
    private String addressLine2;
    private String country;
    private String city;
    private String state;
    private String pinCode;
    private String highestQualification;
    private List<String> documents;
    private List<String> knownLanguages;
    private boolean hasDrivingExperience;
    private String lastEmployerDetails;
    private boolean acceptTerms;
    private String status;
    @NotBlank(message="Email can't be empty or null")
    private String email;
    @NotBlank(message="Password can't be empty or null")
    private String password;
}
