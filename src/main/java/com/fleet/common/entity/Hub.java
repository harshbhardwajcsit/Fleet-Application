package com.fleet.common.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hubs")
@Data
public class Hub {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(nullable = false)
    private BigDecimal inventory;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
