#!/bin/bash

# Exit on error
set -ex

# Compile jar files
sbt clean
sbt assembly

# Build and tag docker containers
docker build . -t othello-root
docker build ./BoardModule -t boardmodule
docker build ./UserModule -t usermodule

# Run docker containers in detached mode
docker-compose up -d
