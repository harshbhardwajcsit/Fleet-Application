package com.fleet.driver;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.fleet.common.dto.DeliveryDto;
import com.fleet.common.dto.DriverLocationDto;
import com.fleet.common.dto.ShiftDto;
import com.fleet.common.enums.DeliveryStatus;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.driver.services.DeliveryService;
import com.fleet.driver.services.DriverService;
import com.fleet.driver.services.TrackingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/driver")
@Tag(name = "Driver APIs", description = "Driver APIs for shifts management, Delivery APIs, Tracking APIs")
public class DriverController {

    private final DriverService driverService;
    private final DeliveryService deliveryService;
    private final TrackingService trackingService;

    // -------- SHIFTS ---------

    @Operation(summary = "Start shift")
    @PostMapping("/{driverId}/shift/{shiftId}/start")
    public ResponseEntity<ApiResponse<ShiftDto>> startShift(
            @PathVariable UUID driverId,
            @PathVariable UUID shiftId) {
        return driverService.startShift(driverId, shiftId);
    }

    @Operation(summary = "End shift")
    @PostMapping("/{driverId}/shift/{shiftId}/end")
    public ResponseEntity<ApiResponse<ShiftDto>> endShift(
            @PathVariable UUID driverId,
            @PathVariable UUID shiftId) {
        return driverService.endShift(driverId, shiftId);
    }

    @Operation(summary = "View driver's shifts")
    @GetMapping("/{driverId}/shifts")
    public ResponseEntity<ApiResponse<List<ShiftDto>>> viewShifts(@PathVariable UUID driverId) {
        return driverService.viewShifts(driverId);
    }

    // -------- DELIVERIES ---------

    @Operation(summary = "View deliveries for driver")
    @GetMapping("/{driverId}/deliveries")
    public ResponseEntity<ApiResponse<List<DeliveryDto>>> viewDeliveries(@PathVariable UUID driverId) {
        return driverService.viewDeliveries(driverId);
    }

    @Operation(summary = "Start a delivery")
    @PostMapping("/delivery/{deliveryId}/start")
    public ResponseEntity<ApiResponse<DeliveryDto>> startDelivery(@PathVariable UUID deliveryId) {
        return deliveryService.startDelivery(deliveryId);
    }

    @Operation(summary = "Complete a delivery")
    @PostMapping("/delivery/{deliveryId}/complete")
    public ResponseEntity<ApiResponse<DeliveryDto>> completeDelivery(@PathVariable UUID deliveryId) {
        return deliveryService.completeDelivery(deliveryId);
    }

    @Operation(summary = "Update delivery status with driver remark")
    @PostMapping("/delivery/{deliveryId}/status")
    public ResponseEntity<ApiResponse<DeliveryDto>> updateStatus(
            @PathVariable UUID deliveryId,
            @RequestParam DeliveryStatus status,
            @RequestParam(required = false) String remark) {
        return deliveryService.updateStatusWithRemark(deliveryId, remark, status);
    }

    @PostMapping("/tracking/{driverId}/location")
    @Operation(summary = "Update driver GPS location", description = "Called by driver app every 5 minutes to update live location.")
    public void updateLocation(
            @PathVariable UUID driverId,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(required = false) Long timestamp) {
        trackingService.updateLocation(driverId, lat, lon, timestamp);
    }

    @GetMapping("/tracking/active")
    @Operation(summary = "Get all active (idle) drivers", description = "Returns drivers who have active shift AND are not on a delivery.")
    public ResponseEntity<ApiResponse<Map<UUID, DriverLocationDto>>> getActiveDrivers() {
        return trackingService.getActiveDrivers();
    }

    @GetMapping("/tracking/on-delivery")
    @Operation(summary = "Get all drivers currently delivering", description = "Returns real-time locations of drivers who are on delivery.")
    public ResponseEntity<ApiResponse<Map<UUID, DriverLocationDto>>> getDriversOnDelivery() {
        return trackingService.getDriversOnDutyDelivery();

    }

    @GetMapping("/vehicles/available")
    @Operation(summary = "Get available vehicles", description = "Returns a list of vehicle IDs that are NOT assigned to any driver today.")
    public ResponseEntity<ApiResponse<Set<UUID>>> getAvailableVehicles() {
        return trackingService.getAvailableVehicles();

    }
}
