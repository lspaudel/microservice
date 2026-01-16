# microservice
This project is a Spring Boot–based microservices system built using a modular architecture, featuring Event-Driven design and Distributed Caching.

---

## Architecture Overview

This system uses a modern microservices architecture with the following patterns:
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Event-Driven Architecture**: RabbitMQ for asynchronous communication
- **Distributed Caching**: Redis for high-performance data access
- **Circuit Breaker**: Resilience4j for fault tolerance

---

## Services Overview

### 1. Core Service
This service handles the core business functionality.

**Responsibilities**
- Product management (CRUD)
- Warehouse management
- **Event Publishing**: Publishes `ProductCreatedEvent` to RabbitMQ
- **Caching**: Uses Redis to cache product data (`@Cacheable`)

**Key Endpoints**
- `/products`
- `/product-details`
- `/warehouse`

### 2. Notification Service
Handles system notifications and alerts.

**Responsibilities**
- **Event Listening**: Consumes `ProductCreatedEvent` from RabbitMQ
- Sends email notifications (simulated)
- Configuration managed via `application-dev.yml` (not committed)

### 3. Shared Service
Contains reusable code shared across all microservices.

**Includes**
- Global exception handler
- Custom exceptions (e.g. `ResourceNotFoundException`)
- **Event DTOs**: `ProductCreatedEvent`
- **Configuration Constants**: RabbitMQ exchange/queue names

### 4. Auth Service
Responsible for authentication and authorization.

**Responsibilities**
- Login / registration

### 5. Gateway Service
Single entry point for all client requests.

**Responsibilities**
- Request routing (routes `/core/**`, `/auth/**` etc.)
- Circuit Breaker (Resilience4j) implementation

### 6. Discovery Service
Service registry using Netflix Eureka. The following image shows all services registered with the Eureka Discovery Server.

![Eureka Clients](eureka_clients.png)

---

## Event-Driven Flow (RabbitMQ)

When a product is created:
1. **Core Service** matches request, saves to DB.
2. Publishes `ProductCreatedEvent` to `product-events-exchange`.
3. **Notification Service** listens to `product-created-queue`.
4. Asynchronously triggers notification logic.

## Caching Strategy (Redis)

- **Read-Through**: Product reads (`GET /products/{id}`) are cached.
- **Write-Through**: Updates (`PUT /products/{id}`) update the cache.
- **Eviction**: Deletions (`DELETE`) remove items from cache.
- **TTL**: Cache entries expire after 10 minutes.

---

## Running the Project

### Prerequisites
- Java 17+
- Maven
- PostgreSQL (Database)
- **RabbitMQ**: Required for event messaging (Port 5672)
- **Redis**: Required for caching (Port 6379)

### Build All Services
```bash
mvn clean install
```

### Run Services (recommended order)
1. **Infrastructure**:
   ```bash
   # Start RabbitMQ & Redis (if using Docker)
   docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:management
   docker run -d -p 6379:6379 redis:alpine
   ```
2. **Microservices**:
   - Discovery Service (`cd discovery && mvn spring-boot:run`)
   - Gateway Service (`cd gateway && mvn spring-boot:run`)
   - Auth Service (`cd auth && mvn spring-boot:run`)
   - Core Service (`cd core && mvn spring-boot:run`)
   - Notification Service (`cd notification && mvn spring-boot:run -Dspring.profiles.active=dev`)

---

## Verification Requests (Postman / cURL)

You can import these cURL commands into Postman to verify the system.

### 1. Create Warehouse (Prerequisite)
**POST** `http://localhost:8075/warehouse`
```bash
curl -X POST http://localhost:8075/warehouse \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Main Warehouse",
    "address": "123 Tech Street"
  }'
```

### 2. Create Product (Triggers Event & Cache)
**POST** `http://localhost:8075/products`
```bash
curl -X POST http://localhost:8075/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MacBook Pro", 
    "price": 2000.0, 
    "quantity": 10, 
    "sku": "MBP-2026",
    "warehouseId": 1
  }'
```

### 3. Get Product (Verifies Cache)
**GET** `http://localhost:8075/products/1`
```bash
curl http://localhost:8075/products/1
```
*First request hits DB, subsequent requests hit Redis cache.*

### 4. Check Actuator Health
**GET** `http://localhost:8075/actuator/health`
```bash
curl http://localhost:8075/actuator/health
```

### 5. Verify Tracing (Zipkin)
**GET** `http://localhost:9411`
- Open Zipkin UI.
- Click "Run Query" to see traces after making requests.
- You should see spans across `gateway`, `core`, `auth`, etc.
