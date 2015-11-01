FROM ubuntu:14.04

RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en  
ENV LC_ALL en_US.UTF-8

RUN apt-get update && apt-get install -y --no-install-recommends \
	git \
	openjdk-7-jdk

COPY ../app-server/ /home/fablab/
COPY ../app-common/ /home/fablab/

COPY fablab-rest-server.keystore fablab-server/src/dist/
COPY tuerstatus-appserver.key fablab-server/src/dist/tuerstatus-appserver.key
COPY APN.p12 fablab-server/src/dist/APN.p12
#COPY calendar.ics fablab-server/src/dist/calendar.ics
#COPY rss.xml.rss fablab-server/src/dist/rss.xml.rss
COPY config.yml fablab-server/src/dist/config.yml

WORKDIR fablab-server

# build the server # see other gradle tasks with ./gradew tasks
RUN ./gradlew build

EXPOSE 4433 8081

# run the server
CMD ./docker/checkoutTag.sh && ./gradlew run
