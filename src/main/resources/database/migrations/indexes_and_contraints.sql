
-- enable extension for UUID generation (safe to run)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- VEHICLE ASSIGNMENTS: constraints (driver <-> vehicle per day)
ALTER TABLE IF EXISTS vehicle_assignments
    ADD CONSTRAINT IF NOT EXISTS unique_vehicle_per_day
        UNIQUE (vehicle_id, assigned_date);

ALTER TABLE IF EXISTS vehicle_assignments
    ADD CONSTRAINT IF NOT EXISTS unique_driver_per_day
        UNIQUE (driver_id, assigned_date);

-- Indexes for vehicle_assignments
CREATE INDEX IF NOT EXISTS idx_vehicle_assignments_assigned_at
    ON vehicle_assignments (assigned_at);

-- DELIVERIES

CREATE INDEX IF NOT EXISTS idx_deliveries_driver_id
    ON deliveries (driver_id);

CREATE INDEX IF NOT EXISTS idx_deliveries_vehicle_id
    ON deliveries (vehicle_id);

-- ORDERS
CREATE INDEX IF NOT EXISTS idx_orders_pickup_terminal
    ON orders (pickup_terminal_id);

CREATE INDEX IF NOT EXISTS idx_orders_dropoff_terminal
    ON orders (dropoff_terminal_id);

CREATE INDEX IF NOT EXISTS idx_orders_product
    ON orders (product_id);


-- VEHICLE_ASSIGNMENTS
CREATE INDEX IF NOT EXISTS idx_va_driver_id
    ON vehicle_assignments (driver_id);

CREATE INDEX IF NOT EXISTS idx_va_vehicle_id
    ON vehicle_assignments (vehicle_id);


