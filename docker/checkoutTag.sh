#!/bin/bash
set -e
# check if ENV var GIT_CHECKOUT_TAG is set, and checkout specific git tag

if [ ! -z "$1" ]; then
	echo "Using tag from parameter: $1"
	GIT_CHECKOUT_TAG=$1
fi

if [ ! -z "$GIT_CHECKOUT_TAG" ]; then

	echo Checking out git tag \"$GIT_CHECKOUT_TAG\" for server repo...
	git pull
	git checkout $GIT_CHECKOUT_TAG

	PATH_COMMON=""
	if [ -d "../fablab-common" ]; then
		PATH_COMMON="../fablab-common";
	elif [ -d "../../../fablab-common" ]; then
	    PATH_COMMON="../../../fablab-common";
	fi

	if [ ! -z "$PATH_COMMON" ]; then
		echo Checking out git tag \"$GIT_CHECKOUT_TAG\" for common repo...
		cd $PATH_COMMON;
		git pull
		git checkout $GIT_CHECKOUT_TAG
	fi
fi
