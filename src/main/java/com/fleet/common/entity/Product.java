package com.fleet.common.entity;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_flameable")
    private Boolean isFlameable;

    @Column(name = "is_hazardous")
    private Boolean isHazardous;

    @Column(name = "is_liquid")
    private Boolean isLiquid;

    @Column(name = "is_frazile")
    private Boolean isFrazile; // fragile?

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
