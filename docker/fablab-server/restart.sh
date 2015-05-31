#!/bin/sh

docker stop rest-server && docker rm rest-server
sh start.sh