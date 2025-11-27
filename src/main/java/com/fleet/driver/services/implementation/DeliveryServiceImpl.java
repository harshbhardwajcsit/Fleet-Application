package com.fleet.driver.services.implementation;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fleet.common.constants.DeliveryConstants;
import com.fleet.common.dto.DeliveryDto;
import com.fleet.common.entity.Delivery;
import com.fleet.common.entity.Order;
import com.fleet.common.entity.Terminal;
import com.fleet.common.enums.DeliveryStatus;
import com.fleet.common.exceptions.DeliveryNotFoundException;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.common.helpers.ApiResponseBuilder;
import com.fleet.common.mapper.DeliveryMapper;
import com.fleet.common.repository.DeliveryRepository;
import com.fleet.common.repository.TerminalRepository;
import com.fleet.driver.services.DeliveryService;
import com.fleet.driver.services.TrackingService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ApiResponseBuilder responseBuilder;
    private final TerminalRepository terminalRepository;
    private final TrackingService trackingService;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<DeliveryDto>> startDelivery(UUID deliveryId) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(DeliveryConstants.DELIVERY_NOT_FOUND));

        if (delivery.getActualPickupTime() != null) {
            return responseBuilder.failure(HttpStatus.CONFLICT, DeliveryConstants.DELIVERY_ALREADY_STARTED);
        }
        // ------ Managing inventory at pickup terminal ------//
        Order order = delivery.getOrder();

        Terminal pickupTerminal = order.getPickupTerminal();

        pickupTerminal.setInventory(pickupTerminal.getInventory().subtract(order.getQuantity()));

        terminalRepository.save(pickupTerminal);

        // ----------------------------------------------------//

        delivery.setStatus(DeliveryStatus.IN_PROGRESS);
        delivery.setActualPickupTime(Instant.now());

        deliveryRepository.save(delivery);

        // Updating cache buckets
        trackingService.addToDriversOnDutyDelivery(delivery.getDriver().getId());
        trackingService.removeFromActiveDrivers(delivery.getDriver().getId());

        return responseBuilder.success(HttpStatus.OK, DeliveryConstants.DELIVERY_STARTED,
                DeliveryMapper.toDto(delivery));
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<DeliveryDto>> completeDelivery(UUID deliveryId) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(DeliveryConstants.DELIVERY_NOT_FOUND));

        if (delivery.getActualPickupTime() == null) {
            return responseBuilder.failure(HttpStatus.BAD_REQUEST, DeliveryConstants.DELIVERY_NOT_STARTED);
        }

        if (delivery.getActualEndTime() != null) {
            return responseBuilder.failure(HttpStatus.CONFLICT, DeliveryConstants.DELIVERY_ALREADY_COMPLETED);
        }

        // ------ Managing inventory at Dropoff terminal ------//

        Order order = delivery.getOrder();

        Terminal dropOffTerminal = order.getDropoffTerminal();

        dropOffTerminal.setInventory(dropOffTerminal.getInventory().add(order.getQuantity()));

        terminalRepository.save(dropOffTerminal);

        // ----------------------------------------------------//

        delivery.setActualEndTime(Instant.now());
        delivery.setStatus(DeliveryStatus.COMPLETED);

        deliveryRepository.save(delivery);

        // Updating cache buckets
        trackingService.removeFromDriversOnDutyDelivery(delivery.getDriver().getId());
        trackingService.addToActiveDrivers(delivery.getDriver().getId());

        return responseBuilder.success(HttpStatus.OK, DeliveryConstants.DELIVERY_COMPLETED,
                DeliveryMapper.toDto(delivery));
    }

    @Override
    public ResponseEntity<ApiResponse<DeliveryDto>> updateStatusWithRemark(UUID deliveryId, String remark,
            DeliveryStatus status) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(DeliveryConstants.DELIVERY_NOT_FOUND));

        delivery.setDriverRemark(remark);
        delivery.setStatus(status);

        deliveryRepository.save(delivery);

        return responseBuilder.success(HttpStatus.OK, DeliveryConstants.DELIVERY_STATUS_UPDATED,
                DeliveryMapper.toDto(delivery));
    }
}
