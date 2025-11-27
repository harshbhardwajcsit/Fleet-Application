package com.fleet.common.redis;


public class RedisKeys {

    // Hash: Stores driver location
    public static final String DRIVER_LOCATION = "driver:location:"; 
    // Set: Drivers with active shift
    public static final String ACTIVE_DRIVERS = "drivers:active";
    // Set: Drivers currently on delivery
    public static final String DRIVERS_ON_DELIVERY = "drivers:on_delivery";
    // Set: Vehicles available (idle)
    public static final String AVAILABLE_VEHICLES = "vehicles:available";
}

