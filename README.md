# fablab-server

REST server based on [Dropwizard](http://www.dropwizard.io)

## build

To get a fat jar containing all dependencies, just type

    ./gradlew fatJar

The jar is located at `build/libs/fablab-server-fat-0.1.jar`

---

## run

To start the server use the following command
    
    java -jar fablab-server-fat-0.1.jar server hello-fablab.yml 
    
`hello-fablab.yml` is the template file which is located in the root dir of the project

The server will listen on port 8080 for application requests and port 8081 for administrative requests.


    
    