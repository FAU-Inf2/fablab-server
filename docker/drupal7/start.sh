#!/bin/bash

# if settings.php does not exist, start drupal installation and configuration
if [ ! -f /var/www/sites/default/settings.php ];
then
	echo "Starting Drupal installation and configuration.."
	/drupal-install-conf.sh

	echo "Test for successful installation and configuration.."

	getnode=$(printf "GET /node/40\n\r\n\r" | nc -q 5 localhost 80)

	if [ -z $getnode ]
	then
		echo "Result was empty. Retry installation.."
		/drupal-install-conf.sh

		getnode=$(printf "GET /node/40\n\r\n\r" | nc -q 5 localhost 80)

		if [ -z $getnode ]
		then
			echo "Installation failed, you should check for errors!"
		fi
	else
		echo "Installation successful!"
	fi 
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
