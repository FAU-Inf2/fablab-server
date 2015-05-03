FROM ubuntu:latest

RUN apt-get update && apt-get install -y --no-install-recommends \
	git \
	openjdk-7-jdk

#build and run the server
CMD git clone https://github.com/FAU-Inf2/fablab-server.git && \
	cd fablab-server && \
	./gradlew run

EXPOSE 8080 8081

