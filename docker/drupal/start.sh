#!/bin/bash

# docker run -e MYSQL_ROOT_PASSWORD=test123 -e MYSQL_DATABASE=drupal -e MYSQL_USER=drupal -e MYSQL_PASSWORD=test123 --name mysql-for-drupal -p 127.0.0.1:53306:3306 -d mysql:latest

# docker run -d -p 80:80 --link mysql-for-drupal:db -i -t drupal-v1-3:latest bash

if [ ! -f /var/www/sites/default/settings.php ];
then
	echo "/var/www/sites/default/settings.php does not exist -> start site-install"
	cd /var/www/
	drush site-install standard -y --account-name=admin --account-pass=admin --db-url="mysqli://${DB_ENV_MYSQL_USER}:${DB_ENV_MYSQL_PASSWORD}@${DB_PORT_3306_TCP_ADDR}:${DB_PORT_3306_TCP_PORT}/drupal"

	# TODO install modules

	a2enmod rewrite vhost_alias
	/etc/init.d/apache2 restart
	sleep 5s
else
	echo "/var/www/sites/default/settings.php already exists.. skipped site-install"
fi
