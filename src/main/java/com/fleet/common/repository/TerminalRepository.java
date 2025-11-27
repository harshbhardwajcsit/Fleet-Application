package com.fleet.common.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fleet.common.entity.Terminal;

public interface TerminalRepository extends JpaRepository<Terminal, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);

    boolean existsByLatitudeAndLongitude(Double latitude, Double longitude);

    boolean existsByLatitudeAndLongitudeAndIdNot(Double latitude, Double longitude, UUID id);
}
