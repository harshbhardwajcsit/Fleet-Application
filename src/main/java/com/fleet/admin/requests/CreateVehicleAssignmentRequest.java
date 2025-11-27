package com.fleet.admin.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class CreateVehicleAssignmentRequest {
    @NotNull
    private UUID vehicleId;

    @NotNull
    private UUID driverId;

    @NotNull
    private UUID assignedBy;

    @NotNull
    private LocalDate assignedDate;

}
