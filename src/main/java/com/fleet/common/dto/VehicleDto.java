package com.fleet.common.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class VehicleDto {
    private UUID id;
    private String model;
    private String licensePlate;
    private Instant createdAt;
    private Instant updatedAt;
}
