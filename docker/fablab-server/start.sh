#!/bin/sh

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
    -e openerp_hostname=$openerp_hostname \
    -e openerp_database=$openerp_database \
    -e openerp_user=$openerp_user \
    -e openerp_password=$openerp_password \
    -e adminUsername=$adminUsername \
    -e adminPassword=$adminPassword \
    -e FABLAB_KEYSTORE_PASSWORD="$FABLAB_KEYSTORE_PASSWORD" \
    -e FABLAB_REST_SERVER_TYPE="$FABLAB_REST_SERVER_TYPE" \
    -e pushAPIkey="$pushAPIkey" \
    -e pushServiceURL="$pushServiceURL" \
    -e APN_PASSWORD=$APN_PASSWORD \
    -e hostname=$hostname \
    -e admin_port=$admin_port \
    -e USER_ADMIN=$USER_ADMIN \
    -e PASSWORD_ADMIN=$PASSWORD_ADMIN \
    -e USER_INVENTORY=$USER_INVENTORY \
    -e PASSWORD_INVENTORY=$PASSWORD_INVENTORY \
    -e apiKey=$apiKey \
    -e GITHUB_TOKEN=$GITHUB_TOKEN \
    $CHECKOUT_TAG \
    --link fablab-db:db \
    -v ~/databaseFiles:/opt/database \
    container-fablab-server
