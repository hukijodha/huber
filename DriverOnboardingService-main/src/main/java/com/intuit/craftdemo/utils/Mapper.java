package com.intuit.craftdemo.utils;

import com.intuit.craftdemo.dto.driver.RequestDriver;
import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.enums.DriverStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapper {

    public static Driver mapIncomingRequestToEntity(RequestDriver requestDriver, Driver driverEntity) {
        if (driverEntity == null) {
            driverEntity = new Driver();
        }
        driverEntity.setFirstName(requestDriver.getFirstName());
        driverEntity.setLastName(requestDriver.getLastName());
        driverEntity.setPhoneNumber(requestDriver.getPhoneNumber());
        driverEntity.setAddressLine1(requestDriver.getAddressLine1());
        driverEntity.setLicenseNumber(requestDriver.getLicenseNumber());
        driverEntity.setAddressLine2(requestDriver.getAddressLine2());
        driverEntity.setCountry(requestDriver.getCountry());
        driverEntity.setCity(requestDriver.getCity());
        driverEntity.setState(requestDriver.getState());
        driverEntity.setPinCode(requestDriver.getPinCode());
        driverEntity.setHighestQualification(requestDriver.getHighestQualification());
        driverEntity.setHasDrivingExperience(requestDriver.isHasDrivingExperience());
        driverEntity.setLastEmployerDetails(requestDriver.getLastEmployerDetails());
        driverEntity.setAcceptTerms(requestDriver.isAcceptTerms());
        driverEntity.setStatus(DriverStatus.APPLICATION_SUBMITTED);
        driverEntity.setEmail(requestDriver.getEmail());
        driverEntity.setPassword(requestDriver.getPassword());

        Map<String, String> documents = new HashMap<>();
        List<String> documentTypes = requestDriver.getDocuments();
        if (documentTypes != null) {
            for (String documentType : documentTypes) {
                documents.put(documentType, "");
            }
        }
        driverEntity.setDocuments(documents);
        driverEntity.setKnownLanguages(requestDriver.getKnownLanguages());
        return driverEntity;
    }
}
