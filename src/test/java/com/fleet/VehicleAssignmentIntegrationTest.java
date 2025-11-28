package com.fleet;

import com.fleet.admin.requests.CreateVehicleAssignmentRequest;
import com.fleet.admin.services.implementation.FleetServiceImpl;
import com.fleet.common.entity.Admin;
import com.fleet.common.entity.Driver;
import com.fleet.common.entity.Vehicle;
import com.fleet.common.exceptions.AdminNotFoundException;
import com.fleet.common.exceptions.ConflictException;
import com.fleet.common.exceptions.DriverNotFoundException;
import com.fleet.common.exceptions.VehicleNotFoundException;
import com.fleet.common.repository.AdminRepository;
import com.fleet.common.repository.DriverRepository;
import com.fleet.common.repository.ShiftRepository;
import com.fleet.common.repository.VehicleAssignmentRepository;
import com.fleet.common.repository.VehicleRepository;
import com.fleet.driver.services.TrackingService;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.*;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleAssignmentIntegrationTest extends BaseTests {

    @Autowired private FleetServiceImpl assignmentService;
    @Autowired private DriverRepository driverRepository;
    @Autowired private ShiftRepository shiftRepository;
    @Autowired private VehicleAssignmentRepository assignmentRepository;
    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private JdbcTemplate jdbc;

    @MockBean private TrackingService trackingService;

    private final LocalDate TEST_DATE = LocalDate.of(2025, 11, 26);

    UUID driverId;
    UUID vehicleId;
    UUID adminId;
    UUID shiftId;

    @BeforeEach
    void setUp() {
        // Clean DB
        jdbc.execute("TRUNCATE TABLE vehicle_assignments RESTART IDENTITY CASCADE;");

        Driver d = new Driver();
        d.setId(UUID.randomUUID());
        d.setName("John Driver");
        d.setLicenseNumber("LIC-" + UUID.randomUUID().toString().substring(0, 6));
        d.setPhoneNumber("9998887777");
        d.setCreatedAt(Instant.now());
        d.setUpdatedAt(Instant.now());
        d = driverRepository.save(d);
        driverId = d.getId();

        Vehicle v = new Vehicle();
        v.setId(UUID.randomUUID());
        v.setModel("Volvo Truck");
        v.setLicensePlate("DL" + (int)(Math.random()*9999));
        v.setCreatedAt(Instant.now());
        v.setUpdatedAt(Instant.now());
        v = vehicleRepository.save(v);
        vehicleId = v.getId();

        Admin a = new Admin();
        a.setId(UUID.randomUUID());
        a.setName("Admin User");
        a.setEmail("admin" + UUID.randomUUID().toString().substring(0,6) + "@mail.com");
        a.setPhoneNumber("8080808080");
        a.setCreatedAt(Instant.now());
        a.setUpdatedAt(Instant.now());
        a = adminRepository.save(a);
        adminId = a.getId();
    }

    @AfterEach
    void clean() {
        assignmentRepository.deleteAll();
        shiftRepository.deleteAll();
        driverRepository.deleteAll();
        vehicleRepository.deleteAll();
        adminRepository.deleteAll();
    }

    private CreateVehicleAssignmentRequest req(UUID driver, UUID vehicle, UUID admin) {
        CreateVehicleAssignmentRequest r = new CreateVehicleAssignmentRequest();
        r.setDriverId(driver);
        r.setVehicleId(vehicle);
        r.setAssignedBy(admin);
        r.setAssignedDate(TEST_DATE);
        return r;
    }

    @Test
    void testAssignVehicleSuccess() {

        doNothing().when(trackingService)
                .removeFromAvailableVehicles(vehicleId);

        var response = assignmentService.assignVehicle(req(driverId, vehicleId, adminId));

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody().getData());

        var dto = response.getBody().getData();

        assertEquals(driverId, dto.getDriverId());
        assertEquals(vehicleId, dto.getVehicleId());

        assertTrue(assignmentRepository.existsByDriverIdAndAssignedDate(driverId, TEST_DATE));
        assertTrue(assignmentRepository.existsByVehicleIdAndAssignedDate(vehicleId, TEST_DATE));
    }
    @Test
    void testConcurrentAssignmentsSameVehicle() throws Exception {

        // Create two drivers & two admins for concurrency test
        Driver d1 = new Driver(); d1.setId(UUID.randomUUID()); d1.setName("Driver1"); d1.setLicenseNumber("L1"); d1.setPhoneNumber("111"); d1.setCreatedAt(Instant.now()); d1.setUpdatedAt(Instant.now());
        d1 = driverRepository.save(d1);

        Driver d2 = new Driver(); d2.setId(UUID.randomUUID()); d2.setName("Driver2"); d2.setLicenseNumber("L2"); d2.setPhoneNumber("222"); d2.setCreatedAt(Instant.now()); d2.setUpdatedAt(Instant.now());
        d2 = driverRepository.save(d2);

        Admin a1 = new Admin(); a1.setId(UUID.randomUUID()); a1.setName("Admin1"); a1.setEmail("admin1@mail.com"); a1.setPhoneNumber("101"); a1.setCreatedAt(Instant.now()); a1.setUpdatedAt(Instant.now());
        a1 = adminRepository.save(a1);

        Admin a2 = new Admin(); a2.setId(UUID.randomUUID()); a2.setName("Admin2"); a2.setEmail("admin2@mail.com"); a2.setPhoneNumber("202"); a2.setCreatedAt(Instant.now()); a2.setUpdatedAt(Instant.now());
        a2 = adminRepository.save(a2);

        var req1 = req(d1.getId(), vehicleId, a1.getId());
        var req2 = req(d2.getId(), vehicleId, a2.getId());

        ExecutorService exec = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        Callable<Object> c1 = () -> { latch.await(); return assignmentService.assignVehicle(req1); };
        Callable<Object> c2 = () -> { latch.await(); return assignmentService.assignVehicle(req2); };

        Future<?> f1 = exec.submit(c1);
        Future<?> f2 = exec.submit(c2);

        latch.countDown();

        Object r1 = null, r2 = null;
        Exception e1 = null, e2 = null;

        try { r1 = f1.get(); } catch (Exception ex) { e1 = (Exception) ex.getCause(); }
        try { r2 = f2.get(); } catch (Exception ex) { e2 = (Exception) ex.getCause(); }

        exec.shutdown();

        // One must succeed
        assertTrue((r1 instanceof ResponseEntity) || (r2 instanceof ResponseEntity));
        // One must fail
        assertTrue((e1 instanceof ConflictException) || (e2 instanceof ConflictException));
    }

    @Test
    void testDriverAlreadyAssignedConflict() {

        Vehicle v2 = newVehicle("ExtraTruck");

        // Assign once
        assignmentService.assignVehicle(req(driverId, vehicleId, adminId));

        // Assign again same driver same date
        assertThrows(ConflictException.class,
                () -> assignmentService.assignVehicle(req(driverId, v2.getId(), adminId)));
    }

    private Vehicle newVehicle(String model) {
        Vehicle v = new Vehicle();
        v.setId(UUID.randomUUID());
        v.setModel(model);
        v.setLicensePlate("PL-" + UUID.randomUUID().toString().substring(0, 6));
        v.setCreatedAt(Instant.now());
        v.setUpdatedAt(Instant.now());
        return vehicleRepository.save(v);
    }

    @Test
    void testMissingEntitiesThrowsNotFound() {

        UUID INVALID = UUID.randomUUID();

        // VEHICLE missing
        assertThrows(VehicleNotFoundException.class,
                () -> assignmentService.assignVehicle(req(driverId, INVALID, adminId)));

        // DRIVER missing
        assertThrows(DriverNotFoundException.class,
                () -> assignmentService.assignVehicle(req(INVALID, vehicleId, adminId)));

        // ADMIN missing
        assertThrows(AdminNotFoundException.class,
                () -> assignmentService.assignVehicle(req(driverId, vehicleId, INVALID)));
    }
}

