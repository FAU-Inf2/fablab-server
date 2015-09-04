# fablab-server [![Build Status](https://travis-ci.org/FAU-Inf2/fablab-server.svg?branch=master)](https://travis-ci.org/FAU-Inf2/fablab-server)

fablab-server is a REST server based on [Dropwizard](http://www.dropwizard.io) needed for the [fablab-android-App](https://github.com/FAU-Inf2/fablab-android).

## Build

To build and run the server, just type:
    
    //set environment variables needed for admin authentication
    export hostname=<HOSTNAME_OF_YOUR_SERVER>
    
    export USER_ADMIN=<FABLAB_ADMIN_USER>
    export PASSWORD_ADMIN=<FABLAB_ADMIN_PASSWORD>
    export USER_INVENTORY=<FABLAB_INVENTORY_USER>
    export PASSWORD_INVENTORY=<FABLAB_INVENTORY_PASSWORD>
    
    export adminUsername=<DROPWIZARD_ADMIN_USER>
    export adminPassword=<DROPWIZARD_ADMIN_PASSWORD>
    
    export openerp_hostname=<OPENERP_HOSTNAME>
    export openerp_database=<OPENERP_DATABASE>
    export openerp_user=<OPENERP_USER>
    export openerp_password=<OPENERP_PASSWORD>
    
    export FABLAB_KEYSTORE_PASSWORD=<SSL_KEYSTORE_PASSWORD>
    export FABLAB_TRUSTSTORE_PATH=<PATH_TO_DROPWIZARD_TRUSTSTORE>
    export FABLAB_REST_SERVER_TYPE=<SSL_CERT_ALIAS>
    
    export pushAPIkey=<ANDROID_GCM_API_KEY>
    export pushServiceURL="https://android.clients.google.com/gcm/send"
    
    export drupalAdmin=<DRUPAL_ADMIN_USER>
    export drupalPassword=<DRUPAL_PASSWORD>
    export drupalDBUser=<DRUPAL_DB_USER>
    export adminPassword=<DRUPAL_ADMIN_PASSWORD>
    export drupalPort=<DRUPAL_PORT>
    
    export APN_PASSWORD=<APPLE_PUSH_KEY_PASSWORD>
    
    //start the server
    ./gradlew run
    
    //start the server inside a docker container
    sh docker/fablab-server/create.sh && sh docker/fablab-server/start.sh
    
<b>NOTE : if any environment variable is missing, the server will not start!</b>

The server will listen on port 4433 (SSL) for application requests and port 8081 (SSL) for administrative requests. 
User credentials for the administrative interface, which is protected by a basic HTTP authentication challenge, have to be provided by the environment variables 'adminUsername' and 'adminPassword'. Passwords and configuration (i.e. for OpenErpClient) can also be supplied in directly config.yml.

## Libraries
fablab-server uses the following libraries and software:
* [docker-library/mysql/5.6](https://github.com/docker-library/mysql/tree/1f430aeee538aec3b51554ca9fc66955231b3563/5.6)     License: [GNU GPL Version 2](https://github.com/docker-library/mysql/blob/1f430aeee538aec3b51554ca9fc66955231b3563/LICENSE)
* [jsonrpc2-client:1.15](http://software.dzhuvinov.com/json-rpc-2.0-client.html)        License: [Apache 2.0](http://software.dzhuvinov.com/files/jsonrpc2server/LICENSE.txt)
* [ical4j:1.0.5](https://github.com/ical4j/ical4j)    License: (https://github.com/ical4j/ical4j/blob/master/LICENSE)
* [jackson-dataformat-xml:2.5.1](https://github.com/FasterXML/jackson-dataformat-xml)   License: [Apache 2.0](https://github.com/FasterXML/jackson-dataformat-xml/wiki#licensing)
* [jsoup:1.8.2](http://jsoup.org)  License: [MIT License](http://jsoup.org/license)
* [cron4j:2.2.5](http://www.sauronsoftware.it/projects/cron4j/) License: [LGPL](http://www.sauronsoftware.it/projects/cron4j/)
* [pushy:0.4.3](https://github.com/relayrides/pushy) License: [MIT](https://github.com/relayrides/pushy)

## License
    
## Contact
Feel free to contact us: fablab@i2.cs.fau.de
