
## Database overview

- **admins** (id PK, name, email, phone, created_at, updated_at)
- **drivers** (id PK, name, license_number UNIQUE, phone, created_at, updated_at)
- **vehicles** (id PK, model, license_plate UNIQUE, created_at, updated_at)
- **orders** (id PK, pickup_terminal FK, dropoff_terminal FK, product FK, qty, unit, status, created_by FK, updated_by FK, created_at, updated_at)
- **deliveries** (id PK, order FK, driver FK, vehicle FK, shift FK, scheduled_start, scheduled_end, actual_pickup, actual_end, status, admin_remark, driver_remark, created_at, updated_at)
- **shifts** (id PK, driver FK, date, start_time, end_time, actual_start_time, actual_end_time, total_hours, created_at, updated_at)
- **vehicle_assignments** (id PK, vehicle FK, driver FK, assigned_by FK, assigned_at, assigned_date, delivery_status, created_at, updated_at)
- **hubs, terminals, products** follow simple relational models.

## Design Decisions
- **VehicleAssignment uniqueness**:
  - `UNIQUE(vehicle_id, assigned_date)`
  - `UNIQUE(driver_id, assigned_date)`
  - Rationale: Enforce one vehicle per day and one driver per day.
    
- **Use UUID PKs** for global uniqueness and easier sharding/merging across services.

-  Timestamps as `timestamp  (Instant)** to avoid timezone ambiguity; store assigned_date as `date` (UTC).
  
- **Indexes**:
  - Please refer `indexes_and_constraints.sql` for available indexes.

## Sharding strategy for 10k+ vehicles
- Keep relational canonical state in Postgres and shard vertically if necessary:
  - **Logical sharding** by tenant/hub or by vehicle hash when scale requires.
  - Prefer read-replicas for query scaling; write-scaling via domain separation (separate DB for analytics vs transactional).
