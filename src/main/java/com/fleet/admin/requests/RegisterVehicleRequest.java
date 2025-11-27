package com.fleet.admin.requests;

import com.fleet.common.constants.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterVehicleRequest {

    @Schema(description = "Vehicle model", example = "Tesla Model 3", required = true)
    @NotBlank(message = "Model is required")
    private String model;

    @Schema(description = "License plate number", example = "AB-1234", required = true)
    @NotBlank(message = "License plate is required")
    @Pattern(regexp = ValidationConstants.LICENSE_PLATE_REGEX, message = "Invalid license plate format")
    private String licensePlate;
}

