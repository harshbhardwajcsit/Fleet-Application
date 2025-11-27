package com.fleet.common.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fleet.common.enums.OrderStatus;

import lombok.Data;

@Data
public class OrderDto {
    private UUID id;
    private TerminalDto pickupTerminal;
    private TerminalDto dropoffTerminal;
    private ProductDto product;
    private BigDecimal quantity;
    private String unit;
    private OrderStatus status;
    private AdminDto createdBy;
    private AdminDto updatedBy;
    private Instant updatedAt;
    private Instant createdAt;

}
