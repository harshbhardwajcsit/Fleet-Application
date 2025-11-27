package com.fleet.common.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class HubDto {
    private UUID id;
    private String name;
    private String location;   
    private String contactNumber;
    private BigDecimal inventory;
    private String unit;
    private Double latitude;
    private Double longitude;
    private Instant createdAt;
    private Instant updatedAt;
}
