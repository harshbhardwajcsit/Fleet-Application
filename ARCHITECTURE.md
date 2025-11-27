# ARCHITECTURE

## Purpose
High-level system design for Fleet Operations: ingesting device telemetry, real-time lookup, assignments, and historical analytics.

## Context (C4 - Level 1)
- **Users**: Admins (web UI), Drivers (mobile), Monitoring/Analytics.
- **System**: Fleet Platform (ingest, tracking, assignments, orders, hubs).
- **External**: Postgres (primary DB), Redis (real-time), Kafka (event bus), TSDB/ClickHouse (analytics).

## Containers (C4 - Level 2)
- **Gateway**: auth, basic validation, batching from device.
- **API Services** (Admin Service, Driver Service): CRUD, business logic, assignment workflows.
- **Tracking Producer**: receives telemetry, produces to Kafka.
- **Kafka Cluster**: durable buffer & partitioning.
- **Stream Processors** (Flink / Kafka Streams): enrich, dedupe, route to Redis & TSDB.
- **Redis Cluster**: latest geo-state / fast queries.
- **TSDB / Analytics**: time-series persistence and OLAP queries.
- **Postgres**: canonical relational data (orders, vehicles, drivers, assignments).
- **UI / Clients**: Admin dashboard, Driver app.

## Components (C4 - Level 3) — Driver Service (example)
- REST controller
- Assignment manager (transactional/unique constraints)
- Vehicle assignment repository
- Tracking integration (writes to Kafka or Redis)
- Metrics & Health

## Component Interactions
1. Device → Gateway (batch/gRPC) → Tracking Producer → Kafka.
2. Stream processor: Kafka → validate/enrich → Redis (latest) + TSDB (history).
3. Admin UI → API Service → Postgres (assignments/orders) → Update Redis / Kafka as needed.

## Why this architecture?
- **Decoupling**: Kafka isolates ingestion from consumers and allows replay.
- **Real-time**: Redis provides sub-10ms reads for nearest-driver queries.
- **Durability & Analytics**: TSDB for long-term analysis without loading primary DB.
- **Transactional Integrity**: Use Postgres with constraints and transactional checks for assignments.

## Diagrams
- Provide exported C4 diagrams (Context, Container, Component) in `/docs/images/`.
