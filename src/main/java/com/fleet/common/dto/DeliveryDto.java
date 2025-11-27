package com.fleet.common.dto;


import com.fleet.common.enums.DeliveryStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class DeliveryDto {

    private UUID id;

    private OrderDto order;
    private DriverDto driver;
    private ShiftDto shift;
    private VehicleDto vehicle;
    private HubDto lastVisitedHub;

    private Instant scheduledStartTime;
    private Instant scheduledEndTime;

    private Instant actualPickupTime;
    private Instant actualEndTime;

    private DeliveryStatus status;

    private String adminRemark;
    private String driverRemark;

    private Instant createdAt;
    private Instant updatedAt;
}
