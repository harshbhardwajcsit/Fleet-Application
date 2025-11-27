package com.fleet.common.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fleet.common.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    public boolean existsByLicenseNumber(String licenseNumber);
}
