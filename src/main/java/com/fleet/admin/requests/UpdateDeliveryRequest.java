package com.fleet.admin.requests;


import com.fleet.common.enums.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class UpdateDeliveryRequest {

    @Schema(description = "Delivery ID", required = true)
    @NotNull(message = "Delivery ID is required")
    private UUID id;

    @Schema(description = "Updated driver ID")
    private UUID driverId;

    @Schema(description = "Updated shift ID")
    private UUID shiftId;

    @Schema(description = "Update Status of the delivery")
    private DeliveryStatus status;


    @Schema(description = "Updated scheduled start time")
    private Instant scheduledStartTime;

    @Schema(description = "Updated scheduled end time")
    private Instant scheduledEndTime;

    @Schema(description = "Admin remark update")
    private String adminRemark;


}
