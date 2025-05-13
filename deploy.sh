#!/bin/bash

echo "Building Docker images and starting containers..."
docker-compose down
docker-compose build
docker-compose up -d

echo "Deployment completed!"