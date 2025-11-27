package com.fleet.admin.services.implementation;

import com.fleet.admin.requests.CreateOrderRequest;
import com.fleet.admin.requests.UpdateOrderRequest;
import com.fleet.admin.services.OrderService;
import com.fleet.common.constants.FleetConstants;
import com.fleet.common.constants.OrderConstants;
import com.fleet.common.constants.ProductConstants;
import com.fleet.common.constants.TerminalConstants;
import com.fleet.common.dto.OrderDto;
import com.fleet.common.entity.*;
import com.fleet.common.exceptions.AdminNotFoundException;
import com.fleet.common.exceptions.DriverNotFoundException;
import com.fleet.common.exceptions.ProductNotFoundException;
import com.fleet.common.exceptions.TerminalNotFoundException;
import com.fleet.common.exceptions.VehicleNotFoundException;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.common.helpers.ApiResponseBuilder;
import com.fleet.common.mapper.OrderMapper;
import com.fleet.common.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TerminalRepository terminalRepository;
    private final ProductRepository productRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final AdminRepository adminRepository;
    private final ApiResponseBuilder responseBuilder;

    @Override
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(CreateOrderRequest req) {

        Terminal pickup = terminalRepository.findById(req.getPickupTerminalId())
                .orElseThrow(() -> new TerminalNotFoundException(TerminalConstants.TERMINAL_NOT_FOUND));

        Terminal dropoff = terminalRepository.findById(req.getDropoffTerminalId())
                .orElseThrow(() -> new TerminalNotFoundException(TerminalConstants.TERMINAL_NOT_FOUND));

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ProductConstants.PRODUCT_NOT_FOUND));

     

        Admin createdBy = adminRepository.findById(req.getCreatedBy())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        Order order = OrderMapper.toEntity(req, pickup, dropoff, product, createdBy);
        orderRepository.save(order);

        return responseBuilder
                .success(HttpStatus.CREATED, OrderConstants.ORDER_CREATE_SUCCESS, OrderMapper.toDto(order));
    }

    @Override
    public ResponseEntity<ApiResponse<OrderDto>> updateOrder(UpdateOrderRequest req) {

        Order order = orderRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException(OrderConstants.ORDER_NOT_FOUND));

        Terminal pickup = terminalRepository.findById(req.getPickupTerminalId())
                    .orElseThrow(() -> new TerminalNotFoundException(TerminalConstants.TERMINAL_NOT_FOUND));
               

        Terminal dropoff = terminalRepository.findById(req.getDropoffTerminalId())
                    .orElseThrow(() -> new TerminalNotFoundException(TerminalConstants.TERMINAL_NOT_FOUND));
               
        Product product =  productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(ProductConstants.PRODUCT_NOT_FOUND));
               


        Admin updatedBy = adminRepository.findById(req.getUpdatedBy())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        OrderMapper.updateEntity(order, req, pickup, dropoff, product, updatedBy);

        orderRepository.save(order);

        return responseBuilder
                .success(HttpStatus.OK, OrderConstants.ORDER_UPDATE_SUCCESS, OrderMapper.toDto(order));
    }

    @Override
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(OrderConstants.ORDER_NOT_FOUND));

        return responseBuilder
                .success(HttpStatus.OK, OrderConstants.ORDER_FETCH_SUCCESS, OrderMapper.toDto(order));
    }

    @Override
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrders() {
        List<OrderDto> orders = orderRepository.findAll().stream()
                .map(OrderMapper::toDto)
                .toList();

        return responseBuilder
                .success(HttpStatus.OK, OrderConstants.ORDER_FETCH_SUCCESS, orders);
    }
}
