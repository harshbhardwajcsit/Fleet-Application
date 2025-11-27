package com.fleet.admin;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fleet.admin.requests.*;
import com.fleet.admin.services.*;
import com.fleet.common.dto.*;
import com.fleet.common.helpers.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Panel", description = "Admin APIs for Deliveries, Drivers, Vehicles, Hubs, Orders, Products and Terminals")
public class AdminController {

    private final AdminDeliveryService adminDeliveryService;
    private final FleetService fleetService;
    private final HubService hubService;
    private final OrderService orderService;
    private final ProductService productService;
    private final TerminalService terminalService;

    // ============================================================
    // DELIVERY MANAGEMENT
    // ============================================================

    @Operation(summary = "Create Delivery", description = "Creates a new delivery with assignment details.")
    @PostMapping("/deliveries")
    public ResponseEntity<ApiResponse<DeliveryDto>> createDelivery(
            @Valid @RequestBody CreateDeliveryRequest request) {
        return adminDeliveryService.createDelivery(request);
    }

    @Operation(summary = "Update Delivery", description = "Updates an existing delivery record.")
    @PutMapping("/deliveries")
    public ResponseEntity<ApiResponse<DeliveryDto>> updateDelivery(
            @Valid @RequestBody UpdateDeliveryRequest request) {
        return adminDeliveryService.updateDelivery(request);
    }

    @Operation(summary = "Get Delivery", description = "Fetch a delivery using its ID.")
    @GetMapping("/deliveries/{id}")
    public ResponseEntity<ApiResponse<DeliveryDto>> getDelivery(
            @Parameter(description = "Delivery UUID") @PathVariable UUID id) {
        return adminDeliveryService.getDelivery(id);
    }

    // ============================================================
    // DRIVER MANAGEMENT
    // ============================================================

    @Operation(summary = "Register Driver", description = "Add a new driver to the system.")
    @PostMapping("/drivers")
    public ResponseEntity<ApiResponse<DriverDto>> registerDriver(
            @Valid @RequestBody RegisterDriverRequest request) {
        return fleetService.registerDriver(request);
    }

    @Operation(summary = "Update Driver", description = "Update driver details.")
    @PutMapping("/drivers")
    public ResponseEntity<ApiResponse<DriverDto>> updateDriver(
            @Valid @RequestBody UpdateDriverRequest request) {
        return fleetService.updateDriver(request);
    }

    @Operation(summary = "List Drivers", description = "Get all drivers.")
    @GetMapping("/drivers")
    public ResponseEntity<ApiResponse<List<DriverDto>>> getDrivers() {
        return fleetService.getDrivers();
    }

    @Operation(summary = "Get Driver", description = "Get a driver by ID.")
    @GetMapping("/drivers/{id}")
    public ResponseEntity<ApiResponse<DriverDto>> getDriver(
            @Parameter(description = "Driver UUID") @PathVariable UUID id) {
        return fleetService.getDriver(id);
    }

    @Operation(summary = "Remove Driver", description = "Delete a driver by ID.")
    @DeleteMapping("/drivers/{id}")
    public ResponseEntity<ApiResponse<DriverDto>> removeDriver(
            @Parameter(description = "Driver UUID") @PathVariable UUID id) {
        return fleetService.removeDriver(id);
    }

    // ============================================================
    // VEHICLE MANAGEMENT
    // ============================================================

    @Operation(summary = "Register Vehicle", description = "Add a new vehicle to the fleet.")
    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse<VehicleDto>> registerVehicle(
            @Valid @RequestBody RegisterVehicleRequest request) {
        return fleetService.registerVehicle(request);
    }

    @Operation(summary = "List Vehicles", description = "Get all vehicles.")
    @GetMapping("/vehicles")
    public ResponseEntity<ApiResponse<List<VehicleDto>>> getVehicles() {
        return fleetService.getVehicles();
    }

    @Operation(summary = "Get Vehicle", description = "Retrieve a vehicle by ID.")
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<ApiResponse<VehicleDto>> getVehicle(
            @Parameter(description = "Vehicle UUID") @PathVariable UUID id) {
        return fleetService.getVehicle(id);
    }

    @Operation(summary = "Remove Vehicle", description = "Delete a vehicle record.")
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<ApiResponse<VehicleDto>> removeVehicle(
            @Parameter(description = "Vehicle UUID") @PathVariable UUID id) {
        return fleetService.removeVehicle(id);
    }

    // ============================================================
    // HUB MANAGEMENT
    // ============================================================

    @Operation(summary = "Add Hub", description = "Create a new hub in the system.")
    @PostMapping("/hubs")
    public ResponseEntity<ApiResponse<HubDto>> addHub(
            @Valid @RequestBody AddHubRequest request) {
        return hubService.addHub(request);
    }

    @Operation(summary = "List Hubs", description = "Get all hubs.")
    @GetMapping("/hubs")
    public ResponseEntity<ApiResponse<List<HubDto>>> getAllHubs() {
        return hubService.getAllHubs();
    }

    @Operation(summary = "Get Hub", description = "Retrieve a hub by ID.")
    @GetMapping("/hubs/{id}")
    public ResponseEntity<ApiResponse<HubDto>> getHubById(
            @Parameter(description = "Hub UUID") @PathVariable UUID id) {
        return hubService.getHubById(id);
    }

    @Operation(summary = "Update Hub", description = "Update hub attributes.")
    @PutMapping("/hubs/{id}")
    public ResponseEntity<ApiResponse<HubDto>> updateHub(
            @Parameter(description = "Hub UUID") @PathVariable UUID id,
            @Valid @RequestBody UpdateHubRequest request) {
        return hubService.updateHub(id, request);
    }

    // ============================================================
    // ORDER MANAGEMENT
    // ============================================================

    @Operation(summary = "Create Order", description = "Add a new order.")
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @Operation(summary = "Update Order", description = "Modify an existing order.")
    @PutMapping("/orders")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrder(
            @Valid @RequestBody UpdateOrderRequest request) {
        return orderService.updateOrder(request);
    }

    @Operation(summary = "Get Order", description = "Fetch an order by ID.")
    @GetMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(
            @Parameter(description = "Order UUID") @PathVariable UUID id) {
        return orderService.getOrder(id);
    }

    @Operation(summary = "List Orders", description = "Get all orders.")
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrders() {
        return orderService.getOrders();
    }

    // ============================================================
    // PRODUCT MANAGEMENT
    // ============================================================

    @Operation(summary = "Create Product", description = "Add a new product type.")
    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }

    @Operation(summary = "Update Product", description = "Modify product attributes.")
    @PutMapping("/products")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @Valid @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(request);
    }

    @Operation(summary = "Get Product", description = "Get product details by ID.")
    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(
            @Parameter(description = "Product UUID") @PathVariable UUID id) {
        return productService.getProduct(id);
    }

    // ============================================================
    // TERMINAL MANAGEMENT
    // ============================================================

    @Operation(summary = "Add Terminal", description = "Register a new terminal.")
    @PostMapping("/terminals")
    public ResponseEntity<ApiResponse<TerminalDto>> addTerminal(
            @Valid @RequestBody AddTerminalRequest request) {
        return terminalService.addTerminal(request);
    }

    @Operation(summary = "List Terminals", description = "Retrieve all terminals.")
    @GetMapping("/terminals")
    public ResponseEntity<ApiResponse<List<TerminalDto>>> getAllTerminals() {
        return terminalService.getAllTerminals();
    }

    @Operation(summary = "Get Terminal", description = "Fetch a terminal by ID.")
    @GetMapping("/terminals/{id}")
    public ResponseEntity<ApiResponse<TerminalDto>> getTerminalById(
            @Parameter(description = "Terminal UUID") @PathVariable UUID id) {
        return terminalService.getTerminalById(id);
    }

    @Operation(summary = "Update Terminal", description = "Update terminal details.")
    @PutMapping("/terminals/{id}")
    public ResponseEntity<ApiResponse<TerminalDto>> updateTerminal(
            @Parameter(description = "Terminal UUID") @PathVariable UUID id,
            @Valid @RequestBody UpdateTerminalRequest request) {
        return terminalService.updateTerminal(id, request);
    }

    // ============================================================
    // VEHICLE ASSIGNMENT MANAGEMENT
    // ============================================================

    @Operation(summary = "Assign a vehicle to a driver for Given date", description = "Assign Vehicle")
    @PostMapping("/assign-vehicle")
    public ResponseEntity<ApiResponse<VehicleAssignmentDto>> assignVehicle(
            @Valid @RequestBody CreateVehicleAssignmentRequest request) {
        return fleetService.assignVehicle(request);
    }
}
