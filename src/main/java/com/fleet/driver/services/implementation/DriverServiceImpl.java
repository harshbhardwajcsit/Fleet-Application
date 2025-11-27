package com.fleet.driver.services.implementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fleet.common.constants.DeliveryConstants;
import com.fleet.common.constants.FleetConstants;
import com.fleet.common.constants.ShiftConstants;
import com.fleet.common.dto.DeliveryDto;
import com.fleet.common.dto.ShiftDto;
import com.fleet.common.entity.Driver;
import com.fleet.common.entity.Shift;
import com.fleet.common.entity.Vehicle;
import com.fleet.common.entity.VehicleAssignment;
import com.fleet.common.exceptions.DriverNotFoundException;
import com.fleet.common.exceptions.ShiftNotFoundException;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.common.helpers.ApiResponseBuilder;
import com.fleet.common.mapper.DeliveryMapper;
import com.fleet.common.mapper.ShiftMapper;
import com.fleet.common.repository.DeliveryRepository;
import com.fleet.common.repository.DriverRepository;
import com.fleet.common.repository.ShiftRepository;
import com.fleet.common.repository.VehicleAssignmentRepository;
import com.fleet.driver.services.DriverService;
import com.fleet.driver.services.TrackingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements
        DriverService {

    private final DriverRepository driverRepository;
    private final ShiftRepository shiftRepository;
    private final DeliveryRepository deliveryRepository;
    private final VehicleAssignmentRepository vehicleAssignmentRepository;
    private final TrackingService trackingService;
    private final ApiResponseBuilder responseBuilder;

    @Override
    public ResponseEntity<ApiResponse<ShiftDto>> startShift(UUID driverId, UUID shiftId) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException(FleetConstants.DRIVER_NOT_FOUND));

        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException(ShiftConstants.SHIFT_NOT_FOUND));

        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        Boolean hasVehicleAssigned = vehicleAssignmentRepository
                .existsByDriverIdAndAssignedDate(driverId, today);

        // Driver cannot start shift without vehicle assigned

        if (!hasVehicleAssigned) {
            return responseBuilder.failure(HttpStatus.BAD_REQUEST, FleetConstants.NO_VEHICLE_ASSIGNED);

        }

        if (!shift.getDriver().getId().equals(driverId)) {
            throw new RuntimeException(ShiftConstants.INVALID_SHIFT_FOR_DRIVER);
        }

        if (shift.getActualStartTime() != null) {
            return responseBuilder.failure(HttpStatus.CONFLICT, ShiftConstants.SHIFT_ALREADY_STARTED);
        }

        shift.setActualStartTime(LocalTime.now(ZoneOffset.UTC));
        shiftRepository.save(shift);

        // Add driver to active drivers when shift started
        trackingService.addToActiveDrivers(driverId);

        return responseBuilder.success(HttpStatus.OK, ShiftConstants.SHIFT_STARTED, ShiftMapper.toDto(shift));
    }

    @Override
    public ResponseEntity<ApiResponse<ShiftDto>> endShift(UUID driverId, UUID shiftId) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException(FleetConstants.DRIVER_NOT_FOUND));

        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException(ShiftConstants.SHIFT_NOT_FOUND));

        if (!shift.getDriver().getId().equals(driverId)) {
            throw new RuntimeException(ShiftConstants.INVALID_SHIFT_FOR_DRIVER);
        }

        if (shift.getActualStartTime() == null) {
            return responseBuilder.failure(HttpStatus.BAD_REQUEST, ShiftConstants.SHIFT_NOT_STARTED);
        }

        if (shift.getActualEndTime() != null) {
            return responseBuilder.failure(HttpStatus.CONFLICT, ShiftConstants.SHIFT_ALREADY_ENDED);
        }

        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        Optional<VehicleAssignment> assignedVehicle = vehicleAssignmentRepository
                .findByDriverIdAndAssignedDate(driverId, today);

        shift.setActualEndTime(LocalTime.now(ZoneOffset.UTC));
        shiftRepository.save(shift);

        // Remove driver from all cache buckets when shift ends
        trackingService.removeDriverFromAll(driverId);

        // Update Vehicle to available Vehicle
        if (assignedVehicle.isPresent()) {
            Vehicle vehicle = assignedVehicle.get().getVehicle();
            trackingService.addToAvailableVehicles(vehicle.getId());
        }

        return responseBuilder.success(HttpStatus.OK, ShiftConstants.SHIFT_ENDED, ShiftMapper.toDto(shift));
    }

    @Override
    public ResponseEntity<ApiResponse<List<ShiftDto>>> viewShifts(UUID driverId) {

        List<ShiftDto> shifts = shiftRepository.findByDriverId(driverId)
                .stream()
                .map(ShiftMapper::toDto)
                .toList();

        return responseBuilder.success(HttpStatus.OK, ShiftConstants.SHIFT_FETCH_SUCCESS, shifts);
    }

    @Override
    public ResponseEntity<ApiResponse<List<DeliveryDto>>> viewDeliveries(UUID driverId) {

        List<DeliveryDto> deliveries = deliveryRepository.findByDriverId(driverId)
                .stream()
                .map(DeliveryMapper::toDto)
                .toList();

        return responseBuilder.success(HttpStatus.OK, DeliveryConstants.DELIVERY_FETCH_SUCCESS, deliveries);
    }
}
