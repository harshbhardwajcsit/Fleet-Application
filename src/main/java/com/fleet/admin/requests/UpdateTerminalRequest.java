package com.fleet.admin.requests;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTerminalRequest {
    @Schema(description = "Updated terminal name", example = "Northern Terminal", required = true)
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Schema(description = "Updated terminal location", example = "Delhi NCR", required = true)
    @NotBlank(message = "Location cannot be blank")
    private String location;

    @Schema(description = "Updated contact number", example = "+91-9876543210")
    private String contactNumber;

    @Schema(description = "Updated latitude", example = "28.6139", required = true)
    @NotNull(message = "Latitude must be provided")
    @DecimalMin(value = "-90.0", message = "Latitude cannot be less than -90")
    @DecimalMax(value = "90.0", message = "Latitude cannot be greater than 90")
    private Double latitude;

    @Schema(description = "Updated longitude", example = "77.2090", required = true)
    @NotNull(message = "Longitude must be provided")
    @DecimalMin(value = "-180.0", message = "Longitude cannot be less than -180")
    @DecimalMax(value = "180.0", message = "Longitude cannot be greater than 180")
    private Double longitude;

    @Schema(description = "Updated terminal type", example = "WAREHOUSE", required = true)
    @NotBlank(message = "Type cannot be blank")
    private String type;

    @Schema(description = "Updated inventory amount", example = "10000", required = true)
    @NotNull(message = "Inventory must be provided")
    @Min(value = 0, message = "Inventory cannot be negative")
    private BigDecimal inventory;

    @Schema(description = "Updated inventory unit", example = "GALLONS", required = true)
    @NotBlank(message = "Unit cannot be blank")
    private String unit;
}
