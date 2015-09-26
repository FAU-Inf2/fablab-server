FROM ubuntu:latest

RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en  
ENV LC_ALL en_US.UTF-8

RUN 	apt-get update && apt-get install -y --no-install-recommends \
	git \
	openjdk-7-jdk

COPY	fablab-rest-server.keystore fablab-rest-server.keystore
COPY	tuerstatus-appserver.key tuerstatus-appserver.key
COPY	apnDev.p12 apnDev.p12
COPY	calendar.ics calendar.ics
COPY	rss.xml.rss rss.xml.rss
COPY	config.yml config.yml

#build and run the server
CMD		if [ ! -d "fablab-server" ]; then \
			git clone https://github.com/FAU-Inf2/fablab-server.git && \
			git clone https://github.com/FAU-Inf2/fablab-common.git && \
			mv fablab-rest-server.keystore fablab-server/src/dist && \
			mv tuerstatus-appserver.key fablab-server/src/dist && \
			mv apnDev.p12 fablab-server/src/dist && \
			mv calendar.ics fablab-server/src/dist && \
			mv rss.xml.rss fablab-server/src/dist && \
			mv config.yml fablab-server/src/dist; \
		fi && \
		cd fablab-server && \
		./docker/fablab-server/checkoutTag.sh && \
		./gradlew run

EXPOSE 4433 8081
