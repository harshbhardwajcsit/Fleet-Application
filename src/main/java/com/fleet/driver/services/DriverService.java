package com.fleet.driver.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.fleet.common.dto.DeliveryDto;
import com.fleet.common.dto.ShiftDto;
import com.fleet.common.helpers.ApiResponse;

public interface DriverService {
    ResponseEntity<ApiResponse<ShiftDto>> startShift(UUID driverId, UUID shiftId);

    ResponseEntity<ApiResponse<ShiftDto>> endShift(UUID driverId, UUID shiftId);

    ResponseEntity<ApiResponse<List<ShiftDto>>> viewShifts(UUID driverId);

    ResponseEntity<ApiResponse<List<DeliveryDto>>> viewDeliveries(UUID driverId);

    // public ResponseEntity<ApiResponse<OrderDto>> updateLocation(UUID deliveryId, String status);

}
