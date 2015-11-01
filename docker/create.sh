#!/bin/sh
set -e

if [ ! -f fablab-rest-server.keystore ]; then
	echo "[ERROR] - Missing keystore file!"
	exit 1
fi

echo "[i] Copying config file from ../config.yml into the container. (TODO: should we do it this way?)"
cp ../config.yml .

echo "[i] Creating container..."
docker build -t container-fablab-server .

echo "[i] Deleting ./config.yml as it only was copied to build the container. (TODO: should we do it this way?)"
rm ./config.yml
