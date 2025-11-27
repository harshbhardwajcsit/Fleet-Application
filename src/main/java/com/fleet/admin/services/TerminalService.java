package com.fleet.admin.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.fleet.admin.requests.AddTerminalRequest;
import com.fleet.admin.requests.UpdateTerminalRequest;
import com.fleet.common.dto.TerminalDto;
import com.fleet.common.helpers.ApiResponse;

public interface TerminalService {

    ResponseEntity<ApiResponse<TerminalDto>> addTerminal(AddTerminalRequest request);

    ResponseEntity<ApiResponse<List<TerminalDto>>> getAllTerminals();

    ResponseEntity<ApiResponse<TerminalDto>> getTerminalById(UUID id);

    ResponseEntity<ApiResponse<TerminalDto>> updateTerminal(UUID id, UpdateTerminalRequest request);
}

    