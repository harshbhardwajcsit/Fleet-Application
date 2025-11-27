package com.fleet.admin.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProductRequest {

    @Schema(description = "Product name", required = true)
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Product description")
    private String description;

    @Schema(description = "Is the product flammable?")
    private Boolean isFlameable;

    @Schema(description = "Is the product hazardous?")
    private Boolean isHazardous;

    @Schema(description = "Is the product liquid?")
    private Boolean isLiquid;

    @Schema(description = "Is the product fragile?")
    private Boolean isFrazile;
}
