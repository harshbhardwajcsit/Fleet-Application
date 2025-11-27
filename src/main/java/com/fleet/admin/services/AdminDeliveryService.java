package com.fleet.admin.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.fleet.admin.requests.CreateDeliveryRequest;
import com.fleet.admin.requests.UpdateDeliveryRequest;
import com.fleet.common.dto.DeliveryDto;
import com.fleet.common.helpers.ApiResponse;

public interface AdminDeliveryService {

    ResponseEntity<ApiResponse<DeliveryDto>> createDelivery(CreateDeliveryRequest request);

    ResponseEntity<ApiResponse<DeliveryDto>> updateDelivery(UpdateDeliveryRequest request);

    ResponseEntity<ApiResponse<DeliveryDto>> getDelivery(UUID id);
    
}