package com.fleet.admin.services;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.fleet.admin.requests.CreateVehicleAssignmentRequest;
import com.fleet.admin.requests.RegisterDriverRequest;
import com.fleet.admin.requests.RegisterVehicleRequest;
import com.fleet.admin.requests.UpdateDriverRequest;
import com.fleet.common.dto.DriverDto;
import com.fleet.common.dto.VehicleAssignmentDto;
import com.fleet.common.dto.VehicleDto;
import com.fleet.common.helpers.ApiResponse;


public interface FleetService {

ResponseEntity<ApiResponse<VehicleDto>> registerVehicle(RegisterVehicleRequest request);  

ResponseEntity<ApiResponse<List<VehicleDto>>> getVehicles();    

ResponseEntity<ApiResponse<VehicleDto>> getVehicle(UUID id);

ResponseEntity<ApiResponse<VehicleDto>> removeVehicle(UUID id);

ResponseEntity<ApiResponse<DriverDto>> registerDriver(RegisterDriverRequest request);  

ResponseEntity<ApiResponse<DriverDto>> updateDriver(UpdateDriverRequest request);  

ResponseEntity<ApiResponse<List<DriverDto>>> getDrivers();

ResponseEntity<ApiResponse<DriverDto>> getDriver(UUID id);

ResponseEntity<ApiResponse<DriverDto>> removeDriver(UUID id);

ResponseEntity<ApiResponse<VehicleAssignmentDto>> assignVehicle(CreateVehicleAssignmentRequest request);

}
