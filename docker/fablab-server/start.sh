#!/bin/sh

docker run -p 80:8080 -p 8081:8081 -e adminUsername=$adminUsername -e adminPassword=$adminPassword container-fablab-server

