#!/bin/bash

# Load environment variables from .env file
if [ -f .env ]; then
    echo "Loading environment variables from .env file..."
    export $(cat .env | grep -v '^#' | xargs)
    echo "✓ Environment variables loaded"
else
    echo "⚠ .env file not found!"
    exit 1
fi

# Display loaded RabbitMQ credentials
echo ""
echo "RabbitMQ Configuration:"
echo "  Username: ${RABBITMQ_USERNAME}"
echo "  Password: ${RABBITMQ_PASSWORD:0:3}***"
echo ""

# Run the service passed as argument
if [ -z "$1" ]; then
    echo "Usage: ./run-service.sh <service-name>"
    echo "Example: ./run-service.sh core"
    exit 1
fi

SERVICE=$1
echo "Starting $SERVICE service..."
cd $SERVICE && mvn spring-boot:run
