package com.fleet.common.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class VehicleAssignmentDto {
    private UUID id;
    private UUID vehicleId;
    private UUID driverId;
    private UUID assignedBy;
    private Instant assignedAt;
    private LocalDate assignedDate;
    private Instant createdAt;
    private Instant updatedAt;
}
