# Microservices API Testing Guide

> **IMPORTANT:** Always use the API Gateway at `http://localhost:8888` to access all services. Never call services directly on their ports.

---

## Architecture Overview

```mermaid
    Client[Postman/Frontend] --> Gateway[API Gateway :8888]
    Gateway --> Core[Core Service :8075]
    Gateway --> Auth[Auth Service :8090]
    Gateway --> Notification[Notification Service :8084]
    Core --> DB[(PostgreSQL :5432)]
    Gateway --> Discovery[Eureka Discovery :8761]
    Core --> Discovery
    Auth --> Discovery
    Notification --> Discovery
    Core --> ConfigServer[Config Server :8889]
    Auth --> ConfigServer
    Notification --> ConfigServer
    Gateway --> ConfigServer
```
![Architecture_Diagram](../architecture_diagram.png)

---

## Service Registration
The image below shows the services registered with the Eureka Discovery Server:

![Eureka Clients](../eureka_clients.png)

---

## Service Endpoints

### Base Gateway URL
All requests must go through the API Gateway:
```
http://localhost:8888
```

---

## 1. Core Service - Product Management

### 1.1 Warehouse Endpoints

#### Create Warehouse
**You must create a warehouse before creating products!**

```http
POST http://localhost:8888/core/api/warehouse
Content-Type: application/json

{
  "name": "Main Warehouse",
  "address": "New York, USA"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Main Warehouse",
  "address": "New York, USA",
  "products": []
}
```

#### Get All Warehouses
```http
GET http://localhost:8888/core/api/warehouse
```

#### Get Warehouse by ID
```http
GET http://localhost:8888/core/api/warehouse/1
```

#### Update Warehouse
```http
PUT http://localhost:8888/core/api/warehouse/1
Content-Type: application/json

{
  "name": "Updated Warehouse",
  "address": "Los Angeles, USA"
}
```

#### Delete Warehouse
```http
DELETE http://localhost:8888/core/api/warehouse/1
```

---

### 1.2 Product Endpoints

#### Create Product
**Requires a valid `warehouseId` from step 1.1**

```http
POST http://localhost:8888/core/api/products
Content-Type: application/json

{
  "name": "Gaming Laptop",
  "description": "High-performance laptop for gaming",
  "price": 1299.99,
  "stockQuantity": 50,
  "warehouseId": 1
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Gaming Laptop",
  "sku": "generated-sku",
  "price": 1299.99,
  "quantity": 50,
  "warehouseId": 1
}
```

#### Get All Products
```http
GET http://localhost:8888/core/api/products
```

#### Get Product by ID
```http
GET http://localhost:8888/core/api/products/1
```

#### Update Product
```http
PUT http://localhost:8888/core/api/products/1
Content-Type: application/json

{
  "name": "Gaming Laptop Pro",
  "description": "Updated description",
  "price": 1499.99,
  "stockQuantity": 45,
  "warehouseId": 1
}
```

#### Delete Product
```http
DELETE http://localhost:8888/core/api/products/1
```

---

### 1.3 Product Details Endpoints

#### Create Product Details
**Requires a valid `productId` from step 1.2**  
**Note:** Each product can only have ONE set of details (unique constraint)

```http
POST http://localhost:8888/core/api/product-details
Content-Type: application/json

{
  "description": "15.6 inch display with RGB backlit keyboard",
  "manufacturer": "TechCorp Industries",
  "weight": 2.5,
  "color": "Midnight Black",
  "productId": 1
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "description": "15.6 inch display with RGB backlit keyboard",
  "manufacturer": "TechCorp Industries",
  "weight": 2.5,
  "color": "Midnight Black",
  "productId": 1
}
```

#### Get All Product Details
```http
GET http://localhost:8888/core/api/product-details
```

#### Get Product Details by ID
```http
GET http://localhost:8888/core/api/product-details/1
```

#### Update Product Details
```http
PUT http://localhost:8888/core/api/product-details/1
Content-Type: application/json

{
  "description": "Updated description",
  "manufacturer": "TechCorp Inc.",
  "weight": 2.3,
  "color": "Space Gray",
  "productId": 1
}
```

#### Delete Product Details
```http
DELETE http://localhost:8888/core/api/product-details/1
```

---

## 2. Notification Service - Email

### Send Email
```http
POST http://localhost:8888/api/notification/send
Content-Type: application/json

{
  "to": "recipient@example.com",
  "subject": "Test Email from Microservice",
  "body": "This is a test email sent from the Notification Service!"
}
```

**Response (200 OK):**
```json
{
  "message": "Email sent successfully"
}
```

> **📧 Gmail Setup Required:**  
> Make sure your `.env` file contains valid Gmail credentials:
> ```env
> MAIL_USERNAME=your-email@gmail.com
> MAIL_PASSWORD=your-gmail-app-password
> ```
> **Note:** Use a [Gmail App Password](https://support.google.com/accounts/answer/185833), not your regular password!

---

## 3. Auth Service

### Health Check
```http
GET http://localhost:8888/auth/api/auth/health
```

**Response (200 OK):**
```json
{
  "status": "UP",
  "service": "Auth Service",
  "message": "Service is running successfully"
}
```

---

## Testing Workflow

Follow this order to test the complete system:

### Step 1: Verify Services
Check Eureka Dashboard to ensure all services are registered:
```
http://localhost:8761
```

You should see: **CORE**, **AUTH**, **GATEWAY**, **NOTIFICATION**, **CONFIG-SERVER**

### Step 2: Create Warehouse
```http
POST http://localhost:8888/core/api/warehouse
{"name": "Main Warehouse", "address": "New York"}
```
→ **Save the returned `id`** (e.g., `id: 1`)

### Step 3: Create Product
```http
POST http://localhost:8888/core/api/products
{
  "name": "Gaming Laptop",
  "description": "High-end laptop",
  "price": 1299.99,
  "stockQuantity": 50,
  "warehouseId": 1
}
```
→ **Save the returned `id`** (e.g., `id: 1`)

### Step 4: Create Product Details
```http
POST http://localhost:8888/core/api/product-details
{
  "description": "15.6 inch RGB laptop",
  "manufacturer": "TechCorp",
  "weight": 2.5,
  "color": "Black",
  "productId": 1
}
```

### Step 5: Test Notification Service
```http
POST http://localhost:8888/api/notification/send
{
  "to": "your-email@gmail.com",
  "subject": "Test",
  "body": "Test email"
}
```

### Step 6: Test Auth Health Check
```http
GET http://localhost:8888/auth/api/auth/health
```

**Expected Response:**
```json
{
  "status": "UP",
  "service": "Auth Service",
  "message": "Service is running successfully"
}
```

---

## Common Errors & Solutions

### 🔴 Error: `404 Not Found`
**Cause:** Wrong URL or missing `/api/` prefix  
**Solution:** 
- ❌ `http://localhost:8075/products` (bypassing gateway)
- ❌ `http://localhost:8888/product-details` (missing `/api/`)
- ✅ `http://localhost:8888/core/api/products`
- ✅ `http://localhost:8888/core/api/product-details`

### 🔴 Error: `500 Internal Server Error - "Warehouse not found"`
**Cause:** Trying to create a product without creating a warehouse first  
**Solution:** Create a warehouse (Step 2) before creating products (Step 3)

### 🔴 Error: `500 - "duplicate key value violates unique constraint"`
**Cause:** Trying to create product details for a product that already has details  
**Solution:** 
- Use a different `productId` that doesn't have details yet
- Or use `PUT` to update existing details instead of `POST`

### 🔴 Error: `503 Service Unavailable`
**Cause:** The target service is not running or not registered with Eureka  
**Solution:** 
1. Check http://localhost:8761 to verify service is registered
2. Check `docker ps` to ensure container is running
3. Restart the service: `docker restart <service-name>`

### 🔴 Error: `400 Bad Request - Validation error`
**Cause:** Missing required fields or invalid data  
**Common Issues:**
- `weight` must be greater than 0
- All string fields must not be blank
- IDs must be positive numbers

---

## Service Ports Reference

| Service       | Port | Direct URL (Debug Only)          | Gateway Route                     |
|---------------|------|----------------------------------|-----------------------------------|
| **Gateway**   | 8888 | http://localhost:8888 ✅ **USE** | N/A                               |
| Discovery     | 8761 | http://localhost:8761            | N/A                               |
| Config Server | 8889 | http://localhost:8889            | N/A                               |
| Core          | 8075 | ~~http://localhost:8075~~        | `http://localhost:8888/core/**`   |
| Auth          | 8090 | ~~http://localhost:8090~~        | `http://localhost:8888/auth/**`   |
| Notification  | 8084 | ~~http://localhost:8084~~        | `http://localhost:8888/api/notification/**` |
| PostgreSQL    | 5432 | localhost:5432                   | N/A                               |

---

## Configuration URLs

### Eureka Dashboard
```
http://localhost:8761
```
View all registered services and their health status.

### Config Server
```
http://localhost:8889/{service-name}/default
```

Examples:
- `http://localhost:8889/core/default`
- `http://localhost:8889/gateway/default`
- `http://localhost:8889/notification/default`

---

## Postman Collection Structure

```
Microservices API
├── 1. Core Service
│   ├── Warehouses
│   │   ├── Create Warehouse
│   │   ├── Get All Warehouses
│   │   ├── Get Warehouse by ID
│   │   ├── Update Warehouse
│   │   └── Delete Warehouse
│   ├── Products
│   │   ├── Create Product
│   │   ├── Get All Products
│   │   ├── Get Product by ID
│   │   ├── Update Product
│   │   └── Delete Product
│   └── Product Details
│       ├── Create Product Details
│       ├── Get All Product Details
│       ├── Get Product Details by ID
│       ├── Update Product Details
│       └── Delete Product Details
├── 2. Notification Service
│   └── Send Email
└── 3. Auth Service
    └── Health Check
```

---

## Environment Variables

Create a `.env` file in the project root:

```env
# Database Configuration
DB_USERNAME=your_db_username
DB_PASSWORD=your_secure_db_password
DB_NAME=ecommerce

# Email Configuration (Gmail)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-gmail-app-password
```

---

## Quick Start Commands

### Start All Services
```bash
docker compose up --build
```

### Stop All Services
```bash
docker compose down
```

### Restart a Specific Service
```bash
docker restart <service-name>
# Example: docker restart core
```

### View Service Logs
```bash
docker logs <service-name> --tail 50
# Example: docker logs notification --tail 50
```

### Database Access
```bash
docker exec -it postgres psql -U postgres -d ecommerce
```

---

## Data Validation Rules

### Warehouse
- `name`: Required, max 100 characters
- `address`: Required, max 200 characters

### Product
- `name`: Required, max 100 characters
- `description`: Required, max 500 characters
- `price`: Required, must be positive
- `stockQuantity`: Required, must be >= 0
- `warehouseId`: Required, must exist in database

### Product Details
- `description`: Required, max 500 characters
- `manufacturer`: Required, max 100 characters
- `weight`: Required, must be positive (> 0)
- `color`: Required, max 50 characters
- `productId`: Required, must exist in database, **must be unique** (one detail per product)

### Email
- `to`: Required, valid email format
- `subject`: Required
- `body`: Required

---

## Troubleshooting

### Services won't start
1. **Check Docker:** Ensure Docker is running
2. **Check ports:** Make sure ports 8075, 8084, 8088, 8090, 8761, 8889, 5432 are not in use
3. **Rebuild:** Run `docker compose down && docker compose up --build`

### Services registered but APIs return errors
1. **Wait 30 seconds** after `docker compose up` for all services to fully initialize
2. **Check Config Server:** Visit `http://localhost:8889/core/default` to verify configs are loading
3. **Check logs:** `docker logs <service-name>` to see detailed errors

### Email not sending
1. **Verify Gmail App Password:** Must use App Password, not regular password
2. **Check .env file:** Ensure `MAIL_USERNAME` and `MAIL_PASSWORD` are set
3. **Check logs:** `docker logs notification` for SMTP errors
4. **Restart notification:** `docker restart notification` after updating `.env`

> **Note:** Notification service may crash on first startup if Config Server isn't ready. Simply restart it with `docker restart notification` after all services are up.

---

## License & Support

For issues or questions, check the logs or restart the relevant service. All services use Spring Boot 3.2.5 with Spring Cloud 2023.0.1.
