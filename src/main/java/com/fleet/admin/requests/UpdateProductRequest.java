package com.fleet.admin.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateProductRequest {

    @Schema(description = "Product ID", required = true)
    @NotNull(message = "Product ID is required")
    private UUID id;

    private String name;

    private String description;

    private Boolean isFlameable;

    private Boolean isHazardous;

    private Boolean isLiquid;
    
    private Boolean isFrazile;
}
