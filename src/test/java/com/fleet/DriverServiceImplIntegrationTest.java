package com.fleet;

import com.fleet.common.constants.FleetConstants;
import com.fleet.common.constants.ShiftConstants;
import com.fleet.common.dto.ShiftDto;
import com.fleet.common.entity.*;
import com.fleet.common.exceptions.DriverNotFoundException;
import com.fleet.common.exceptions.ShiftNotFoundException;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.common.repository.AdminRepository;
import com.fleet.common.repository.DeliveryRepository;
import com.fleet.common.repository.DriverRepository;
import com.fleet.common.repository.ShiftRepository;
import com.fleet.common.repository.VehicleAssignmentRepository;
import com.fleet.common.repository.VehicleRepository;
import com.fleet.driver.services.TrackingService;
import com.fleet.driver.services.implementation.DriverServiceImpl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DriverServiceImplIntegrationTest extends BaseTests {

    @Autowired
    private DriverServiceImpl driverService;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private VehicleAssignmentRepository assignmentRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private AdminRepository adminRepository;

    @MockBean
    private TrackingService trackingService;

    UUID driverId;
    UUID shiftId;
    UUID vehicleId;
    UUID adminId;

    @BeforeEach
    void setup() {
        assignmentRepository.deleteAll();
        shiftRepository.deleteAll();
        deliveryRepository.deleteAll();
        driverRepository.deleteAll();
        vehicleRepository.deleteAll();
        adminRepository.deleteAll();

        Driver d = new Driver();
        d.setName("John Driver");
        d.setLicenseNumber("LIC1088");
        d.setPhoneNumber("888282809");
        d.setCreatedAt(Instant.now());
        d.setUpdatedAt(Instant.now());
        d = driverRepository.save(d);
        driverId = d.getId();

        Shift shift = new Shift();
        shift.setStartTime(LocalTime.of(9, 0, 0)); // 09:00:00
        shift.setEndTime(LocalTime.of(18, 0, 0)); // 18:00:00

        shift.setDriver(d);

        shift.setDate(LocalDate.of(2025, 11, 13)); // 2025-11-13

        shift.setCreatedAt(
                Instant.now());
        shift.setUpdatedAt(
                Instant.now());

        shift = shiftRepository.save(shift);
        shiftId = shift.getId();

        Vehicle v = new Vehicle();
        v.setModel("Test Truck");
        v.setLicensePlate("DL7CAF1234");
        v.setCreatedAt(Instant.now());
        v.setUpdatedAt(Instant.now());
        v = vehicleRepository.save(v);
        
        vehicleId = v.getId();

        Admin admin = new Admin();
        admin.setName("Admin");
        admin.setEmail("29admin@email.com");
        admin.setPhoneNumber("8107276565");
        admin.setCreatedAt(Instant.now());
        admin.setUpdatedAt(Instant.now());
        admin = adminRepository.save(admin);
        adminId = admin.getId();
    }

    @AfterEach()
    void doVacumn(){
        assignmentRepository.deleteAll();
        shiftRepository.deleteAll();
        deliveryRepository.deleteAll();
        driverRepository.deleteAll();
        vehicleRepository.deleteAll();
        adminRepository.deleteAll();

    }

    private void assignVehicleToday() {
        VehicleAssignment a = new VehicleAssignment();
        a.setDriver(driverRepository.findById(driverId).get());
        a.setVehicle(vehicleRepository.findById(vehicleId).get());
        a.setAssignedDate(LocalDate.now(ZoneOffset.UTC));
        a.setAssignedAt(Instant.now());
        a.setAssignedBy(adminRepository.findById(adminId).get());
        a.setCreatedAt(Instant.now());
        a.setUpdatedAt(Instant.now());
        assignmentRepository.save(a);
    }

    @Test
    void startShiftDriverNotFound() {
        assertThrows(DriverNotFoundException.class,
                () -> driverService.startShift(UUID.randomUUID(), shiftId));
    }

    @Test
    void startShiftAndShiftNotFound() {
        assertThrows(ShiftNotFoundException.class,
                () -> driverService.startShift(driverId, UUID.randomUUID()));
    }

    @Test
    void startShiftNoVehicleAssignedShouldreturnsBadRequest() {

        ResponseEntity<ApiResponse<ShiftDto>> res = driverService.startShift(driverId, shiftId);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(FleetConstants.NO_VEHICLE_ASSIGNED, res.getBody().getMessage());
    }

    @Test
    void startShiftWhenShiftAlreadyStarted() {
        assignVehicleToday();

        Shift s = shiftRepository.findById(shiftId).get();
        s.setActualStartTime(LocalTime.now());
        shiftRepository.save(s);

        ResponseEntity<ApiResponse<ShiftDto>> res = driverService.startShift(driverId, shiftId);

        assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
        assertEquals(ShiftConstants.SHIFT_ALREADY_STARTED, res.getBody().getMessage());
    }

    @Test
    void startShiftSuccess() {
        assignVehicleToday();

        ResponseEntity<ApiResponse<ShiftDto>> res = driverService.startShift(driverId, shiftId);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        verify(trackingService, times(1)).addToActiveDrivers(driverId);

        Shift updated = shiftRepository.findById(shiftId).get();
        assertNotNull(updated.getActualStartTime());
    }

    @Test
    void endShiftWhenDriverNotFound() {
        assertThrows(DriverNotFoundException.class,
                () -> driverService.endShift(UUID.randomUUID(), shiftId));
    }

    @Test
    void endShiftWithShiftNotFound() {
        assertThrows(ShiftNotFoundException.class,
                () -> driverService.endShift(driverId, UUID.randomUUID()));
    }

    @Test
    void endShiftWhenShiftNotStarted() {

        ResponseEntity<ApiResponse<ShiftDto>> res = driverService.endShift(driverId, shiftId);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals(ShiftConstants.SHIFT_NOT_STARTED, res.getBody().getMessage());
    }

    @Test
    void endShiftWhenShiftAlreadyEnded() {
        assignVehicleToday();

        Shift s = shiftRepository.findById(shiftId).get();
        s.setActualStartTime(LocalTime.now());
        s.setActualEndTime(LocalTime.now());
        shiftRepository.save(s);

        ResponseEntity<ApiResponse<ShiftDto>> res = driverService.endShift(driverId, shiftId);

        assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
        assertEquals(ShiftConstants.SHIFT_ALREADY_ENDED, res.getBody().getMessage());
    }

    @Test
    void endShiftSuccessWithVehicleAssigned() {

        assignVehicleToday();

        Shift s = shiftRepository.findById(shiftId).get();
        s.setActualStartTime(LocalTime.now());
        shiftRepository.save(s);

        ResponseEntity<ApiResponse<ShiftDto>> res = driverService.endShift(driverId, shiftId);

        assertEquals(HttpStatus.OK, res.getStatusCode());

        verify(trackingService, times(1)).removeDriverFromAll(driverId);

        VehicleAssignment assign = assignmentRepository
                .findByDriverIdAndAssignedDate(
                        driverId,
                        LocalDate.now(ZoneOffset.UTC))
                .get();

        verify(trackingService, times(1)).addToAvailableVehicles(assign.getVehicle().getId());
    }

    // ========================
    // VIEW SHIFTS
    // ========================

    @Test
    void viewShiftsReturnsList() {
        List<ShiftDto> result = driverService.viewShifts(driverId).getBody().getData();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(shiftId, result.get(0).getId());
    }
}
