FROM openjdk:8-jdk-alpine
MAINTAINER sabuj.das@gmail.com

USER root
RUN apk update && apk add bash
RUN apk add --no-cache bash


# Default to UTF-8 file.encoding
ENV LANG C.UTF-8


# Copy generated JAR
COPY maven/@{project.artifactId}-@{project.version}.jar /usr/share/consolefire/@{project.artifactId}/
# Copy logger config
COPY maven/config/logger/log4j2.xml /usr/share/consolefire/@{project.artifactId}/config/logger/log4j2.xml
# Copy entrypoint.sh
COPY maven/scripts/entrypoint.sh /usr/share/consolefire/@{project.artifactId}/run.sh
RUN ["chmod", "+x", "/usr/share/consolefire/@{project.artifactId}/run.sh"]

ENV SERVICE_NAME=@{project.artifactId} \
	APP_JAR_PATH=/usr/share/consolefire/@{project.artifactId} \
	APP_JAR_NAME=@{project.artifactId}-@{project.version}.jar \
	LOGGER_CFG_LOCATION=/usr/share/consolefire/@{project.artifactId}/config/logger \
    LOGGER_CFG_FILE=/log4j2.xml
    

WORKDIR /usr/share/consolefire/@{project.artifactId}


EXPOSE ${SERVER_PORT}

ENTRYPOINT ["/bin/sh"]
CMD ["/usr/share/consolefire/@{project.artifactId}/run.sh"]

