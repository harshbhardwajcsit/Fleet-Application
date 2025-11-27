package com.fleet.common.mapper;

import com.fleet.admin.requests.CreateOrderRequest;
import com.fleet.admin.requests.UpdateOrderRequest;
import com.fleet.common.dto.OrderDto;
import com.fleet.common.entity.*;

import java.time.Instant;

public class OrderMapper {

    public static OrderDto toDto(Order order) {
        if (order == null)
            return null;

        OrderDto dto = new OrderDto();

        dto.setId(order.getId());

        dto.setPickupTerminal(TerminalMapper.toDto(order.getPickupTerminal()));

        dto.setDropoffTerminal(TerminalMapper.toDto(order.getDropoffTerminal()));


        dto.setProduct(ProductMapper.toDto(order.getProduct()));

        dto.setQuantity(order.getQuantity());
        dto.setUnit(order.getUnit());
        dto.setStatus(order.getStatus());
    
        dto.setCreatedBy(AdminMapper.toDto(order.getCreatedBy()));
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedBy(AdminMapper.toDto(order.getUpdatedBy()));
        dto.setUpdatedAt(order.getUpdatedAt());

        return dto;
    }

    public static Order toEntity(CreateOrderRequest req,
            Terminal pickup,
            Terminal dropoff,
            Product product,
            Admin createdBy) {

        Order order = new Order();

        order.setPickupTerminal(pickup);
        order.setDropoffTerminal(dropoff);
        order.setProduct(product);

        order.setQuantity(req.getQuantity());
        order.setUnit(req.getUnit());
        order.setStatus(req.getStatus());

        order.setCreatedBy(createdBy);
        order.setCreatedAt(Instant.now());

        return order;
    }

    public static void updateEntity(Order order,
            UpdateOrderRequest req,
            Terminal pickup,
            Terminal dropoff,
            Product product,
            Admin updatedBy) {

        if (pickup != null)
            order.setPickupTerminal(pickup);
        if (dropoff != null)
            order.setDropoffTerminal(dropoff);
    
        if (product != null)
            order.setProduct(product);

        if (req.getQuantity() != null)
            order.setQuantity(req.getQuantity());
        if (req.getUnit() != null)
            order.setUnit(req.getUnit());
        if (req.getStatus() != null)
            order.setStatus(req.getStatus());

        order.setUpdatedBy(updatedBy);
        order.setUpdatedAt(Instant.now());
    }
}
