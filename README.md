# fablab-server [![Build Status](https://travis-ci.org/FAU-Inf2/fablab-server.svg?branch=master)](https://travis-ci.org/FAU-Inf2/fablab-server)

fablab-server is a REST server based on [Dropwizard](http://www.dropwizard.io) needed for the [fablab-android-App](https://github.com/FAU-Inf2/fablab-android).

## Build

To build and run the server, just type:
    
    //set environment variables needed for admin authentication
    export adminUsername=secret
    export adminPassword=secret
    
    //set environment variables for openerp client
    export openerp_hostname="openerp hostname"
    export openerp_database="openerp database"
    export openerp_user="openerp user"
    export openerp_password="openerp user password"
    
    // private key password for ssl certificate
    export FABLAB_KEYSTORE_PASSWORD="certificate password"
    
    ./gradlew run
    
<b>NOTE : if any environment variable is missing, the server will not start!</b>

The server will listen on port 8080 for application requests and port 8081 for administrative requests. User credentials for the administrative interface, which is protected by a basic HTTP authentication challenge, have to be provided by the environment variables 'adminUsername' and 'adminPassword'. Passwords and configuration (i.e. for OpenErpClient) can also be supplied in directly config.yml.

## Libraries
fablab-server uses the following libraries and software:
* [docker-library/mysql/5.6](https://github.com/docker-library/mysql/tree/1f430aeee538aec3b51554ca9fc66955231b3563/5.6)     License: [GNU GPL Version 2](https://github.com/docker-library/mysql/blob/1f430aeee538aec3b51554ca9fc66955231b3563/LICENSE)
* [jsonrpc2-client:1.15](http://software.dzhuvinov.com/json-rpc-2.0-client.html)        License: [Apache 2.0](http://software.dzhuvinov.com/files/jsonrpc2server/LICENSE.txt)
* [ical4j:1.0.5](https://github.com/ical4j/ical4j)    License: (https://github.com/ical4j/ical4j/blob/master/LICENSE)
* [jackson-dataformat-xml:2.5.1](https://github.com/FasterXML/jackson-dataformat-xml)   License: [Apache 2.0](https://github.com/FasterXML/jackson-dataformat-xml/wiki#licensing)
* [jsoup:1.8.2](http://jsoup.org)  License: [MIT License](http://jsoup.org/license)

## License
    
## Contact
Feel free to contact us: fablab@i2.cs.fau.de
