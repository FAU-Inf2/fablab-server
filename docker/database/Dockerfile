FROM ubuntu:latest

RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en  
ENV LC_ALL en_US.UTF-8

RUN 	apt-get update && apt-get install -y --no-install-recommends \
	openjdk-7-jdk \
	unzip \
	wget


RUN 	wget http://www.h2database.com/h2-2014-04-05.zip
RUN	unzip h2-2014-04-05.zip
RUN	chmod 755 ./h2/bin/h2.sh

EXPOSE 8082 9092

#build and run the server
CMD	./h2/bin/h2.sh -web -tcp -webAllowOthers -tcpAllowOthers

