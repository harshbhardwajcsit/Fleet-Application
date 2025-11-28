package com.fleet;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest(classes = FleetOperation.class)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class BaseTests {
    
}
