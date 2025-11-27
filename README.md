# Fleet-Application
 Fleet Operations Management System
 Backend service to manage Drivers, Vehicles, Hubs, Terminals, Deliveries, Shifts, Orders and Real-Time Driver Tracking using Redis.

## ⚙️Tech Stack
1. Java 17
2. Spring Boot 3.2.x
3. PostgreSQL 15
4. Redis 7.x (for real-time driver tracking)
5. Hibernate (JPA)
6. Flyway migrations
7. Maven
8. JUnit + Mockito
9. Swagger / OpenAPI

# Project Architecture
1. admin → Admin APIs (drivers, vehicles, hubs, terminals, deliveries)
2. driver → Driver APIs (shifts, delivery updates, tracking)
3. tracking  → Redis-based real-time tracking
4. common → Entities, DTOs, Mappers, Repositories, Exceptions
5. config → Redis, Swagger, Database configuration
6. tests  → Unit & Integration tests

# Prerequisites
## Dependency	Version
1. Java	17
2. Maven	3.9+
3. PostgreSQL	15
4. Redis	7.x


## Validate:

1. java -version
2. mvn -version
3. psql --version
4. redis-server --version

## PostgreSQL Setup
1️⃣ Create databases
`CREATE DATABASE fleet;`
`CREATE DATABASE fleet_test;`

2️⃣ Configure credentials
`src/main/resources/application-dev.properties`
`spring.datasource.url=jdbc:postgresql://localhost:5432/fleet`
`spring.datasource.username=postgres`
`spring.datasource.password=yourpassword`

`spring.jpa.hibernate.ddl-auto=none`
`spring.flyway.enabled=true`

`src/test/resources/application-test.properties`
`spring.datasource.url=jdbc:postgresql://localhost:5432/fleet_test`
`spring.datasource.username=postgres`
`spring.datasource.password=yourpassword`

`spring.jpa.hibernate.ddl-auto=none`
`spring.flyway.enabled=true`


## Flyway will run all SQL migrations from:

`src/main/resources/db/migration/`

## Redis Setup
### Local install
`redis-server`

### Redis config used:
`spring.redis.host=localhost`
`spring.redis.port=6379`
`spring.redis.timeout=6000ms`

`spring.redis.lettuce.pool.max-active=8`
`spring.redis.lettuce.pool.max-idle=8`
`spring.redis.lettuce.pool.min-idle=0`

# Run Application
Development mode:
`mvn spring-boot:run -Dspring-boot.run.profiles=dev`

# TECH_STACK.md

## Components & Rationale

- **Java 17 + Spring Boot 3.2.x**  
  Mature ecosystem, strong productivity, excellent telemetry & ecosystem.

- **Postgres (15)**  
  Strong transactional guarantees, extensions, good for relational data (orders, assignments). Use unique constraints & transactions for assignment integrity.

- **Kafka (Apache Kafka)**  
  Durable event bus, ordering per key, scalable ingestion, supports stream processing.

- **Stream processing: Flink / Kafka Streams**  
  Stateful processing, enrichment, dedupe, low-latency routing to Redis and TSDB.

- **Redis (Cluster)**  
  The latest location store uses GEO commands for nearest-driver lookups.

- **TSDB / OLAP: ClickHouse / TimescaleDB**  
  ClickHouse for high-volume analytics; TimescaleDB for SQL-time-series when relational joins are needed.

- **Observability: Prometheus + Grafana OR Datadog**  
  Monitor lag, latency, errors, and traces.

- **Deployment: Kubernetes**  
  Horizontal scaling, rolling updates, pod autoscaling.

## Why these choices?
- Each choice isolates the primary requirement: speed (Redis), durability & replay (Kafka), transactional correctness (Postgres), analytics (ClickHouse).

