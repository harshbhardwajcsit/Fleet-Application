package com.fleet.admin.requests;

import com.fleet.common.constants.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateDriverRequest {

    @Schema(description = "Driver unique identifier", required = true)
    @NotNull(message = "Driver ID is required")
    private UUID id;

    @Schema(description = "Driver name", example = "John Doe")
    @Pattern(regexp = ValidationConstants.NAME_REGEX, message = "Invalid name format")
    private String name;

    @Schema(description = "Driver license number", example = "DL-9238472")
    @Pattern(regexp = ValidationConstants.LICENSE_REGEX, message = "Invalid license number format")
    private String licenseNumber;

    @Schema(description = "Phone number", example = "9876543210")
    @Pattern(regexp = ValidationConstants.PHONE_REGEX, message = "Invalid phone number")
    private String phoneNumber;
}

