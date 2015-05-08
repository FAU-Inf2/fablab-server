#!/bin/bash

# build drupal-docker
docker build -t basic-drupal .
# build mysql-docker
docker build -t mysql-for-drupal mysql/

# run mysql-docker
docker run -e MYSQL_ROOT_PASSWORD=test123 -e MYSQL_DATABASE=drupal -e MYSQL_USER=drupal -e MYSQL_PASSWORD=test123 --name mysql-for-drupal -p 127.0.0.1:53306:3306 -d mysql:latest

# run drupal-docker
docker run -d -p 80:80 --link mysql-for-drupal:db -i -t --name basic-drupal-v1 basic-drupal:latest

docker ps
