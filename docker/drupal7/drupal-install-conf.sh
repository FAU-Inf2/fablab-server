#!/bin/bash

echo "/var/www/sites/default/settings.php does not exist -> start site-install"
sleep 5s
cd /var/www/
drush site-install standard -y --account-name=${adminDrupal} --account-pass=${adminPassword} --db-prefix="drupal7_" --db-url="mysqli://${DB_ENV_MYSQL_USER}:${DB_ENV_MYSQL_PASSWORD}@${DB_PORT_3306_TCP_ADDR}:${DB_PORT_3306_TCP_PORT}/drupal7"

# save host
echo ${DB_PORT_3306_TCP_ADDR} > /oldhost.txt

# import old mysql-dump
#mysql -u${DB_ENV_MYSQL_USER} -p${DB_ENV_MYSQL_PASSWORD} -h${DB_PORT_3306_TCP_ADDR} drupal7 < /fab.sql 
# import new sql-dump (after basic migration)
mysql -u${DB_ENV_MYSQL_USER} -p${DB_ENV_MYSQL_PASSWORD} -h${DB_PORT_3306_TCP_ADDR} drupal7 < /fabdb_drupal7.sql

sleep 5s

chown -R www-data:www-data /var/www/sites/default

# uncomment for migration modules
#drush en -y migrate migrate_d2d 
#drush en -y migrate_ui migrate_d2d_ui

# install rest_server
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
