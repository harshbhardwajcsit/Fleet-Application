package com.fleet.admin.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.fleet.admin.requests.AddHubRequest;
import com.fleet.admin.requests.UpdateHubRequest;
import com.fleet.common.dto.HubDto;
import com.fleet.common.helpers.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Hubs", description = "Hub Management APIs")
public interface HubService {
   @Operation(
        summary = "Create a new hub",
        description = "Adds a new hub with inventory, location, and type information."
    )
    ResponseEntity<ApiResponse<HubDto>> addHub(AddHubRequest request);

    @Operation(
        summary = "Get all hubs",
        description = "Returns a list of all hubs in the system."
    )
    ResponseEntity<ApiResponse<List<HubDto>>> getAllHubs();

    @Operation(
        summary = "Get a hub by ID",
        description = "Fetch detailed information about a single hub using its UUID."
    )
    ResponseEntity<ApiResponse<HubDto>> getHubById(UUID id);

    @Operation(
        summary = "Update hub details",
        description = "Updates hub name, location, inventory, type, or any editable fields."
    )
    ResponseEntity<ApiResponse<HubDto>> updateHub(UUID id, UpdateHubRequest request);

}
