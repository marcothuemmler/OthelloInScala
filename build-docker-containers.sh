#!/bin/bash

# Exit on error
set -ex

# Compile jar files
sbt ';clean;assembly'

# Build and tag docker images
DOCKER_BUILDKIT=1 docker build --target othello-root -t othello-root .
DOCKER_BUILDKIT=1 docker build --target boardmodule -t boardmodule .
DOCKER_BUILDKIT=1 docker build --target usermodule -t usermodule .

# Run docker containers in detached mode
docker-compose up -d
