package com.fleet.common.mapper;

import com.fleet.common.dto.VehicleAssignmentDto;
import com.fleet.common.entity.VehicleAssignment;

public class VehicleAssignmentMapper {

    public static VehicleAssignmentDto toDto(VehicleAssignment a) {
        if (a == null) return null;
        VehicleAssignmentDto dto = new VehicleAssignmentDto();
        dto.setId(a.getId());
        dto.setVehicleId(a.getVehicle() != null ? a.getVehicle().getId() : null);
        dto.setDriverId(a.getDriver() != null ? a.getDriver().getId() : null);
        dto.setAssignedBy(a.getAssignedBy() != null ? a.getAssignedBy().getId() : null);
        dto.setAssignedAt(a.getAssignedAt());
        dto.setAssignedDate(a.getAssignedDate());
        dto.setCreatedAt(a.getCreatedAt());
        dto.setUpdatedAt(a.getUpdatedAt());
        return dto;
    }
}

