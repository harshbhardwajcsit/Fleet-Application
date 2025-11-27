package com.fleet.driver.services;

import com.fleet.common.dto.DriverLocationDto;
import com.fleet.common.helpers.ApiResponse;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

/**
 * TrackingService provides real-time driver location tracking and availability
 * checks
 * using Redis as the primary data store.
 */
public interface TrackingService {

    /**
     * Updates the last known GPS location for the given driver.
     * 
     * @param driverId Unique identifier for the driver
     * @param lat      Latitude reported by the driver’s device
     * @param lon      Longitude reported by the driver’s device
     *
     * @throws IllegalArgumentException if driverId is null
     */
    void updateLocation(UUID driverId, double lat, double lon, Long timestamp);

    /**
     * Returns all drivers who:
     * 
     * @return A map of driverId → DriverLocationDto for all idle but active
     *         drivers.
     */
    ResponseEntity<ApiResponse<Map<UUID, DriverLocationDto>>> getActiveDrivers();

    /**
     * Returns the real-time locations of drivers who are currently on-duty
     * and actively serving a delivery.
     *
     * @return A map of driverId → DriverLocationDto for drivers on delivery.
     */
    ResponseEntity<ApiResponse<Map<UUID, DriverLocationDto>>> getDriversOnDutyDelivery();

    /**
     * Returns a set of vehicles that are currently free (idle) and not assigned to
     * any driver today.
     *
     * @return A set of vehicle UUIDs that are currently idle and available.
     */
    ResponseEntity<ApiResponse<Set<UUID>>> getAvailableVehicles();

    void removeFromActiveDrivers(UUID driverId);

    void removeFromDriversOnDutyDelivery(UUID driverId);

    void addToActiveDrivers(UUID driverId);

    void addToDriversOnDutyDelivery(UUID driverId);

    void removeDriverFromAll(UUID driverId);

    void removeFromAvailableVehicles(UUID vehicleId);

    void addToAvailableVehicles(UUID vehicleId);
}
