#!/bin/sh

docker run -d -p 80:8080 -p 8081:8081 --name="rest-server" -e adminUsername=$adminUsername -e adminPassword=$adminPassword container-fablab-server
