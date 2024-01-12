package com.intuit.craftdemo.repository;

import com.intuit.craftdemo.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByLicenseNumber(String licenseNumber);

    Driver findByEmail(String email);

    @Modifying
    @Query("UPDATE Driver d SET d.status = :status WHERE d.id = :driverId")
    void updateDriverStatusById(@Param("driverId") Long driverId, @Param("status") String status);



}
