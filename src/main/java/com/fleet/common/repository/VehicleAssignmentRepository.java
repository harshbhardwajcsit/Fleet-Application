package com.fleet.common.repository;

import com.fleet.common.entity.VehicleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface VehicleAssignmentRepository extends JpaRepository<VehicleAssignment, UUID> {

    boolean existsByDriverIdAndAssignedDate(UUID driverId, LocalDate assignedDate);

    boolean existsByVehicleIdAndAssignedDate(UUID vehicleId, LocalDate assignedDate);

    Optional<VehicleAssignment> findById(UUID id);

    Optional<VehicleAssignment> findByDriverIdAndAssignedDate(UUID driverId, LocalDate assignedDate);

    Optional<VehicleAssignment> findByVehicleIdAndAssignedDate(UUID vehicleId, LocalDate assignedDate);        
}
