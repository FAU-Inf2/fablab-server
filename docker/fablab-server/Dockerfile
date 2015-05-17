FROM ubuntu:latest

RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en  
ENV LC_ALL en_US.UTF-8

RUN 	apt-get update && apt-get install -y --no-install-recommends \
	git \
	openjdk-7-jdk

COPY	fablab-rest-server.keystore fablab-rest-server.keystore

#build and run the server
CMD 	git clone https://github.com/FAU-Inf2/fablab-server.git && \
	mv fablab-rest-server.keystore fablab-server/src/dist && \
	cd fablab-server && \
	./gradlew run

EXPOSE 8080 8081 8082
