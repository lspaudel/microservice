# Apigee API Proxy Bundle - Microservices Gateway

This directory contains the Apigee API Proxy configuration for the microservices ecosystem.

## Overview

This proxy provides:
- Unified API gateway for all microservices
- Base path: `/microservices/v1`
- CORS support for web clients
- Distributed tracing headers
- Routes to Spring Cloud Gateway (port 8888)

## Structure

```
apiproxy/
├── microservice-gateway-proxy.xml      # Proxy bundle definition
├── proxies/
│   └── default.xml                     # Proxy endpoint (client-facing)
├── targets/
│   └── default.xml                     # Target endpoint (Spring Cloud Gateway)
└── policies/
    ├── AM-SetCORSHeaders.xml           # CORS headers policy
    └── AM-AddTraceHeaders.xml          # Trace ID injection policy
```

## Deployment Instructions

### 1. Create Deployment Package

```bash
cd apigee-bundle
zip -r microservice-gateway-proxy.zip apiproxy/
```

### 2. Deploy to Apigee

#### Option A: Using Apigee UI (Edge/X)
1. Navigate to **Develop > API Proxies**
2. Click **+ Proxy** → **Upload proxy bundle**
3. Upload `microservice-gateway-proxy.zip`
4. Deploy to desired environment (test/prod)

#### Option B: Using Apigee CLI
```bash
# Install Apigee CLI (if not already installed)
npm install -g apigeecli

# Authenticate
apigeecli token cache -a <access-token>

# Deploy proxy
apigeecli apis create bundle -f microservice-gateway-proxy.zip \
  --name microservice-gateway-proxy \
  --org <org-name> \
  --env <env-name>
```

### 3. Configure Target URL

By default, the target URL is set to `http://host.docker.internal:8888` for local development.

**For production deployment:**
1. Edit `apiproxy/targets/default.xml`
2. Replace the URL with your actual Spring Cloud Gateway address:
   ```xml
   <URL>http://your-gateway-host:8888</URL>
   ```
   or
   ```xml
   <URL>https://gateway.yourdomain.com</URL>
   ```
3. Re-zip and redeploy

## API Endpoints

All backend endpoints are exposed through Apigee with the `/microservices/v1` prefix:

### Core Service Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/microservices/v1/core/products` | Create product |
| GET | `/microservices/v1/core/products` | Get all products |
| GET | `/microservices/v1/core/products/{id}` | Get product by ID |
| PUT | `/microservices/v1/core/products/{id}` | Update product |
| DELETE | `/microservices/v1/core/products/{id}` | Delete product |
| POST | `/microservices/v1/core/warehouse` | Create warehouse |
| GET | `/microservices/v1/core/warehouse` | Get all warehouses |
| GET | `/microservices/v1/core/warehouse/{id}` | Get warehouse by ID |
| PUT | `/microservices/v1/core/warehouse/{id}` | Update warehouse |
| DELETE | `/microservices/v1/core/warehouse/{id}` | Delete warehouse |
| POST | `/microservices/v1/core/product-details` | Create product details |
| GET | `/microservices/v1/core/product-details` | Get all product details |
| GET | `/microservices/v1/core/product-details/{id}` | Get product details by ID |
| PUT | `/microservices/v1/core/product-details/{id}` | Update product details |
| DELETE | `/microservices/v1/core/product-details/{id}` | Delete product details |
| GET | `/microservices/v1/core/info` | Core service info |

### Auth Service Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/microservices/v1/auth/test` | Test auth integration |

### Notification Service Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/microservices/v1/notification/send` | Send email notification |

## Example API Calls

### Create Warehouse
```bash
curl -X POST https://{apigee-org}-{env}.apigee.net/microservices/v1/core/warehouse \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Main Warehouse",
    "address": "123 Tech Street"
  }'
```

### Create Product
```bash
curl -X POST https://{apigee-org}-{env}.apigee.net/microservices/v1/core/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MacBook Pro",
    "price": 2000.0,
    "quantity": 10,
    "sku": "MBP-2026",
    "warehouseId": 1
  }'
```

### Get All Products
```bash
curl https://{apigee-org}-{env}.apigee.net/microservices/v1/core/products
```

### Get Product by ID
```bash
curl https://{apigee-org}-{env}.apigee.net/microservices/v1/core/products/1
```

### Send Notification
```bash
curl -X POST https://{apigee-org}-{env}.apigee.net/microservices/v1/notification/send \
  -H "Content-Type: application/json" \
  -d '{
    "to": "user@example.com",
    "subject": "Test Notification",
    "body": "Hello from Apigee!"
  }'
```

## Testing Locally

If you're using Apigee emulator or local development:

```bash
# Ensure Spring Cloud Gateway is running on port 8888
cd /path/to/microservice
mvn clean install
# Start all services (Discovery, Gateway, Core, Auth, Notification)

# Test through Apigee local endpoint
curl http://localhost:9001/microservices/v1/core/products
```

## Features

### CORS Support
The proxy automatically adds CORS headers to responses:
- `Access-Control-Allow-Origin: *`
- `Access-Control-Allow-Methods: GET, PUT, POST, DELETE, OPTIONS`
- `Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With, X-Trace-ID`

### Distributed Tracing
The proxy injects trace headers for integration with Zipkin:
- `X-Trace-ID`: Unique message ID for request tracing
- `X-Apigee-Proxy`: Identifies the proxy name

### Path Routing
All requests are forwarded to Spring Cloud Gateway, which handles:
- Service discovery via Eureka
- Load balancing
- Circuit breaker (Resilience4j)
- Routing to backend microservices

## Troubleshooting

### Connection Refused
If you get "connection refused" errors:
1. Verify Spring Cloud Gateway is running on port 8888
2. Check the target URL in `targets/default.xml`
3. For Docker deployments, ensure `host.docker.internal` resolves correctly

### 404 Not Found
If endpoints return 404:
1. Verify the base path is `/microservices/v1`
2. Check Spring Cloud Gateway routing configuration
3. Ensure backend services are registered with Eureka

### CORS Errors
If you encounter CORS issues:
1. Verify `AM-SetCORSHeaders` policy is attached in PreFlow
2. Check browser console for specific CORS error details
3. Adjust CORS policy headers as needed

## Next Steps

Consider adding these enhancements:
- **Rate Limiting**: Add Quota or SpikeArrest policies
- **Authentication**: Add OAuth 2.0 or API Key verification
- **Response Caching**: Cache GET responses for better performance
- **Request Validation**: Add schema validation policies
- **Analytics**: Enable Apigee Analytics for monitoring

## Related Documentation

- [Spring Cloud Gateway Routes](file:///Users/laxmansapaudel/Desktop/microservice/gateway/src/main/java/com/example/gateway/config/GatewayRouteConfig.java)
- [Project README](file:///Users/laxmansapaudel/Desktop/microservice/README.md)
- Apigee Documentation: https://cloud.google.com/apigee/docs
