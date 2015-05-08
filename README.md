# fablab-server [![Build Status](https://travis-ci.org/FAU-Inf2/fablab-server.svg?branch=master)](https://travis-ci.org/FAU-Inf2/fablab-server)

fablab-server is a REST server based on [Dropwizard](http://www.dropwizard.io) needed for the [fablab-android-App](https://github.com/FAU-Inf2/fablab-android).

## Build

To build and run the server, just type:

    export adminUsername=secret
    export adminPassword=secret
    ./gradlew run

The server will listen on port 8080 for application requests and port 8081 for administrative requests. User credentials for the administrative interface, which is protected by a basic HTTP authentication challenge, have to be provided by the environment variables 'adminUsername' and 'adminPassword'.

## Libraries
fablab-server uses the following libraries and software:
* [docker-library/mysql/5.6](https://github.com/docker-library/mysql/tree/1f430aeee538aec3b51554ca9fc66955231b3563/5.6) License: [GNU GPL Version 2](https://github.com/docker-library/mysql/blob/1f430aeee538aec3b51554ca9fc66955231b3563/LICENSE)
* 

## License
    
## Contact
Feel free to contact us: fablab@i2.cs.fau.de
