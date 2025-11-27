package com.fleet.common.mapper;


import java.time.Instant;
import org.springframework.stereotype.Component;

import com.fleet.admin.requests.AddTerminalRequest;
import com.fleet.admin.requests.UpdateTerminalRequest;
import com.fleet.common.dto.TerminalDto;
import com.fleet.common.entity.Terminal;

@Component
public class TerminalMapper {

    public static Terminal toEntity(AddTerminalRequest req) {
        Terminal t = new Terminal();
        t.setName(req.getName());
        t.setLocation(req.getLocation());
        t.setContactNumber(req.getContactNumber());
        t.setLatitude(req.getLatitude());
        t.setLongitude(req.getLongitude());
        t.setType(req.getType());
        t.setInventory(req.getInventory());
        t.setUnit(req.getUnit());
        t.setCreatedAt(Instant.now());
        t.setUpdatedAt(Instant.now());
        return t;
    }

    public static TerminalDto toDto(Terminal t) {
        TerminalDto dto = new TerminalDto();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setLocation(t.getLocation());
        dto.setContactNumber(t.getContactNumber());
        dto.setLatitude(t.getLatitude());
        dto.setLongitude(t.getLongitude());
        dto.setType(t.getType());
        dto.setInventory(t.getInventory());
        dto.setUnit(t.getUnit());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setUpdatedAt(t.getUpdatedAt());
        return dto;
    }

    public static void updateEntity(Terminal t, UpdateTerminalRequest req) {

        if (req.getName() != null) t.setName(req.getName());
        if (req.getLocation() != null) t.setLocation(req.getLocation());
        if (req.getContactNumber() != null) t.setContactNumber(req.getContactNumber());
        if (req.getLatitude() != null) t.setLatitude(req.getLatitude());
        if (req.getLongitude() != null) t.setLongitude(req.getLongitude());
        if (req.getType() != null) t.setType(req.getType());
        if (req.getInventory() != null) t.setInventory(req.getInventory());
        if (req.getUnit() != null) t.setUnit(req.getUnit());
        t.setUpdatedAt(Instant.now());
    }
}
