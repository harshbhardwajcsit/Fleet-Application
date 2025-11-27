package com.fleet.admin.services;

import com.fleet.admin.requests.CreateOrderRequest;
import com.fleet.admin.requests.UpdateOrderRequest;
import com.fleet.common.dto.OrderDto;
import com.fleet.common.helpers.ApiResponse;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    ResponseEntity<ApiResponse<OrderDto>> createOrder(CreateOrderRequest request);

    ResponseEntity<ApiResponse<OrderDto>> updateOrder(UpdateOrderRequest request);

    ResponseEntity<ApiResponse<OrderDto>> getOrder(UUID id);

    ResponseEntity<ApiResponse<List<OrderDto>>> getOrders();

}
