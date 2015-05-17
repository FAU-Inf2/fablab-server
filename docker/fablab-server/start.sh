#!/bin/sh

docker run -d \
    -p 80:8080 -p 8081:8081 -p 443:8082 --name="rest-server" \
    -e openerp_hostname=$openerp_hostname \
    -e openerp_database=$openerp_database \
    -e openerp_user=$openerp_user \
    -e openerp_password=$openerp_password \
    -e adminUsername=$adminUsername \
    -e adminPassword=$adminPassword \
    -e FABLAB_KEYSTORE_PASSWORD=$FABLAB_KEYSTORE_PASSWORD \
    container-fablab-server
