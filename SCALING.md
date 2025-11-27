# Scaling to 100k Vehicles & 1M GPS Points/Minute
To reliably handle 100k active vehicles sending around 1,000,000 GPS updates per minute (~16.6k events/sec), the system must separate ingestion, 
real-time lookup, and historical storage. The architecture below focuses only on the components that matter at scale.

## 1. Gateway Layer (Ingress)

All driver and admin requests pass through a lightweight Gateway (HTTP/gRPC).
The Gateway performs:

1. Authentication
2. Basic validation
3. Small batching and fast ACKs
4. This keeps device interactions fast and prevents backpressure leaks into downstream components.

## 2. Kafka for High-Volume Event Ingestion

All GPS location updates are published to Kafka.
Key points:
1. Partition by vehicle_id → preserves ordering for each vehicle
2. Compression reduces bandwidth & broker storage
3. Kafka acts as the central buffer that smooths bursts and protects the rest of the system.

## 3. Stream Processing for Enrichment & Routing
1. A lightweight streaming layer (Kafka Streams or Flink) consumes location messages

2. Validates & deduplicates updates

3. Enriches data with driver/vehicle metadata

### Splits into two output streams:

1. Recent/real-time state → Redis Geo

2. Historical telemetry → Time-Series DB (ClickHouse / TimescaleDB)

3. This decouples hot queries from long-term storage.

4. Redis Geo for Fast Real-Time Lookups

#### Redis stores the latest location of every active driver:

1. `GEOADD` to keep coordinates updated

2. `GEOSEARCH / GEORADIUS` for “drivers near point X”

##### Redis Sets track:
1. Active drivers

2. Drivers currently on delivery

3. Available/idle vehicles

#### Time-Series DB for History (TSDB / OLAP)

1. Historical GPS data is written in batched form to a time-series optimized database:
2. ClickHouse (OLAP) or TimescaleDB (PostgreSQL extension)
3. Supports billions of rows

`Ideal for analytics, heatmaps, compliance, and replay
Older data is moved to S3/Parquet for long-term retention at low cost.

# 6. Monitoring & Lag Control

Prometheus + Grafana monitor or Datadog Tracing + SignOz(Open Source)

1. Kafka consumer lag
2. Redis cluster load
3. TSDB write throughput
4. Overall ingestion rate

This ensures ingestion remains stable even at peak bursts.
