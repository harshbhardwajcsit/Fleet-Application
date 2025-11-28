

-- Drop FOREIGN KEY constraint for shift_id
ALTER TABLE vehicle_assignments
    DROP CONSTRAINT IF EXISTS fk_assignment_shift;

-- Drop the shift_id column
ALTER TABLE vehicle_assignments
    DROP COLUMN IF EXISTS shift_id;

-- Drop the delivery_status column
ALTER TABLE vehicle_assignments
    DROP COLUMN IF EXISTS delivery_status;
