package com.fleet.admin.requests;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fleet.common.enums.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @Schema(description = "Pickup terminal ID", required = true)
    @NotNull(message = "Pickup terminal ID is required")
    private UUID pickupTerminalId;

    @Schema(description = "Dropoff terminal ID", required = true)
    @NotNull(message = "Dropoff terminal ID is required")
    private UUID dropoffTerminalId;

    
    @Schema(description = "Product ID", required = true)
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @Schema(description = "Quantity", example = "100.50", required = true)
    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.1", message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    @Schema(description = "Unit of measurement", example = "liters", required = true)
    @NotBlank(message = "Unit is required")
    private String unit;

    @Schema(description = "Order status", required = true, example = "CREATED", allowableValues = "CREATED, COMPLETED")
    @NotNull(message = "Status is required")
    private OrderStatus status;

    @Schema(description = "Admin ID creating the order", required = true)
    @NotNull(message = "CreatedBy Admin ID is required")
    private UUID createdBy;
}
