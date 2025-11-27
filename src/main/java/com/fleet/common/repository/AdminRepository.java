package com.fleet.common.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fleet.common.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
}
