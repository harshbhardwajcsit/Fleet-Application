package com.fleet.admin.services.implementation;

import java.util.List;
import java.util.UUID;

import org.apache.catalina.mapper.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fleet.admin.requests.AddHubRequest;
import com.fleet.admin.requests.UpdateHubRequest;
import com.fleet.admin.services.HubService;
import com.fleet.common.constants.HubConstants;
import com.fleet.common.dto.HubDto;
import com.fleet.common.entity.Hub;
import com.fleet.common.exceptions.HubNotFoundExecption;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.common.helpers.ApiResponseBuilder;
import com.fleet.common.mapper.HubMapper;
import com.fleet.common.repository.HubRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;
    private final ApiResponseBuilder responseBuilder;

    @Override
    public ResponseEntity<ApiResponse<HubDto>> addHub(AddHubRequest request) {

        if (hubRepository.existsByNameIgnoreCase(request.getName())) {
            return responseBuilder.failure(HttpStatus.CONFLICT, HubConstants.HUB_NAME_EXISTS);
        }

        if (hubRepository.existsByLatitudeAndLongitude(request.getLatitude(), request.getLongitude())) {
            return responseBuilder.failure(HttpStatus.CONFLICT, HubConstants.HUB_LOCATION_EXISTS);
        }

        Hub hubEntity = HubMapper.toEntity(request);
        hubEntity = hubRepository.save(hubEntity);

        return responseBuilder.success(HttpStatus.CREATED, HubConstants.HUB_ADDED_SUCCESS,
                HubMapper.toDto(hubEntity));
    }

    @Override
    public ResponseEntity<ApiResponse<List<HubDto>>> getAllHubs() {

        List<HubDto> result = hubRepository.findAll()
                .stream()
                .map(HubMapper::toDto)
                .toList();

        return responseBuilder.success(HttpStatus.OK, HubConstants.LIST_OF_HUBS, result);
    }

    @Override
    public ResponseEntity<ApiResponse<HubDto>> getHubById(UUID id) {

        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new HubNotFoundExecption(HubConstants.HUB_NOT_FOUND));

        return responseBuilder.success(HttpStatus.OK, HubConstants.HUB_FETCH_SUCCESS, HubMapper.toDto(hub));
    }

    @Override
    public ResponseEntity<ApiResponse<HubDto>> updateHub(UUID id, UpdateHubRequest request) {

        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new HubNotFoundExecption(HubConstants.HUB_NOT_FOUND));

        // Validate uniqueness only if new values are provided
        if (request.getName() != null &&
                hubRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            return responseBuilder.failure(HttpStatus.CONFLICT, HubConstants.HUB_NAME_CONFLICT);
        }

        if (request.getLatitude() != null && request.getLongitude() != null &&
                hubRepository.existsByLatitudeAndLongitudeAndIdNot(request.getLatitude(), request.getLongitude(), id)) {
            return responseBuilder.failure(HttpStatus.CONFLICT, HubConstants.HUB_LOCATION_CONFLICT);
        }

        HubMapper.forUpdated(hub, request);

        hub = hubRepository.save(hub);

        return responseBuilder.success(HttpStatus.OK, HubConstants.HUB_UPDATED_SUCCESS, HubMapper.toDto(hub));
    }
}
