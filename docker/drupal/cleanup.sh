#!/bin/bash

docker stop mysql-for-drupal
docker rm mysql-for-drupal basic-drupal-v1 
docker rmi mysql-for-drupal basic-drupal
