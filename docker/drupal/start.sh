#!/bin/bash

# if settings.php already exists, restart apache
if [ ! -f /var/www/sites/default/settings.php ];
then
	echo "/var/www/sites/default/settings.php does not exist -> start site-install"
	sleep 5s
	cd /var/www/
	drush site-install standard -y --account-name=${adminDrupal} --account-pass=${adminPassword} --db-url="mysqli://${DB_ENV_MYSQL_USER}:${DB_ENV_MYSQL_PASSWORD}@${DB_PORT_3306_TCP_ADDR}:${DB_PORT_3306_TCP_PORT}/drupal"

	# save host
	echo ${DB_PORT_3306_TCP_ADDR} > /oldhost.txt

	# import mysql-dump
	#mysql -u${DB_ENV_MYSQL_USER} -p${DB_ENV_MYSQL_PASSWORD} -h${DB_PORT_3306_TCP_ADDR} drupal < /fablab.sql 
	mysql -u${DB_ENV_MYSQL_USER} -p${DB_ENV_MYSQL_PASSWORD} -h${DB_PORT_3306_TCP_ADDR} drupal < /fab.sql 

	sleep 5s

	chown -R www-data:www-data /var/www/sites/default

	# install rest_server
	#drush en -y services rest_server
	cd /var/www/modules
	wget http://ftp.drupal.org/files/projects/services-6.x-3.3.tar.gz
	tar -xzvf *.gz 
	rm *.gz
	cd ..
	drush en -y ctools autoload inputstream
	# enable services
	drush en -y services rest_server

	# install modules
	echo "test installing module:"
	drush dl -y cck
	drush en -y content
	drush en -y active_tags antispam better_formats calendar content_taxonomy custom_breadcrumbs date devel diff filefield filefield_paths footnotes fullcalendar image imageapi imagecache imagecache_batch imagefield imce imce_wysiwyg jquery_ui jquery_update lightbox2 linkit l10n_client l10n_update messaging mothermayi notifications pathauto pathologic profile_permission skinr devel_themer token transliteration views view_unpublished views_rss webform wysiwyg

	# theme
	drush dl -y fusion
	drush en -y fusion_core
	drush vset theme_default fusion_core

	# clear cache
	drush cc all

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
