#!/bin/bash
set -e

CHECKOUT_TAG=""

function usage
{
	echo
	echo Helper script to start docker container
	echo
	echo usage:
	echo -e  \\t -t [tag] checkout specific git tag
	echo
}

# Loop until all parameters are used up
while [ "$1" != "" ]; do
	case $1 in
		-t )
            shift
			if [ -z "$1" ]; then
				echo missing value for parameter -t
				usage
				exit 1
			fi
			CHECKOUT_TAG="-e GIT_CHECKOUT_TAG=$1"
		;;
		* )
			echo wrong parameter: $1
			usage
			exit 1
	esac
	shift
done
echo

NAME="rest-server-"$(date +%Y-%m-%d_%H-%M-%S)
echo "Starting container..."
docker run -d \
    -p 8081:8081 -p 4433:4433 --name="$NAME" \
    --env-file ./env \
    -e hostname="app.fablab.fau.de" \
    $CHECKOUT_TAG \
    -v ~/databaseFiles:/opt/database \
    container-fablab-server
