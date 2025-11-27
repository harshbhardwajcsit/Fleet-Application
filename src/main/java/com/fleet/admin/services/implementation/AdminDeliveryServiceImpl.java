package com.fleet.admin.services.implementation;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fleet.admin.requests.CreateDeliveryRequest;
import com.fleet.admin.requests.UpdateDeliveryRequest;
import com.fleet.admin.services.AdminDeliveryService;
import com.fleet.common.constants.DeliveryConstants;
import com.fleet.common.constants.FleetConstants;
import com.fleet.common.constants.OrderConstants;
import com.fleet.common.constants.ShiftConstants;
import com.fleet.common.dto.DeliveryDto;
import com.fleet.common.entity.Delivery;
import com.fleet.common.entity.Driver;
import com.fleet.common.entity.Order;
import com.fleet.common.entity.Shift;
import com.fleet.common.entity.Vehicle;
import com.fleet.common.entity.VehicleAssignment;
import com.fleet.common.exceptions.DeliveryNotFoundException;
import com.fleet.common.exceptions.DriverNotFoundException;
import com.fleet.common.exceptions.OrderNotFoundException;
import com.fleet.common.exceptions.ShiftNotFoundException;
import com.fleet.common.exceptions.VehicleAssignmentException;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.common.helpers.ApiResponseBuilder;
import com.fleet.common.mapper.DeliveryMapper;
import com.fleet.common.repository.DeliveryRepository;
import com.fleet.common.repository.DriverRepository;
import com.fleet.common.repository.OrderRepository;
import com.fleet.common.repository.ShiftRepository;
import com.fleet.common.repository.VehicleAssignmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDeliveryServiceImpl implements AdminDeliveryService {

    private final OrderRepository orderRepository;

    private final ApiResponseBuilder responseBuilder;

    private final DriverRepository driverRepository;

    private final ShiftRepository shiftRepository;

    private final DeliveryRepository deliveryRepository;

    private final VehicleAssignmentRepository vehicleAssignmentRepository;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<DeliveryDto>> createDelivery(CreateDeliveryRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(OrderConstants.ORDER_NOT_FOUND));

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException(FleetConstants.DRIVER_NOT_FOUND));

        Shift shift = shiftRepository.findById(request.getShiftId())
                .orElseThrow(() -> new ShiftNotFoundException(ShiftConstants.SHIFT_NOT_FOUND));

        Vehicle vehicle = getActiveVehicleAssignmentForDriver(driver)
                .map(VehicleAssignment::getVehicle)
                .orElseThrow(() -> new VehicleAssignmentException(DeliveryConstants.NO_VEHICLE_ASSIGNED));

        Delivery delivery = DeliveryMapper.toEntity(request, order, driver, shift, vehicle);

        deliveryRepository.save(delivery);

        return responseBuilder.success(
                HttpStatus.CREATED,
                DeliveryConstants.DELIVERY_CREATE_SUCCESS,
                DeliveryMapper.toDto(delivery));
    }

    @Override
    public ResponseEntity<ApiResponse<DeliveryDto>> updateDelivery(UpdateDeliveryRequest request) {

        Delivery delivery = deliveryRepository.findById(request.getId())
                .orElseThrow(() -> new DeliveryNotFoundException(DeliveryConstants.DELIVERY_NOT_FOUND));

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException(FleetConstants.DRIVER_NOT_FOUND));

        Shift shift = shiftRepository.findById(request.getShiftId())
                .orElseThrow(() -> new ShiftNotFoundException(ShiftConstants.SHIFT_NOT_FOUND));

        Vehicle vehicle = getActiveVehicleAssignmentForDriver(driver)
                .map(VehicleAssignment::getVehicle)
                .orElseThrow(() -> new VehicleAssignmentException(DeliveryConstants.NO_VEHICLE_ASSIGNED));

        DeliveryMapper.updateEntity(delivery, request, driver, shift, vehicle);

        deliveryRepository.save(delivery);

        return responseBuilder.success(
                HttpStatus.OK,
                DeliveryConstants.DELIVERY_UPDATE_SUCCESS,
                DeliveryMapper.toDto(delivery));
    }

    @Override
    public ResponseEntity<ApiResponse<DeliveryDto>> getDelivery(java.util.UUID id) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(DeliveryConstants.DELIVERY_NOT_FOUND));

        return responseBuilder.success(
                HttpStatus.OK,
                DeliveryConstants.DELIVERY_FETCH_SUCCESS,
                DeliveryMapper.toDto(delivery));
    }

    private Optional<VehicleAssignment> getActiveVehicleAssignmentForDriver(Driver driver) {

        LocalDate todayUtc = LocalDate.now(ZoneOffset.UTC);

        return vehicleAssignmentRepository
                .findByDriverIdAndAssignedDate(
                        driver.getId(),
                        todayUtc);
    }

}
