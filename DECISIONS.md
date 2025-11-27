
## Key Decisions & Justifications

1. **Event-driven telemetry ingestion (Kafka)**  
   - Reason: decouples devices from downstream consumers; supports replay, buffering, and smooth scaling.

2. **Redis for latest state**  
   - Reason: geo queries require low latency and efficient radius search; Redis GEO is purpose-built.

3. **TSDB for history**  
   - Reason: time-series workloads benefit from columnar / time-optimised storage; use ClickHouse/Timescale depending on query patterns.

4. **Postgres for canonical transactional data**  
   - Reason: assignments/orders need ACID and schema constraints.

5. **Microservices with clear separation**  
   - Reason: tracking ingestion & enrichment is a hot path and needs independent scaling.

6. **Assignment transactional checks + DB unique constraints**  
   - Reason: ensure correctness; also handle race using DB unique constraints + saveAndFlush within transaction to surface conflicts.
  
7. **REST is chosen because:**

- Simple, predictable JSON APIs
- Works with Swagger/OpenAPI 3 (already integrated)
- Stable contract for driver apps, admin dashboards, and partners
- Easy monitoring via API Gateway / Spring Actuator
- Supports idempotency & concurrency control cleanly

## 1000 GPS updates/sec handling (short)
- Kafka producers with batching & compression → Kafka cluster sized to handle 1000 msg/s (small, even single broker can handle).
- Stream processor (Flink) with parallelism to handle enrichment and writes to Redis & TSDB.
- Redis stores the latest only; TSDB receives batched writes.

## Microservices vs Monolith
- Start with a modular monolith or small microservices; evolve to microservices where independent scaling is necessary (tracking, admin, driver).
