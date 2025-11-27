package com.fleet.common.mapper;

import com.fleet.admin.requests.CreateDeliveryRequest;
import com.fleet.admin.requests.UpdateDeliveryRequest;
import com.fleet.common.dto.DeliveryDto;
import com.fleet.common.entity.*;
import java.time.Instant;

public class DeliveryMapper {

    public static DeliveryDto toDto(Delivery delivery) {
        if (delivery == null)
            return null;

        DeliveryDto dto = new DeliveryDto();

        dto.setId(delivery.getId());
        dto.setOrder(OrderMapper.toDto(delivery.getOrder()));
        dto.setDriver(DriverMapper.toDto(delivery.getDriver()));
        dto.setShift(ShiftMapper.toDto(delivery.getShift()));
        dto.setVehicle(VehicleMapper.toDto(delivery.getVehicle()));

        dto.setScheduledStartTime(delivery.getScheduledStartTime());
        dto.setScheduledEndTime(delivery.getScheduledEndTime());
        dto.setActualEndTime(delivery.getActualEndTime());

        dto.setStatus(delivery.getStatus());

        dto.setAdminRemark(delivery.getAdminRemark());
        dto.setDriverRemark(delivery.getDriverRemark());

        dto.setCreatedAt(delivery.getCreatedAt());
        dto.setUpdatedAt(delivery.getUpdatedAt());

        return dto;
    }

    // ----------------- CREATE -----------------

    public static Delivery toEntity(
            CreateDeliveryRequest req,
            Order order,
            Driver driver,
            Shift shift, Vehicle vehicle) {
        Delivery delivery = new Delivery();

        delivery.setOrder(order);
        delivery.setDriver(driver);
        delivery.setShift(shift);
        delivery.setVehicle(vehicle);

        delivery.setScheduledStartTime(req.getScheduledStartTime());
        delivery.setScheduledEndTime(req.getScheduledEndTime());

        delivery.setStatus(req.getStatus());
        delivery.setAdminRemark(req.getAdminRemark());

        delivery.setCreatedAt(Instant.now());

        return delivery;
    }

    // ----------------- UPDATE -----------------

    public static void updateEntity(
            Delivery delivery,
            UpdateDeliveryRequest req,
            Driver driver,
            Shift shift, Vehicle vehicle) {

        if (driver != null)
            delivery.setDriver(driver);
        if (shift != null)
            delivery.setShift(shift);

        if( vehicle != null)
            delivery.setVehicle(vehicle);   
        if (req.getScheduledStartTime() != null)
            delivery.setScheduledStartTime(req.getScheduledStartTime());
        if (req.getScheduledEndTime() != null)
            delivery.setScheduledEndTime(req.getScheduledEndTime());

        if (req.getStatus() != null)
            delivery.setStatus(req.getStatus());

        if (req.getAdminRemark() != null)
            delivery.setAdminRemark(req.getAdminRemark());

        delivery.setUpdatedAt(Instant.now());
    }
}
