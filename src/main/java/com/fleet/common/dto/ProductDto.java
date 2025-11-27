package com.fleet.common.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class ProductDto {
    private UUID id;
    private String name;
    private String description;
    private Boolean isFlameable;
    private Boolean isHazardous;
    private Boolean isLiquid; 
    private Boolean isFrazile;
    private Instant createdAt;
    private Instant updatedAt;
}
