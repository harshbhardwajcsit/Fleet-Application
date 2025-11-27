package com.fleet.common.constants;

public class FleetConstants {
     // ------- VEHICLE -------
    public static final String VEHICLE_REGISTER_SUCCESS = "Vehicle registered successfully";
    public static final String VEHICLE_ALREADY_EXISTS = "Vehicle with the same license plate already exists.";
    public static final String VEHICLE_NOT_FOUND= "Vehicle not found";
    public static final String VEHICLE_FETCH_SUCCESS = "Vehicle fetched successfully";
    public static final String VEHICLES_FETCH_SUCCESS = "Vehicles fetched successfully";
    public static final String VEHICLE_REMOVE_SUCCESS = "Vehicle removed successfully";
    public static final String VEHICLES_LICENSE_PLATE_CONFLICT = "Another vehicle already uses this license plate.";

    // ------- DRIVER -------
    public static final String DRIVER_NOT_FOUND = "Driver not found";
    public static final String DRIVER_REGISTER_SUCCESS = "Driver registered successfully";
    public static final String DRIVER_UPDATE_SUCCESS = "Driver updated successfully";
    public static final String DRIVER_FETCH_SUCCESS = "Driver fetched successfully";
    public static final String DRIVERS_FETCH_SUCCESS = "Drivers fetched successfully";
    public static final String DRIVER_REMOVE_SUCCESS = "Driver removed successfully";    


    // ------- ASSIGNMENT -------
    public static final String ASSIGNMENT_CONFLICT = "Assignment conflict: driver or vehicle already assigned for the date";
    public static final String ASSIGNMENT_CREATE_SUCCESS = "Vehicle assigned to driver successfully";


    public static final String ADMIN_NOT_FOUND = "Admin not found";

    public static final String DRIVER_ASSIGNMENT_CONFLICT = "Driver already has a vehicle for date:";

    public static final String VEHICLE_ASSIGNMENT_CONFLICT = "Vehicle already assigned to another driver for date: ";

    public static final String NO_VEHICLE_ASSIGNED = "No vehicle assigned for today";
}
   
