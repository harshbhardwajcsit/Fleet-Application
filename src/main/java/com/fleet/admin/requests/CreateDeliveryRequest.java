package com.fleet.admin.requests;


import com.fleet.common.enums.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class 
CreateDeliveryRequest {

    @Schema(description = "Order ID for which delivery is created", required = true)
    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @Schema(description = "Driver assigned to the delivery", required = true)
    @NotNull(message = "Driver ID is required")
    private UUID driverId;

    @Schema(description = "Shift for which delivery is scheduled", required = true)
    @NotNull(message = "Shift ID is required")
    private UUID shiftId;

    @Schema(description = "Hub where delivery was last visited (optional during creation)")
    private UUID lastVisitedHubId;

   

    @Schema(description = "Scheduled start time", required = true)
    private Instant scheduledStartTime;

    @Schema(description = "Scheduled end time",  required = true)
    private Instant scheduledEndTime;


    @Schema(description = "Delivery status", example = "ASSIGNED", required = true)
    @NotNull(message = "Delivery status is required")
    private DeliveryStatus status;


    @Schema(description = "Admin's remark on the delivery")
    private String adminRemark;
}

