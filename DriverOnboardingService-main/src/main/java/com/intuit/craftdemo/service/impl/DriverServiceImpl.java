package com.intuit.craftdemo.service.impl;

import com.intuit.craftdemo.dto.driver.RequestDriver;
import com.intuit.craftdemo.dto.driver.ResponseDriver;
import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.repository.DriverRepository;
import com.intuit.craftdemo.service.IDriverService;
import com.intuit.craftdemo.utils.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements IDriverService {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository, PasswordEncoder passwordEncoder) {
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final Logger log = LoggerFactory.getLogger(DriverServiceImpl.class);

    @Override
    public ResponseDriver onBoardDriver(RequestDriver driverDetails) {
        ResponseDriver response = new ResponseDriver();
        try {
            Driver driver = new Driver();
            driverDetails.setPassword(passwordEncoder.encode(driverDetails.getPassword()));
            driverRepository.save(Mapper.mapIncomingRequestToEntity(driverDetails, driver));
            log.info("Driver with License [ {} ]'s info saved in Db", driverDetails.getLicenseNumber());
        }
        catch(DataIntegrityViolationException e){
            response.setStatus(false);
            response.setErrorMessage("Some of the values are not correct Please Check" +e.getLocalizedMessage());
            return response;
        }
        response.setStatus(true);
        return response;
    }

    @Override
    public String checkApplicationStatus(String licenseNumber) {
        Driver driver = driverRepository.findByLicenseNumber(licenseNumber);
        return (driver != null) ? driver.getStatus().toString() : null;   }


}
