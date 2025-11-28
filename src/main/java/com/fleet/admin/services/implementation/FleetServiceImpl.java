package com.fleet.admin.services.implementation;

import com.fleet.admin.requests.*;
import com.fleet.admin.services.FleetService;
import com.fleet.common.constants.FleetConstants;
import com.fleet.common.dto.DriverDto;
import com.fleet.common.dto.VehicleAssignmentDto;
import com.fleet.common.dto.VehicleDto;
import com.fleet.common.entity.Admin;
import com.fleet.common.entity.Driver;
import com.fleet.common.entity.Vehicle;
import com.fleet.common.entity.VehicleAssignment;
import com.fleet.common.exceptions.AdminNotFoundException;
import com.fleet.common.exceptions.ConflictException;
import com.fleet.common.exceptions.DriverNotFoundException;
import com.fleet.common.exceptions.VehicleNotFoundException;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.common.helpers.ApiResponseBuilder;
import com.fleet.common.mapper.DriverMapper;
import com.fleet.common.mapper.VehicleAssignmentMapper;
import com.fleet.common.mapper.VehicleMapper;
import com.fleet.common.repository.AdminRepository;
import com.fleet.common.repository.DriverRepository;
import com.fleet.common.repository.VehicleAssignmentRepository;
import com.fleet.common.repository.VehicleRepository;
import com.fleet.driver.services.TrackingService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FleetServiceImpl implements FleetService {

        private final VehicleRepository vehicleRepository;
        private final DriverRepository driverRepository;
        private final ApiResponseBuilder responseBuilder;
        private final VehicleAssignmentRepository assignmentRepository;
        private final AdminRepository adminRepository;
        private final TrackingService trackingService;

        // -------------------- VEHICLES --------------------

        @Override
        public ResponseEntity<ApiResponse<VehicleDto>> registerVehicle(RegisterVehicleRequest request) {

                if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
                        return responseBuilder
                                        .failure(HttpStatus.CONFLICT, FleetConstants.VEHICLES_LICENSE_PLATE_CONFLICT);
                }

                Vehicle vehicle = VehicleMapper.toEntity(request);
                vehicleRepository.save(vehicle);

                return responseBuilder
                                .success(HttpStatus.CREATED, FleetConstants.VEHICLE_REGISTER_SUCCESS,
                                                VehicleMapper.toDto(vehicle));
        }

        @Override
        public ResponseEntity<ApiResponse<List<VehicleDto>>> getVehicles() {
                List<VehicleDto> result = vehicleRepository.findAll().stream()
                                .map(VehicleMapper::toDto)
                                .toList();

                return responseBuilder
                                .success(HttpStatus.OK, FleetConstants.VEHICLES_FETCH_SUCCESS, result);
        }

        @Override
        public ResponseEntity<ApiResponse<VehicleDto>> getVehicle(UUID id) {
                Vehicle vehicle = vehicleRepository.findById(id)
                                .orElseThrow(() -> new VehicleNotFoundException(FleetConstants.VEHICLE_NOT_FOUND));

                return responseBuilder
                                .success(HttpStatus.OK, FleetConstants.VEHICLE_FETCH_SUCCESS,
                                                VehicleMapper.toDto(vehicle));
        }

        @Override
        public ResponseEntity<ApiResponse<VehicleDto>> removeVehicle(UUID id) {
                Vehicle vehicle = vehicleRepository.findById(id)
                                .orElseThrow(() -> new VehicleNotFoundException(FleetConstants.VEHICLE_NOT_FOUND));
                vehicleRepository.delete(vehicle);

                return responseBuilder
                                .success(HttpStatus.OK, FleetConstants.VEHICLE_REMOVE_SUCCESS,
                                                VehicleMapper.toDto(vehicle));
        }

        // -------------------- DRIVERS -------------------

        @Override
        public ResponseEntity<ApiResponse<DriverDto>> registerDriver(RegisterDriverRequest request) {

                Driver driver = DriverMapper.toEntity(request);
                driverRepository.save(driver);

                return responseBuilder
                                .success(HttpStatus.CREATED, FleetConstants.DRIVER_REGISTER_SUCCESS,
                                                DriverMapper.toDto(driver));
        }

        @Override
        public ResponseEntity<ApiResponse<DriverDto>> updateDriver(UpdateDriverRequest request) {

                Driver driver = driverRepository.findById(request.getId())
                                .orElseThrow(() -> new DriverNotFoundException(FleetConstants.DRIVER_NOT_FOUND));

                DriverMapper.updateEntity(driver, request);
                driverRepository.save(driver);

                return responseBuilder
                                .success(HttpStatus.OK, FleetConstants.DRIVER_UPDATE_SUCCESS,
                                                DriverMapper.toDto(driver));
        }

        @Override
        public ResponseEntity<ApiResponse<List<DriverDto>>> getDrivers() {
                List<DriverDto> result = driverRepository.findAll().stream()
                                .map(DriverMapper::toDto)
                                .toList();

                return responseBuilder
                                .success(HttpStatus.OK, FleetConstants.DRIVERS_FETCH_SUCCESS, result);
        }

        @Override
        public ResponseEntity<ApiResponse<DriverDto>> getDriver(UUID id) {
                Driver driver = driverRepository.findById(id)
                                .orElseThrow(() -> new DriverNotFoundException(FleetConstants.DRIVER_NOT_FOUND));

                return responseBuilder
                                .success(HttpStatus.OK, FleetConstants.DRIVER_FETCH_SUCCESS,
                                                DriverMapper.toDto(driver));
        }

        @Override
        public ResponseEntity<ApiResponse<DriverDto>> removeDriver(UUID id) {
                Driver driver = driverRepository.findById(id)
                                .orElseThrow(() -> new DriverNotFoundException(FleetConstants.DRIVER_NOT_FOUND));
                driverRepository.delete(driver);

                return responseBuilder
                                .success(HttpStatus.OK, FleetConstants.DRIVER_REMOVE_SUCCESS,
                                                DriverMapper.toDto(driver));
        }

        @Override
        public ResponseEntity<ApiResponse<VehicleAssignmentDto>> assignVehicle(CreateVehicleAssignmentRequest request) {

                // insert inside transaction
                try {
                        VehicleAssignment saved = createAssignmentTransactional(request, request.getAssignedDate());

                        // Update cache: remove vehicle from active vehicles
                        trackingService.removeFromAvailableVehicles(request.getVehicleId());

                        return responseBuilder
                                        .success(HttpStatus.CREATED, FleetConstants.ASSIGNMENT_CREATE_SUCCESS,
                                                        VehicleAssignmentMapper.toDto(saved));
                } catch (DataIntegrityViolationException ex) {
                        // unique constraint violation in concurrent insert
                        throw new ConflictException(FleetConstants.ASSIGNMENT_CONFLICT);
                }
        }

        @Transactional
        private VehicleAssignment createAssignmentTransactional(CreateVehicleAssignmentRequest request,
                        LocalDate assignedDate) {
                Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                                .orElseThrow(() -> new VehicleNotFoundException(FleetConstants.VEHICLE_NOT_FOUND));
                Driver driver = driverRepository.findById(request.getDriverId())
                                .orElseThrow(() -> new DriverNotFoundException(FleetConstants.DRIVER_NOT_FOUND));
                Admin admin = adminRepository.findById(request.getAssignedBy())
                                .orElseThrow(() -> new AdminNotFoundException(FleetConstants.ADMIN_NOT_FOUND));

                // authoritative check inside tx
                if (assignmentRepository.existsByDriverIdAndAssignedDate(driver.getId(), assignedDate)) {
                        throw new ConflictException(FleetConstants.DRIVER_ASSIGNMENT_CONFLICT + assignedDate);
                }
                if (assignmentRepository.existsByVehicleIdAndAssignedDate(vehicle.getId(), assignedDate)) {
                        throw new ConflictException(FleetConstants.VEHICLE_ASSIGNMENT_CONFLICT + assignedDate);
                }

                Instant now = Instant.now();
                VehicleAssignment assignment = new VehicleAssignment();
                assignment.setVehicle(vehicle);
                assignment.setDriver(driver);
                assignment.setAssignedBy(admin);
                assignment.setAssignedDate(assignedDate);
                assignment.setAssignedAt(now);
                assignment.setCreatedAt(now);
                assignment.setUpdatedAt(now);

                // save & flush to catch unique constraint violations
                return assignmentRepository.saveAndFlush(assignment);
        }

}
