#!/bin/bash

# Exit on error
set -ex

# Check if docker daemon is running
docker info > /dev/null 2>&1

# Compile jar files
sbt ';clean;assembly'

# Build and tag docker images
DOCKER_BUILDKIT=1 docker build --target othello-root -t othello-root .
DOCKER_BUILDKIT=1 docker build --target boardmodule -t boardmodule .
DOCKER_BUILDKIT=1 docker build --target usermodule -t usermodule .
DOCKER_BUILDKIT=1 docker build --target othello-mongodb -t othello-mongodb .

# Run docker containers in detached mode
COMPOSE_PROJECT_NAME=othelloinscala docker-compose up -d
