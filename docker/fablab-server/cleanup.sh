#!/bin/bash

#default options

#should images be removed?
CLEAN_IMAGES='no'

#should containers be removed?
CLEAN_CONTAINER='no'

# remove without asking if it is ok?
FORCE='no'

# only remove containers for this 
# FILTER='-f container-fablab-server'
# filter not yet supported

function usage 
{
	echo
	echo Helper script to delete docker files:
	echo
	echo usage:
	echo -e  \\t -a delete everything
	echo -e  \\t -c delete container files
	echo -e  \\t -i delete images
	echo -e  \\t -f force \(don\'t ask if it\'s ok to delete the files\)
	echo 
}

# print usage text, if there aren't any parameters
if [ -z $1 ]; then
	usage
	exit 1
fi

# Loop until all parameters are used up
while [ "$1" != "" ]; do
	case $1 in
		-a )	
			CLEAN_IMAGES='yes'
			CLEAN_CONTAINER='yes'
		;;
		-c )
			CLEAN_CONTAINER='yes'
		;;
		-i )
			CLEAN_IMAGES='yes'
		;;
		-f )
			FORCE='yes'
		;;
		* )	
			echo wrong parameter: $1
			usage
			exit 1
	esac

	shift
done

echo 

if [ "$CLEAN_CONTAINER" == "yes" ]; then
	
	echo Cleaning docker contaier files:
	docker ps -a 

	# ask if FORCE is disabled
	if [ "$FORCE" != "yes" ];then

		echo Is this ok? y=yes
		read -n 1 c
		if [ "$c" != "y" ];then
			echo aborting...
			exit 1;
		fi
	fi
	
	# now delete the container files
	TO_DELETE=$(docker ps -a -q)

	if [ -z $TO_DELETE ];then
		echo nothing to do...
	else
		docker rm $TO_DELETE
	fi
fi

if [ "$CLEAN_IMAGES" == "yes" ]; then
	
	echo Cleaning docker images files:
	docker images

	if [ "$FORCE" != "yes" ];then
		echo Is this ok? y=yes 
		read -n 1 c
		if [ "$c" != "y" ];then
			echo aborting...
			exit 1;
		fi
	fi

	#delete the images
	TO_DELETE=$(docker images -q)
	if [ -z $TO_DELETE ]; then
		echo nothing to do...
	else
		docker rmi $TO_DELETE
	fi
fi
