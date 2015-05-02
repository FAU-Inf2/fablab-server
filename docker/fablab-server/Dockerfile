FROM ubuntu:latest

RUN apt-get -y update
RUN apt-get install -y --no-install-recommends git openjdk-7-jdk

#clone the server repo
RUN git clone https://github.com/FAU-Inf2/fablab-server.git

#build and run the server
CMD cd fablab-server && ./gradlew run

EXPOSE 8080 8081

