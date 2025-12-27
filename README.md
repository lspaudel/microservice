# microservice
This project is a Spring Boot–based microservices system built using a modular architecture. 

---

## Services Overview

### 1. Core Service
Handles core business functionality.

**Responsibilities**
- Product management
- Product details management
- Warehouse management
- JPA entities, repositories, services, controllers
- Uses DTOs and mappers
- Uses shared exception handling

**Key Endpoints**
- `/product`
- `/product-details`
- `/warehouse`

---

### 2. Shared Service
Contains reusable code shared across all microservices.

**Includes**
- Global exception handler
- Custom exceptions (e.g. `ResourceNotFoundException`)

This module is added as a dependency in other services.

---

### 3. Auth Service
Responsible for authentication and authorization.

**Responsibilities**
- Login / registration

---

### 4. Gateway Service
Acts as the single entry point for all client requests.

**Responsibilities**
- Request routing  
The gateway routes incoming requests to the appropriate service using Eureka service discovery and applies basic resiliency patterns.
Requests starting with `/core/**` are routed to the CORE service.
Requests starting with `/auth/**` are routed to the AUTH service.
The gateway removes the service prefix (`/core` or `/auth`) before forwarding the request.
A circuit breaker is configured for each route to handle service failures gracefully. If the target service becomes unavailable, requests are redirected to a fallback endpoint.
This prevents cascading failures and keeps the system responsive.

---

### 5. Discovery Service
Service registry using Eureka.

**Responsibilities**
- Registers all services
- Enables service-to-service communication without hardcoding URLs  
  The following image shows all services registered with the Eureka Discovery Server.

![Eureka Clients](eureka_clients.png)
---

### Docker Containerization (Recommended)

Run the entire system (Database + 5 Services) with a single command.

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.

### 1. Configure Environment
Create a `.env` file in the root directory:
```properties
DB_USERNAME=postgres
DB_PASSWORD=your_password
DB_NAME=ecommerce
MAIL_USERNAME=your_gmail@gmail.com
MAIL_PASSWORD=your_app_password
```

### 2. Build & Run
```bash
# Build the JAR files
mvn clean package -DskipTests

# Start Docker containers
docker compose up --build
```
> **Note**: If you see connection errors initially, wait 1-2 minutes for the Discovery Service to fully initialize.

### 3. Access Services
- **Eureka Dashboard**: [http://localhost:8761](http://localhost:8761)
- **API Gateway**: [http://localhost:8888](http://localhost:8888)

---



**Base URL**: `http://localhost:8888`

#### 1. Notification - Send Email
- **Endpoint**: `POST /api/notification/send`
- **Body**:
```json
{
  "to": "your_email@example.com",
  "subject": "Test Email",
  "body": "Hello from Microservices!"
}
```

#### 2. Auth - Connectivity Test
- **Endpoint**: `GET /auth/test`

#### 3. Core - Create Warehouse
- **Endpoint**: `POST /core/warehouse`
- **Body**:
```json
{
  "name": "Central Warehouse",
  "address": "123 Tech Park, Silicon Valley"
}
```

#### 4. Core - Create Product
- **Endpoint**: `POST /core/products`
- **Body**:
```json
{
  "name": "Gaming Laptop",
  "sku": "LAPTOP-ROG-001",
  "price": 1499.99,
  "quantity": 50,
  "warehouseId": 1
}
```

#### 5. Core - Add Product Details
- **Endpoint**: `POST /core/product-details`
- **Body** (JSON):
```json
{
  "productId": 1,
  "manufacturer": "Asus",
  "description": "High performance gaming laptop",
  "weight": 2.5,
  "color": "Black"
}
```

