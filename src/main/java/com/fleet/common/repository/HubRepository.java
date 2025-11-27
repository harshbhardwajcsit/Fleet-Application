package com.fleet.common.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fleet.common.entity.Hub;

public interface HubRepository extends JpaRepository<Hub, UUID> {  

    public boolean existsByNameIgnoreCase(String name);

    public boolean existsByLatitudeAndLongitude(Double latitude, Double longitude);

    public boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);

    public boolean existsByLatitudeAndLongitudeAndIdNot(Double latitude, Double longitude, UUID id);
} 
