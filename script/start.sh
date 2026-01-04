#!/bin/bash

echo "Starting MO Demo Application Stack..."

echo "Starting Java backend "
./mvnw spring-boot:run

# Start frontend service
echo "Starting frontend service..."
cd mo-demo-frontend
npm install
npm run dev
cd ..
curl http://localhost:8080/actuator/health

echo "Application stack started successfully!"
echo "Backend API: http://localhost:8080"
echo "Frontend: http://localhost:3000"

