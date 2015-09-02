#!/bin/sh


if [ ! -f fablab-rest-server.keystore ]; then
	echo "[ERROR] - Missing keystore file!"
	exit
fi

echo "Creating container..."
docker build -t container-fablab-server .
