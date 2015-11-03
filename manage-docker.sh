#!/bin/sh

set -e

IMAGE="app-server"
CONTAINER="${IMAGE}"
SHELL="bash"
ENV_FILE="config.env"
PORTS="-p 8081 -p 4433"
VOLUMES="-v $(pwd)/conf/:/home/fablab/app-server/src/dist/"

if [ $(whoami) != "root" ]; then echo "[i] this script has to be executed as root!" && exit 1; fi

if [ "$1" == "port" ]; then
    docker port "${CONTAINER}"
elif [ "$1" == "build" ]; then
    docker build -t "${IMAGE}" .
elif [ "$1" == "up" ]; then
    if [ ! -e app-server/src/dist/config.yml && ! -e app-server/src/dist/minimumVersion.yml ]; then
        echo "[x] app-server/src/dist/config.yml or app-server/src/dist/minimumVersion.yml is missing"
        exit 1
    else
        docker build -t "${IMAGE}" .
        docker run -d --name="${CONTAINER}" "${PORTS}" "${VOLUMES}" "${IMAGE}"
    fi
elif [ "$1" == "run" ]; then
    docker run -d --name="${CONTAINER}" "${PORTS}" "${VOLUMES}" "${IMAGE}"
elif [ "$1" == "start" ]; then
    docker start "${CONTAINER}"
elif [ "$1" == "stop" ]; then
    docker stop "${CONTAINER}"
elif [ "$1" == "restart" ]; then
    docker restart "${CONTAINER}"
elif [ "$1" == "clean" ]; then
    read -p "Do you really want to delete the container including ALL data? [y/N] "
    if [ "$REPLY" == "y" ]; then
        docker rm -v "${CONTAINER}"
    fi
elif [ "$1" == "shell" ]; then
    docker exec -it "${CONTAINER}" "${SHELL}"
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
