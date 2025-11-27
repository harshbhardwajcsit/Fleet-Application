package com.fleet.common.mapper;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fleet.admin.requests.AddHubRequest;
import com.fleet.admin.requests.UpdateHubRequest;
import com.fleet.common.dto.HubDto;
import com.fleet.common.entity.Hub;

@Component
public class HubMapper {
    public static Hub toEntity(AddHubRequest req) {
        Hub hub = new Hub();
        hub.setName(req.getName());
        hub.setLocation(req.getLocation());
        hub.setContactNumber(req.getContactNumber());
        hub.setInventory(req.getInventory());
        hub.setUnit(req.getUnit());
        hub.setLatitude(req.getLatitude());
        hub.setLongitude(req.getLongitude());
        hub.setCreatedAt(Instant.now());
        hub.setUpdatedAt(Instant.now());
        return hub;
    }

    public static HubDto toDto(Hub hub) {
        HubDto dto = new HubDto();
        dto.setId(hub.getId());
        dto.setName(hub.getName());
        dto.setLocation(hub.getLocation());
        dto.setContactNumber(hub.getContactNumber());
        dto.setInventory(hub.getInventory());
        dto.setUnit(hub.getUnit());
        dto.setLatitude(hub.getLatitude());
        dto.setLongitude(hub.getLongitude());
        dto.setCreatedAt(hub.getCreatedAt());
        dto.setUpdatedAt(hub.getUpdatedAt());
        return dto;
    }

    public static Hub forUpdated(Hub hub, UpdateHubRequest request){
        if(request.getName()!=null) hub.setName(request.getName());
        if(request.getLocation()!=null) hub.setLocation(request.getLocation());
        if(request.getContactNumber()!=null) hub.setContactNumber(request.getContactNumber());
        if(request.getInventory()!=null) hub.setInventory(request.getInventory());
        if(request.getUnit()!=null) hub.setUnit(request.getUnit());
        if(request.getLatitude()!=null) hub.setLatitude(request.getLatitude());
        if(request.getLongitude()!=null) hub.setLongitude(request.getLongitude());
        hub.setUpdatedAt(Instant.now());
        return hub;

    }
}
