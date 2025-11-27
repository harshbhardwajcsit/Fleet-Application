package com.fleet.common.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fleet.common.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
