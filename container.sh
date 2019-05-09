#!/bin/bash

DIR=$(pwd)
CLUSTER_HOME=$DIR/compose
PROJECT_NAME=jdbc-json-ext
NETWORK_NAME=bridge
COMPOSE_FILE=jdbc-json-ext.compose.yml


start(){
	docker-compose -p $PROJECT_NAME -f $CLUSTER_HOME/$COMPOSE_FILE up -d
	echo "Sleepng for 10 seconds ..."
	sleep 10
}

stop(){
	docker-compose -p $PROJECT_NAME -f $CLUSTER_HOME/$COMPOSE_FILE down
}

top(){
	docker-compose -p $PROJECT_NAME -f $CLUSTER_HOME/$COMPOSE_FILE top
}

tail(){
	NAME=$1
	docker-compose -p $PROJECT_NAME -f $CLUSTER_HOME/$COMPOSE_FILE logs -f $NAME
}

list(){
	docker-compose -p $PROJECT_NAME -f $CLUSTER_HOME/$COMPOSE_FILE config --services
}

status(){
	docker-compose -p $PROJECT_NAME -f $CLUSTER_HOME/$COMPOSE_FILE ps
}

restart(){
	NAME=$1
	docker-compose -p $PROJECT_NAME -f $CLUSTER_HOME/$COMPOSE_FILE rm -f -s -v $NAME
	docker-compose -p $PROJECT_NAME -f $CLUSTER_HOME/$COMPOSE_FILE up -d $NAME
	echo "Sleepng for 10 seconds ..."
	sleep 10
}

OPTION=$1

case $OPTION in
start)
    echo "Starting containers for ${PROJECT_NAME} in detached mode"
    start
    ;;
stop)
    echo "Stopping all containers for ${PROJECT_NAME}"
    stop
    ;;
restart)
    echo "Restarting $2...!!"
    restart $2
    ;;
display)
	echo "Containers in ${PROJECT_NAME}"
	top
	;;
tail)
	tail $2
	;;
list)
	list
	;;
status)
	status
	;;
*)
    echo "Use one of [ start | stop | restart | list | tail | status | display ]"
    echo "-- Options --"
    echo "start : Starts the complete container in detached mode"
    echo "stop : Stops and remove the complete container"
    echo "restart <service_name> : Restart specified container only"
    echo "restart : Restarts all the containers"
    echo "list : List all the services in the container"
    echo "status : Status of all the services in the container"
    echo "tail <service_name> : Tail the logs of the service"
    echo "display : Shows the top output for all the services"
esac
