package com.fleet.common.entity;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "drivers")
@Data
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

   @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private List<Shift> shifts;
}
