package com.fleet.common.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class DriverDto {
    private UUID id;
    private String name;
    private String licenseNumber;
    private String phoneNumber;
    private Instant createdAt;
    private Instant updatedAt; 
}
