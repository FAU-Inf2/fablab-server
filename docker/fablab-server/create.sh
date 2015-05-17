#!/bin/sh


if [ ! -f fablab-rest-server.keystore ]; then
	echo "[INFO] - Missing keystore file! looking for it in ../../src/dist"
	if [ ! -f ../../src/dist/fablab-rest-server.keystore ]; then
		echo "[ERROR] - No keystore file found in src/dist. please copy it over from docs repo"
		echo "Quitting...."
		exit
	fi
	echo "[INFO] - Copying keystore file from ../../src/dist to current dir."
	cp ../../src/dist/fablab-rest-server.keystore .
fi

echo "Creating container..."
docker build -t container-fablab-server .
