package com.fleet.common.entity;


import com.fleet.common.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "scheduled_start_time")
    private Instant scheduledStartTime;

    @Column(name = "scheduled_end_time")
    private Instant scheduledEndTime;


    @Column(name = "assigned_time")
    private Instant assignedTime;


    @Column(name = "actual_pickup_time")
    private Instant actualPickupTime;

    @Column(name = "actual_end_time")
    private Instant actualEndTime;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_visited_hub_id")
    private Hub lastVisitedHub;


    @Column(name = "admin_remark", columnDefinition = "TEXT")
    private String adminRemark;

    @Column(name = "driver_remark", columnDefinition = "TEXT")
    private String driverRemark;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}

