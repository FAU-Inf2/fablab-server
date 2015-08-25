#!/bin/sh

NAME="rest-server-"$(date +%Y-%m-%d_%H-%M-%S)

docker run -d \
    -p 8081:8081 -p 4433:4433 --name="$NAME" \
    -e openerp_hostname=$openerp_hostname \
    -e openerp_database=$openerp_database \
    -e openerp_user=$openerp_user \
    -e openerp_password=$openerp_password \
    -e adminUsername=$adminUsername \
    -e adminPassword=$adminPassword \
    -e FABLAB_KEYSTORE_PASSWORD="$FABLAB_KEYSTORE_PASSWORD" \
    -e pushAPIkey="$pushAPIkey" \
    -e pushServiceURL="$pushServiceURL" \
    -e APN_PASSWORD=$APN_PASSWORD \
    --link fablab-db:db \
    container-fablab-server
