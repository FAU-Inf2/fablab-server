#!/bin/sh

sh create.sh
docker kill $(docker ps -q -f name=rest-server)
sh start.sh
