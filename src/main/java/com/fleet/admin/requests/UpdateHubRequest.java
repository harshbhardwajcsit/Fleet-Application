package com.fleet.admin.requests;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class UpdateHubRequest {

    @Schema(description = "Updated name of the hub", example = "Northern Fuel Hub")
    @Size(min = 1, message = "Name cannot be empty")
    private String name;

    @Schema(description = "Updated hub location", example = "Delhi NCR")
    @Size(min = 1, message = "Location cannot be empty")
    private String location;

    @Schema(description = "Updated contact number", example = "+91-9000090000")
    private String contactNumber;

    @Schema(description = "Updated inventory level", example = "8000")
    @Min(value = 0, message = "Inventory cannot be negative")
    private BigDecimal inventory;

    @Schema(description = "Updated unit of measurement", example = "GALLONS")
    @Size(min = 1, message = "Unit cannot be empty")
    private String unit;

    @Schema(description = "Updated latitude value", example = "28.6139")
    @DecimalMin(value = "-90.0", message = "Latitude cannot be less than -90")
    @DecimalMax(value = "90.0", message = "Latitude cannot be greater than 90")
    private Double latitude;

    @Schema(description = "Updated longitude value", example = "77.2090")
    @DecimalMin(value = "-180.0", message = "Longitude cannot be less than -180")
    @DecimalMax(value = "180.0", message = "Longitude cannot be greater than 180")
    private Double longitude;
}
