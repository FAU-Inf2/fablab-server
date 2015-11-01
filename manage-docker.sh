#!/bin/sh

set -e

if [ $(whoami) != "root" ]; then echo "[i] this script has to be executed as root!" && exit 1; fi

if [ "$1" == "port" ]; then
    docker port app-server
elif [ "$1" == "build" ]; then
    docker build -t app-server .
elif [ "$1" == "up" ]; then
    if [ ! -e config.env ]; then
        echo "[x] config.env is missing"
        exit 1
    else
        docker build -t app-server .
        docker run -d --name=app-server -p 8081 -p 4433 --env-file=config.env app-server
    fi
elif [ "$1" == "run" ]; then
    docker run -d --name=app-server -p 8081 -p 4433 --env-file=config.env app-server
elif [ "$1" == "start" ]; then
    docker start app-server
elif [ "$1" == "stop" ]; then
    docker stop app-server
elif [ "$1" == "restart" ]; then
    docker restart app-server
elif [ "$1" == "clean" ]; then
    read -p "Do you really want to delete the container including ALL data? [y/N] "
    if [ "$REPLY" == "y" ]; then
        docker rm -v app-server
    fi
elif [ "$1" == "shell" ]; then
    docker exec -it app-server bash
else
    echo "Usage: manage.sh [COMMAND]"
    echo ""
    echo "Commands:"
    echo "    up       build and run"
    echo "    run      run an already built container"
    echo "    start    start an already created container"
    echo "    stop     stop a running container"
    echo "    restart  stop + start"
    echo "    build    build a new container"
    echo "    clean    remove all persistent data"
    echo "    shell    attach to running container and run a shell"
    echo "    port     query the port where the appserver is bound to"
fi
