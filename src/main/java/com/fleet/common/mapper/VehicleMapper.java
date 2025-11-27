package com.fleet.common.mapper;


import java.time.Instant;

import com.fleet.admin.requests.RegisterVehicleRequest;
import com.fleet.common.dto.VehicleDto;
import com.fleet.common.entity.Vehicle;

public class VehicleMapper {

    public static VehicleDto toDto(Vehicle vehicle) {
        if (vehicle == null) return null;

        VehicleDto dto = new VehicleDto();
        dto.setId(vehicle.getId());
        dto.setModel(vehicle.getModel());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setCreatedAt(vehicle.getCreatedAt());
        dto.setUpdatedAt(vehicle.getUpdatedAt());
        return dto;
    }

    public static Vehicle toEntity(RegisterVehicleRequest request) {
        Vehicle v = new Vehicle();
        v.setModel(request.getModel());
        v.setLicensePlate(request.getLicensePlate());
        v.setCreatedAt(Instant.now());
        v.setUpdatedAt(Instant.now());
        return v;
    }
}

