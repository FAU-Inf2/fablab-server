FROM ubuntu:14.04

RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en  
ENV LC_ALL en_US.UTF-8

RUN apt-get update && apt-get install -y --no-install-recommends \
	git \
	openjdk-7-jdk

RUN mkdir /home/fablab

COPY app-server/ /home/fablab/app-server
COPY app-common/ /home/fablab/app-common

WORKDIR /home/fablab/app-server

RUN ./gradlew build

EXPOSE 4433 8081

CMD ./gradlew run
