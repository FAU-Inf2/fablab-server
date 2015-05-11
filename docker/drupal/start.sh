#!/bin/bash

# if settings.php already exists, restart apache
if [ ! -f /var/www/sites/default/settings.php ];
then
	echo "/var/www/sites/default/settings.php does not exist -> start site-install"
	sleep 5s
	cd /var/www/
	drush site-install standard -y --account-name=${adminDrupal} --account-pass=${adminPassword} --db-url="mysqli://${DB_ENV_MYSQL_USER}:${DB_ENV_MYSQL_PASSWORD}@${DB_PORT_3306_TCP_ADDR}:${DB_PORT_3306_TCP_PORT}/drupal"

	# save host
	#export DB_HOST=${DB_PORT_3306_TCP_ADDR}
	#echo "DB_HOST=${DB_HOST}" >> /etc/environment 
	echo ${DB_PORT_3306_TCP_ADDR} > /oldhost.txt

	chown -R www-data:www-data /var/www/sites/default

	# TODO install modules
	echo "test installing module:"
	drush en fullcalendar -y
	#drush en notifications -y

	a2enmod rewrite vhost_alias
	/etc/init.d/apache2 restart
	sleep 5s
else
	echo "/var/www/sites/default/settings.php already exists.. skipped site-install"
	cd /var/www/sites/default/

	old_host=$(cat /oldhost.txt)
	echo ${old_host}

	sed -i s/${old_host}/${DB_PORT_3306_TCP_ADDR}/g settings.php
#	sed /host/s/\b${DB_OLD_HOST}\b/${DB_PORT_3306_TCP_ADDR}/g settings.php

	# save host
	echo ${DB_PORT_3306_TCP_ADDR} > /oldhost.txt

	/etc/init.d/apache2 restart
fi
