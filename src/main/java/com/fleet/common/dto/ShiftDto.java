package com.fleet.common.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ShiftDto {
    private UUID id;
    private LocalTime startTime;
    private LocalTime endTime;
    private UUID driverId;
    private LocalTime actualStartTime;
    private LocalTime actualEndTime;
    private BigDecimal totalWorkingHours;
    private LocalDate date;
    private Instant createdAt;
    private Instant updatedAt;
}
