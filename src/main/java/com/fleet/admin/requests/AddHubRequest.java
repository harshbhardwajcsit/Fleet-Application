package com.fleet.admin.requests;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class AddHubRequest {

    @Schema(description = "Name of the hub", example = "North Distribution Hub", required = true)
    @NotBlank(message = "Hub name cannot be empty")
    private String name;

    @Schema(description = "Hub location / address", example = "Mumbai, Maharashtra", required = true)
    @NotBlank(message = "Location cannot be empty")
    private String location;

    @Schema(description = "Contact number of the hub (optional)", example = "+91-9876543210")
    private String contactNumber;

    @Schema(description = "Initial inventory quantity at the hub", example = "5000", required = true)
    @NotNull(message = "Inventory cannot be null")
    @Min(value = 0, message = "Inventory cannot be negative")
    private BigDecimal inventory;

    @Schema(description = "Unit type of inventory", example = "GALLONS", required = true)
    @NotBlank(message = "Unit cannot be empty")
    private String unit;

    @Schema(description = "Latitude of hub location", example = "19.0760", required = true)
    @NotNull(message = "Latitude cannot be null")
    @DecimalMin(value = "-90.0", message = "Latitude cannot be less than -90")
    @DecimalMax(value = "90.0", message = "Latitude cannot be greater than 90")
    private Double latitude;

    @Schema(description = "Longitude of hub location", example = "72.8777", required = true)
    @NotNull(message = "Longitude cannot be null")
    @DecimalMin(value = "-180.0", message = "Longitude cannot be less than -180")
    @DecimalMax(value = "180.0", message = "Longitude cannot be greater than 180")
    private Double longitude;


}
