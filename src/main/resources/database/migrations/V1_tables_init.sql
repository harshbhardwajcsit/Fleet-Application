------------------------------------------------------------
-- ENABLE PGCRYPTO FOR gen_random_uuid()
------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

------------------------------------------------------------
-- ADMINS
------------------------------------------------------------
CREATE TABLE admins (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255),
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

------------------------------------------------------------
-- DRIVERS
------------------------------------------------------------
CREATE TABLE drivers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    license_number VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

------------------------------------------------------------
-- PRODUCTS
------------------------------------------------------------
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_flameable BOOLEAN,
    is_hazardous BOOLEAN,
    is_liquid BOOLEAN,
    is_frazile BOOLEAN,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

------------------------------------------------------------
-- TERMINALS
------------------------------------------------------------
CREATE TABLE terminals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    contact_number VARCHAR(255),
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    type VARCHAR(255) NOT NULL,
    inventory NUMERIC NOT NULL,
    unit VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

------------------------------------------------------------
-- VEHICLES
------------------------------------------------------------
CREATE TABLE vehicles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    model VARCHAR(255) NOT NULL,
    license_plate VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

------------------------------------------------------------
-- SHIFTS
------------------------------------------------------------
CREATE TABLE shifts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    driver_id UUID NOT NULL,
    actual_start_time TIME,
    actual_end_time TIME,
    total_working_hours NUMERIC,
    date DATE NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,

    CONSTRAINT fk_shifts_driver FOREIGN KEY (driver_id) REFERENCES drivers(id)
);

------------------------------------------------------------
-- ORDERS
------------------------------------------------------------
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pickup_terminal_id UUID NOT NULL,
    dropoff_terminal_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity NUMERIC NOT NULL,
    unit VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'CREATED',
    created_by_id UUID,
    updated_by_id UUID,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,

    CONSTRAINT fk_orders_pickup_terminal FOREIGN KEY (pickup_terminal_id) REFERENCES terminals(id),
    CONSTRAINT fk_orders_dropoff_terminal FOREIGN KEY (dropoff_terminal_id) REFERENCES terminals(id),
    CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_orders_created_by FOREIGN KEY (created_by_id) REFERENCES admins(id),
    CONSTRAINT fk_orders_updated_by FOREIGN KEY (updated_by_id) REFERENCES admins(id)
);

------------------------------------------------------------
-- HUBS
------------------------------------------------------------
CREATE TABLE hubs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    contact_number VARCHAR(255),
    inventory NUMERIC NOT NULL,
    unit VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

------------------------------------------------------------
-- DELIVERIES
------------------------------------------------------------
CREATE TABLE deliveries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    driver_id UUID NOT NULL,
    shift_id UUID NOT NULL,
    vehicle_id UUID,
    scheduled_start_time TIMESTAMP(6),
    scheduled_end_time TIMESTAMP(6),
    assigned_time TIMESTAMP(6),
    actual_pickup_time TIMESTAMP(6),
    actual_end_time TIMESTAMP(6),
    status VARCHAR(255) NOT NULL,
    last_visited_hub_id UUID,
    admin_remark TEXT,
    driver_remark TEXT,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,

    CONSTRAINT fk_deliveries_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_deliveries_driver FOREIGN KEY (driver_id) REFERENCES drivers(id),
    CONSTRAINT fk_deliveries_shift FOREIGN KEY (shift_id) REFERENCES shifts(id),
    CONSTRAINT fk_deliveries_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_deliveries_hub FOREIGN KEY (last_visited_hub_id) REFERENCES hubs(id)
);

------------------------------------------------------------
-- VEHICLE ASSIGNMENTS
------------------------------------------------------------
CREATE TABLE vehicle_assignments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vehicle_id UUID NOT NULL,
    driver_id UUID NOT NULL,
    shift_id UUID NOT NULL,
    assigned_by UUID NOT NULL,
    assigned_at TIMESTAMP(6) NOT NULL,
    assigned_date DATE NOT NULL,
    delivery_status VARCHAR(255) NOT NULL DEFAULT 'ASSIGNED',
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,

    CONSTRAINT fk_assignment_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_assignment_driver FOREIGN KEY (driver_id) REFERENCES drivers(id),
    CONSTRAINT fk_assignment_shift FOREIGN KEY (shift_id) REFERENCES shifts(id),
    CONSTRAINT fk_assignment_admin FOREIGN KEY (assigned_by) REFERENCES admins(id)
);
