package com.fleet.common.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DriverLocationDto {
    private UUID driverId;
    private Double latitude;
    private Double longitude;
    // milliseconds since epoch, easy to parse on all platforms
    private Long timestamp;
}

