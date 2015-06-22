#!/bin/sh

echo Starting database container
docker run -d \
    --name="fablab-db" \
    container-fablab-database
