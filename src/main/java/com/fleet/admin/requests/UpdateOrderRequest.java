package com.fleet.admin.requests;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fleet.common.enums.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateOrderRequest {

    @Schema(description = "Order ID", required = true)
    @NotNull(message = "Order ID is required")
    private UUID id;

    private UUID pickupTerminalId;
    private UUID dropoffTerminalId;
  
    private UUID productId;

    @DecimalMin(value = "0.1", message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    private String unit;

    @Schema(allowableValues = "CREATED, COMPLETED")
    private OrderStatus status;
  
    
    @Schema(description = "Admin updating the order", required = true)
    @NotNull(message = "UpdatedBy Admin ID is required")
    private UUID updatedBy;
}
