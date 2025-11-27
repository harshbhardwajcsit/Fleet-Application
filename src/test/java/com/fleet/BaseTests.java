package com.fleet;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fleet.FleetOperation;

@SpringBootTest(classes = FleetOperation.class)
@ActiveProfiles("test")
class BaseTests {
    
}
