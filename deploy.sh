#!/bin/bash

echo "Building application..."
./gradlew clean build -x test

echo "Building Docker images and starting containers..."
docker-compose down
docker-compose build
docker-compose up -d

echo "Deployment completed!"