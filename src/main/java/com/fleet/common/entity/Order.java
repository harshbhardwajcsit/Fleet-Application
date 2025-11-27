package com.fleet.common.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.hibernate.type.descriptor.jdbc.InstantAsTimestampJdbcType;

import com.fleet.common.enums.OrderStatus;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_terminal_id", nullable = false)
    private Terminal pickupTerminal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dropoff_terminal_id", nullable = false)
    private Terminal dropoffTerminal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private String unit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private Admin createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id")
    private Admin updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
