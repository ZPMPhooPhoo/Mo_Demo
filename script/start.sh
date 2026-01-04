#!/bin/bash

echo "Starting MO Demo Application Stack..."

# Start backend and database services
echo "Starting Java backend and MySQL database..."
docker-compose up -d

# Wait for database to be ready
echo "Waiting for database to be ready..."
sleep 30

# Check database health
echo "Checking database health..."
docker-compose exec mysql mysqladmin ping -h localhost -u root -prootpassword

# Import SQL file
echo "Importing database schema..."
docker-compose exec -T mysql mysql -u root -prootpassword mo_demo_db < script/mo_demo.sql

# Start frontend service
echo "Starting frontend service..."
cd mo-demo-frontend
docker-compose up -d
cd ..

echo "Application stack started successfully!"
echo "Backend API: http://localhost:8080"
echo "Frontend: http://localhost:3000"
echo "Database: localhost:3308"

# Show running containers
echo "Running containers:"
docker ps --filter "name=mo-demo"
