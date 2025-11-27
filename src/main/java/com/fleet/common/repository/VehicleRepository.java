package com.fleet.common.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fleet.common.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    public boolean existsByLicensePlate(String licensePlate);
}
