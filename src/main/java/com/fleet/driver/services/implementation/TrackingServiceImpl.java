package com.fleet.driver.services.implementation;

import com.fleet.common.redis.RedisKeys;
import com.fleet.common.constants.ShiftConstants;
import com.fleet.common.dto.DriverLocationDto;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.common.helpers.ApiResponseBuilder;
import com.fleet.driver.services.TrackingService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    private final StringRedisTemplate stringRedisTemplate;
    private final ApiResponseBuilder responseBuilder;

    private static final long LOCATION_TTL_SECONDS = 600; // 10 mins for safety

    @Override
    public void updateLocation(UUID driverId, double lat, double lon, Long timestamp) {
        if (driverId == null) {
            throw new IllegalArgumentException("driverId cannot be null");
        }

        String key = RedisKeys.DRIVER_LOCATION + driverId;

        Map<String, String> fields = new HashMap<>();
        fields.put("lat", String.valueOf(lat));
        fields.put("lon", String.valueOf(lon));
        fields.put("timestamp", String.valueOf(timestamp));

        stringRedisTemplate.opsForHash().putAll(key, fields);
        stringRedisTemplate.expire(key, LOCATION_TTL_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public ResponseEntity<ApiResponse<Map<UUID, DriverLocationDto>>> getActiveDrivers() {

        // Fetch all drivers who have an active shift (from Redis SET)
        Set<String> drivers = stringRedisTemplate.opsForSet().members(RedisKeys.ACTIVE_DRIVERS);

        if (drivers == null || drivers.isEmpty()) {
            return responseBuilder.success(HttpStatus.OK, "Active drivers", Collections.emptyMap());
        }

        // Convert driver IDs → UUID
        Set<UUID> driverIds = drivers.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        Map<UUID, DriverLocationDto> result = loadDriverLocations(driverIds);

        return responseBuilder.success(HttpStatus.OK, "Active drivers", result);
    }

    @Override
    public ResponseEntity<ApiResponse<Map<UUID, DriverLocationDto>>> getDriversOnDutyDelivery() {

        // Fetch all drivers currently on delivery
        Set<String> drivers = stringRedisTemplate.opsForSet().members(RedisKeys.DRIVERS_ON_DELIVERY);

        if (drivers == null || drivers.isEmpty()) {
            return responseBuilder.success(HttpStatus.OK, "Drivers on duty", Collections.emptyMap());
        }

        Set<UUID> driverIds = drivers.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        Map<UUID, DriverLocationDto> result = loadDriverLocations(driverIds);

        return responseBuilder.success(HttpStatus.OK, "Drivers on duty", result);

    }

    @Override
    public ResponseEntity<ApiResponse<Set<UUID>>> getAvailableVehicles() {

        Set<String> vehicles = stringRedisTemplate.opsForSet().members(RedisKeys.AVAILABLE_VEHICLES);

        if (vehicles == null || vehicles.isEmpty()) {
            return responseBuilder.success(HttpStatus.OK, "Available Vehicles", Collections.emptySet());
        }

        Set<UUID> vehicleIds = vehicles.stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        return responseBuilder.success(HttpStatus.OK, "Available Vehicles", vehicleIds);
    }

    private Map<UUID, DriverLocationDto> loadDriverLocations(Set<UUID> driverIds) {

        Map<UUID, DriverLocationDto> result = new HashMap<>();

        for (UUID id : driverIds) {
            String key = RedisKeys.DRIVER_LOCATION + id;

            Map<Object, Object> data = stringRedisTemplate.opsForHash().entries(key);

            if (data == null || data.isEmpty())
                continue;

            DriverLocationDto dto = new DriverLocationDto();
            dto.setLatitude(Double.parseDouble((String) data.get("lat")));
            dto.setLongitude(Double.parseDouble((String) data.get("lon")));
            dto.setTimestamp(Long.parseLong((String) data.get("timestamp")));

            result.put(id, dto);
        }

        return result;
    }

    @Override
    public void removeFromActiveDrivers(UUID driverId) {
        if (driverId == null)
            return;

        stringRedisTemplate.opsForSet()
                .remove(RedisKeys.ACTIVE_DRIVERS, driverId.toString());
    }

    @Override
    public void removeFromAvailableVehicles(UUID vehicleId) {
        if (vehicleId == null)
            return;

        stringRedisTemplate.opsForSet()
                .remove(RedisKeys.AVAILABLE_VEHICLES, vehicleId.toString());
    }

    @Override
    public void removeFromDriversOnDutyDelivery(UUID driverId) {
        if (driverId == null)
            return;

        stringRedisTemplate.opsForSet()
                .remove(RedisKeys.DRIVERS_ON_DELIVERY, driverId.toString());
    }

    @Override
    public void addToActiveDrivers(UUID driverId) {
        if (driverId == null)
            return;

        stringRedisTemplate.opsForSet()
                .add(RedisKeys.AVAILABLE_VEHICLES, driverId.toString());
    }

    @Override
    public void addToAvailableVehicles(UUID vehicleId) {
        if (vehicleId == null)
            return;

        stringRedisTemplate.opsForSet()
                .add(RedisKeys.ACTIVE_DRIVERS, vehicleId.toString());
    }

    @Override
    public void addToDriversOnDutyDelivery(UUID driverId) {
        if (driverId == null)
            return;

        stringRedisTemplate.opsForSet()
                .add(RedisKeys.DRIVERS_ON_DELIVERY, driverId.toString());
    }

    @Override
    public void removeDriverFromAll(UUID driverId) {
        if (driverId == null)
            return;

        String id = driverId.toString();

        stringRedisTemplate.opsForSet().remove(RedisKeys.ACTIVE_DRIVERS, id);
        stringRedisTemplate.opsForSet().remove(RedisKeys.DRIVERS_ON_DELIVERY, id);

        // remove driver location as well
        stringRedisTemplate.delete(RedisKeys.DRIVER_LOCATION + id);
    }

}
