package com.fleet.common.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fleet.common.entity.Shift;

public interface ShiftRepository extends JpaRepository<Shift, UUID> {

    List<Shift> findByDriverId(UUID driverId);

}
