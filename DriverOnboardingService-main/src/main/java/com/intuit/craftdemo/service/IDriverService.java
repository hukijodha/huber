package com.intuit.craftdemo.service;

import com.intuit.craftdemo.dto.driver.RequestDriver;
import com.intuit.craftdemo.dto.driver.ResponseDriver;

public interface IDriverService {

   ResponseDriver onBoardDriver(RequestDriver driverDetails);

   String checkApplicationStatus(String licenseNumber);
}
