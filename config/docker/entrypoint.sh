#!/usr/bin/env bash -e

JAVA_OPTS=$JAVA_OPTS
VM_OPTS=$VM_OPTS

echo "================================================================================="
echo ""
echo "Current path: $(pwd)"
echo ""

startService(){
    echo "Start service ${SERVICE_NAME}"
    
    if [ -n $SPRING_ACTIVE_PROFILES ] ; then
        JAVA_OPTS="${JAVA_OPTS} -Dspring.profiles.active=${SPRING_ACTIVE_PROFILES}"
    else 
        JAVA_OPTS="${JAVA_OPTS} -Dspring.profiles.active=default"
    fi
    
    if [ $JMX_ENABLED == "true" ] ; then
        echo "Activating JMX @ ${HOST_NAME}:${JMX_PORT}"
        JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote"
        JAVA_OPTS="${JAVA_OPTS} -Djava.rmi.server.hostname=${HOST_NAME}"
        JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.port=${JMX_PORT}"
        JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.local.only=false"
        JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.authenticate=false"
        JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.ssl=false"
    fi
    
    if [ $REMOTE_DEBUG_ENABLED == "true" ] ; then
        echo "Activating Remote Debug @ ${HOST_NAME}:${REMOTE_DEBUG_PORT}"
        JAVA_OPTS="${JAVA_OPTS} -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=${REMOTE_DEBUG_PORT},suspend=n"
    fi
    
    JAVA_OPTS="${JAVA_OPTS} -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -Dlog4j2.asyncLoggerWaitStrategy=Yield "
    
    JVM_XMS=$JVM_XMS
    if [ ! $JVM_XMS ]; then 
        JVM_XMS=1024m
    fi
    JVM_XMX=$JVM_XMX
    if [ ! $JVM_XMX ]; then 
        JVM_XMX=2048m
    fi
    VM_OPTS=" -server -Xms${JVM_XMS} -Xmx${JVM_XMX} "
    echo "***"
    echo "Executing command: java ${VM_OPTS} ${JAVA_OPTS} -jar ${APP_JAR_PATH}/${APP_JAR_NAME}"
    echo "***"
    java $VM_OPTS $JAVA_OPTS -jar ${APP_JAR_PATH}/${APP_JAR_NAME}
}

echo "================================================================================="

startService

