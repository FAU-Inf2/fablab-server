#!/bin/bash

sh create.sh
docker kill $(docker ps -q -f name=rest-server)
./start.sh
