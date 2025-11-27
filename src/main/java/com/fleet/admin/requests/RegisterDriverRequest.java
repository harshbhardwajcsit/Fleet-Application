package com.fleet.admin.requests;


import java.util.UUID;

import com.fleet.common.constants.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterDriverRequest {

    @Schema(description = "Driver name", example = "John Doe", required = true)
    @NotBlank(message = "Name is required")
    @Pattern(regexp = ValidationConstants.NAME_REGEX, message = "Invalid name format")
    private String name;

    @Schema(description = "Driver license number", example = "DL-9238472", required = true)
    @NotBlank(message = "License number is required")
    @Pattern(regexp = ValidationConstants.LICENSE_REGEX, message = "Invalid license number format")
    private String licenseNumber;

    @Schema(description = "Phone number", example = "9876543210", required = true)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = ValidationConstants.PHONE_REGEX, message = "Invalid phone number")
    private String phoneNumber;

}
