package com.fleet.common.mapper;


import java.time.Instant;

import com.fleet.admin.requests.RegisterDriverRequest;
import com.fleet.admin.requests.UpdateDriverRequest;
import com.fleet.common.dto.DriverDto;
import com.fleet.common.entity.Driver;

public class DriverMapper {

    public static DriverDto toDto(Driver driver) {
        if (driver == null) return null;

        DriverDto dto = new DriverDto();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setPhoneNumber(driver.getPhoneNumber());
        dto.setCreatedAt(driver.getCreatedAt());
        dto.setUpdatedAt(driver.getUpdatedAt());

        return dto;
    }

    public static Driver toEntity(RegisterDriverRequest request) {
        Driver d = new Driver();
        d.setName(request.getName());
        d.setLicenseNumber(request.getLicenseNumber());
        d.setPhoneNumber(request.getPhoneNumber());
        d.setCreatedAt(Instant.now());
        d.setUpdatedAt(Instant.now());
        return d;
    }

    public static void updateEntity(Driver d, UpdateDriverRequest req) {
        if (req.getName() != null) d.setName(req.getName());
        if (req.getLicenseNumber() != null) d.setLicenseNumber(req.getLicenseNumber());
        if (req.getPhoneNumber() != null) d.setPhoneNumber(req.getPhoneNumber());
        d.setUpdatedAt(Instant.now());
    }
}

