package com.intuit.craftdemo.service.impl;

import com.intuit.craftdemo.dto.driver.DriverDetails;
import com.intuit.craftdemo.entities.Driver;
import com.intuit.craftdemo.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DriverDetailsService implements UserDetailsService {

    private final DriverRepository driverRepository;

    @Autowired
    public DriverDetailsService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Driver driver = driverRepository.findByEmail(email);
        if(driver == null )
        {
            throw new UsernameNotFoundException("Driver not found with email: " + email);
        }
        return new DriverDetails(driver.getEmail(), driver.getPassword(), new ArrayList<>());
    }
}
