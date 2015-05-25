#!/bin/bash

# build drupal-docker
docker build -t fablab-drupal .
# build mysql-docker
docker build -t mysql-for-drupal mysql/

# run mysql-docker
docker run -e MYSQL_ROOT_PASSWORD=$adminPassword -e MYSQL_DATABASE=drupal -e MYSQL_USER=$drupalDBUser -e MYSQL_PASSWORD=$drupalPassword --name mysql-for-drupal -p 127.0.0.1:53306:3306 -d mysql:latest

# run drupal-docker
docker run -e adminDrupal=$drupalAdmin -e adminPassword=$drupalPassword -d  -p 50080:80 --link mysql-for-drupal:db -i -t --name fablab-drupal-v1 fablab-drupal:latest

docker ps

#docker attach fablab-drupal-v1 
