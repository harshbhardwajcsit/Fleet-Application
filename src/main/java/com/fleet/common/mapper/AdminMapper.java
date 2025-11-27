package com.fleet.common.mapper;


import com.fleet.common.dto.AdminDto;
import com.fleet.common.entity.Admin;

public class AdminMapper {

    private AdminMapper() {}

    public static AdminDto toDto(Admin admin) {
        if (admin == null) return null;

        AdminDto dto = new AdminDto();
        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setEmail(admin.getEmail());
        dto.setPhoneNumber(admin.getPhoneNumber());
        dto.setCreatedAt(admin.getCreatedAt());
        dto.setUpdatedAt(admin.getUpdatedAt());

        return dto;
    }

    public static Admin toEntity(AdminDto dto) {
        if (dto == null) return null;

        Admin admin = new Admin();
        admin.setId(dto.getId());
        admin.setName(dto.getName());
        admin.setEmail(dto.getEmail());
        admin.setPhoneNumber(dto.getPhoneNumber());
        admin.setCreatedAt(dto.getCreatedAt());
        admin.setUpdatedAt(dto.getUpdatedAt());

        return admin;
    }
}

