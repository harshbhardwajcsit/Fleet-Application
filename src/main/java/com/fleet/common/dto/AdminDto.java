package com.fleet.common.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
public class AdminDto {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private Instant createdAt;
    private Instant updatedAt;
}
