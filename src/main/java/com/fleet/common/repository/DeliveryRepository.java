package com.fleet.common.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fleet.common.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    List<Delivery> findByDriverId(UUID driverId);


}
