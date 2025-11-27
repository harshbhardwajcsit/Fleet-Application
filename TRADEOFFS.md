# TRADEOFFS

## Summary of major trade-offs
1. **Kafka vs HTTP direct writes to DB**
   - Kafka pros: buffering, ordering, replay, decoupling.
   - Kafka cons: operational complexity.
   - Decision: use Kafka for telemetry to protect DB and enable replay/analytics.

2. **Redis for latest state vs storing everything in TSDB**
   - Redis pros: sub-10ms reads, geo operations.
   - Redis cons: memory cost; not for a large history.
   - Decision: Redis = latest; TSDB = history.

3. **ClickHouse vs Relational/Non-Relational DB **
   - ClickHouse: excellent OLAP for analytics / cost-effective on large volumes.
   - TimescaleDB: SQL + Postgres familiarity, good at relational + timeseries.
   - Decision: Choose ClickHouse for heavy analytics; TimescaleDB when relational joins and SQL are priority.
   - With the large High throughput, other DBs lag, also requiring rapid optimization and scaling leads to higher cost & complexity 

4. **Microservices vs Monolith**
   - Microservices: independent scaling (tracking vs admin). More complexity.
   - Monolith: simpler local dev, but hard to scale hot-path independently.
   - Decision: modular microservices with clear boundaries (tracking, admin, driver) to scale independent hot-paths.


## Assumptions
- Devices can batch telemetry Events.
- Network reliability between edge and cloud is reasonable (retries/backoff supported).
- Admin operations are low QPS compared to telemetry.
