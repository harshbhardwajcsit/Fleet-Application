package com.fleet.admin.requests;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddTerminalRequest {

    @Schema(example = "North Terminal")
    @NotBlank
    private String name;

    @Schema(example = "Mumbai, India")
    @NotBlank
    private String location;

    @Schema(example = "+91-9876543210")
    private String contactNumber;

    @Schema(example = "19.0760")
    @NotNull
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double latitude;

    @Schema(example = "72.8777")
    @NotNull
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double longitude;

    @Schema(example = "WAREHOUSE")
    @NotBlank
    private String type;

    @Schema(example = "9000")
    @NotNull
    @Min(0)
    private BigDecimal inventory;

    @Schema(example = "GALLONS")
    @NotBlank
    private String unit;
}
