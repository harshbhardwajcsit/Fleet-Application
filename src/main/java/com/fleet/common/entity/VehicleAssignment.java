package com.fleet.common.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "vehicle_assignments", uniqueConstraints = {
        @UniqueConstraint(name = "unique_vehicle_per_day", columnNames = { "vehicle_id", "assigned_date" }),
        @UniqueConstraint(name = "unique_driver_per_day", columnNames = { "driver_id", "assigned_date" })
})
@Data
public class VehicleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by", nullable = false)
    private Admin assignedBy;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // Utility method to set assignedDate automatically in UTC
    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
        this.assignedDate = assignedAt.atZone(ZoneOffset.UTC).toLocalDate();
    }
}
