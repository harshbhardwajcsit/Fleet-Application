package com.fleet.admin.services.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fleet.admin.requests.AddTerminalRequest;
import com.fleet.admin.requests.UpdateTerminalRequest;
import com.fleet.admin.services.TerminalService;
import com.fleet.common.constants.TerminalConstants;
import com.fleet.common.dto.TerminalDto;
import com.fleet.common.entity.Terminal;
import com.fleet.common.exceptions.TerminalNotFoundException;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.common.helpers.ApiResponseBuilder;
import com.fleet.common.mapper.TerminalMapper;
import com.fleet.common.repository.TerminalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TerminalServiceImpl implements TerminalService {

    private final TerminalRepository terminalRepository;
    private final ApiResponseBuilder responseBuilder;

    @Override
    public ResponseEntity<ApiResponse<TerminalDto>> addTerminal(AddTerminalRequest request) {

        if (terminalRepository.existsByNameIgnoreCase(request.getName())) {
            return responseBuilder.failure(HttpStatus.CONFLICT, TerminalConstants.NAME_EXISTS);
        }

        if (terminalRepository.existsByLatitudeAndLongitude(request.getLatitude(), request.getLongitude())) {
            return responseBuilder.failure(HttpStatus.CONFLICT, TerminalConstants.LOCATION_EXISTS);
        }

        Terminal terminal = TerminalMapper.toEntity(request);
        terminal = terminalRepository.save(terminal);

        return responseBuilder.success(HttpStatus.CREATED, TerminalConstants.CREATED_SUCCESS,
                TerminalMapper.toDto(terminal));
    }

    @Override
    public ResponseEntity<ApiResponse<List<TerminalDto>>> getAllTerminals() {
        List<TerminalDto> dtos = terminalRepository.findAll()
                .stream()
                .map(TerminalMapper::toDto)
                .toList();

        return responseBuilder.success(HttpStatus.OK, TerminalConstants.LIST_OF_TERMINALS, dtos);
    }

    @Override
    public ResponseEntity<ApiResponse<TerminalDto>> getTerminalById(UUID id) {

        Terminal terminal = terminalRepository.findById(id)
                .orElseThrow(() -> new TerminalNotFoundException(TerminalConstants.TERMINAL_NOT_FOUND));

        return responseBuilder.success(HttpStatus.OK, TerminalConstants.FETCH_SUCCESS, TerminalMapper.toDto(terminal));
    }

    @Override
    public ResponseEntity<ApiResponse<TerminalDto>> updateTerminal(UUID id, UpdateTerminalRequest req) {

        Terminal terminal = terminalRepository.findById(id)
                .orElseThrow(() -> new TerminalNotFoundException(TerminalConstants.TERMINAL_NOT_FOUND));

        if (req.getName() != null &&
            terminalRepository.existsByNameIgnoreCaseAndIdNot(req.getName(), id)) {
            return responseBuilder.failure(HttpStatus.CONFLICT, TerminalConstants.TERMINAL_NAME_CONFLICT);
        }

        if (req.getLatitude() != null && req.getLongitude() != null &&
            terminalRepository.existsByLatitudeAndLongitudeAndIdNot(req.getLatitude(), req.getLongitude(), id)) {
            return responseBuilder.failure(HttpStatus.CONFLICT, TerminalConstants.TERMINAL_LOCATION_CONFLICT);
        }

        TerminalMapper.updateEntity(terminal, req);
        terminal = terminalRepository.save(terminal);

        return responseBuilder.success(HttpStatus.OK, TerminalConstants.UPDATED_SUCCESS,
                TerminalMapper.toDto(terminal));
    }
}

