#!/bin/bash

# if settings.php already exists, restart apache
if [ ! -f /var/www/sites/default/settings.php ];
then
	echo "/var/www/sites/default/settings.php does not exist -> start site-install"
	sleep 5s
	cd /var/www/
	drush site-install standard -y --account-name=admin --account-pass=admin --db-url="mysqli://${DB_ENV_MYSQL_USER}:${DB_ENV_MYSQL_PASSWORD}@${DB_PORT_3306_TCP_ADDR}:${DB_PORT_3306_TCP_PORT}/drupal"

	chown -R www-data:www-data /var/www/sites/default

	# TODO install modules

	a2enmod rewrite vhost_alias
	/etc/init.d/apache2 restart
	sleep 5s
else
	echo "/var/www/sites/default/settings.php already exists.. skipped site-install"
	/etc/init.d/apache2 restart
fi
