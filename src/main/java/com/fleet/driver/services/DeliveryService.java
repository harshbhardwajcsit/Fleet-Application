package com.fleet.driver.services;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.fleet.common.dto.DeliveryDto;
import com.fleet.common.enums.DeliveryStatus;
import com.fleet.common.helpers.ApiResponse;

public interface DeliveryService {

    ResponseEntity<ApiResponse<DeliveryDto>> startDelivery(UUID deliveryId);

    ResponseEntity<ApiResponse<DeliveryDto>> completeDelivery(UUID deliveryId);

    ResponseEntity<ApiResponse<DeliveryDto>> updateStatusWithRemark(UUID deliveryId, String remark, DeliveryStatus status);
    
} 