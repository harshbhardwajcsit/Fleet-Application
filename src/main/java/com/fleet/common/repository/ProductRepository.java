package com.fleet.common.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fleet.common.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
