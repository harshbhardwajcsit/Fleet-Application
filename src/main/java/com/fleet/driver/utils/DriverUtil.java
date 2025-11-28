package com.fleet.driver.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;


public class DriverUtil {

    public static BigDecimal getTotalWorkingHours(LocalTime actualStartTime, LocalTime actualEndTime) {
        Duration duration = Duration.between(
                actualStartTime,
                actualEndTime);

        return BigDecimal.valueOf(duration.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }

}
