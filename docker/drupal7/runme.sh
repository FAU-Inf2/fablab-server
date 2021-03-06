#!/bin/bash

# build drupal-docker
docker build -t fablab-drupal7 .
# build mysql-docker
docker build -t mysql-for-drupal7 mysql/

# run mysql-docker
docker run -e MYSQL_ROOT_PASSWORD=$adminPassword -e MYSQL_DATABASE=drupal7 -e MYSQL_USER=$drupalDBUser -e MYSQL_PASSWORD=$drupalPassword --name mysql-for-drupal7 -p 127.0.0.1:53306:3306 -d mysql:latest

# run drupal-docker
docker run -e adminDrupal=$drupalAdmin -e adminPassword=$drupalPassword -d  -p 50080:80 --link mysql-for-drupal7:db -i -t --name fablab-drupal7-v1 fablab-drupal7:latest

docker ps

docker attach fablab-drupal7-v1 
