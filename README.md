# Jdbc JSON Extension 
`jdbc-json-extension` is to fetch data from RDBMS in JSON format. The response data is structured the in the same relation structure of the Tables relationships in the database. The fetch is pre-planned using a `FetchPlan` configuration. The execution is multi-threaded tree pattern where it starts from the `root` table with supplied filter parameters. The subsequent nodes (tables) are fetched as the `One-to-*` relationship defined from the root tables.

__Release Version: `1.2.0`__

__Maven Dependency__

`pom.xml`

```xml
<properties>
	<jdbc.json.extension.version>1.2.0</jdbc.json.extension.version>
</properties>
<dependencies>
	<dependency>
		<groupId>com.consolefire</groupId>
		<artifactId>jdbc-json-extension-common</artifactId>
		<version>${jdbc.json.extension.version}</version>
	</dependency>
	<dependency>
		<groupId>com.consolefire</groupId>
		<artifactId>jdbc-json-extension-core</artifactId>
		<version>${jdbc.json.extension.version}</version>
	</dependency>
</dependencies>
```


## The Intent
__Given__
- Sakila's Film Database.
- Fetch a Film (by ID) with its Categoris and Actors data
- Table Structure:
    
```
film
 |- film_actor (joins with film.film_id)
 |   |- actor (joins with film_actor.actor_id)
 |- film_category (joins with film.film_id)
 |   |- category (joins with film_category.category_id)
```
    
__Query:__  

*Fetch* `film` for `ID = 100`

__Expected Response:__

```javascript
{
  "body": {
    "film": {
      "special_features": "Commentaries",
      "rental_duration": 7,
      "rental_rate": 4.99,
      "filmCategories": [
        {
          "category_id": 9,
          "last_update": "2006-02-15T05:07:09.000+0000",
          "film_id": 100,
          "category": {
            "category_id": 9,
            "last_update": "2006-02-15T04:46:27.000+0000",
            "name": "Foreign"
          }
        }
      ],
      "length": 161,
      "rating": "R",
      "release_year": "2006-01-01",
      "replacement_cost": 21.99,
      "description": "A Beautiful Drama of a Dentist And a Composer who must Battle a Sumo Wrestler in The First Manned Space Station",
      "language_id": 1,
      "title": "BROOKLYN DESERT",
      "inventory": {
        "store_id": 1,
        "inventory_id": 451,
        "last_update": "2006-02-15T05:09:17.000+0000",
        "film_id": 100
      },
      "filmActors": [
        {
          "actor": {
            "last_update": "2006-02-15T04:34:33.000+0000",
            "last_name": "DEGENERES",
            "actor_id": 41,
            "first_name": "JODIE"
          },
          "last_update": "2006-02-15T05:05:03.000+0000",
          "actor_id": 41,
          "film_id": 100
        },
        {
          "actor": {
            "last_update": "2006-02-15T04:34:33.000+0000",
            "last_name": "NEESON",
            "actor_id": 62,
            "first_name": "JAYNE"
          },
          "last_update": "2006-02-15T05:05:03.000+0000",
          "actor_id": 62,
          "film_id": 100
        },
        {
          "actor": {
            "last_update": "2006-02-15T04:34:33.000+0000",
            "last_name": "GUINESS",
            "actor_id": 90,
            "first_name": "SEAN"
          },
          "last_update": "2006-02-15T05:05:03.000+0000",
          "actor_id": 90,
          "film_id": 100
        },
        {
          "actor": {
            "last_update": "2006-02-15T04:34:33.000+0000",
            "last_name": "NOLTE",
            "actor_id": 125,
            "first_name": "ALBERT"
          },
          "last_update": "2006-02-15T05:05:03.000+0000",
          "actor_id": 125,
          "film_id": 100
        },
        {
          "actor": {
            "last_update": "2006-02-15T04:34:33.000+0000",
            "last_name": "WILLIAMS",
            "actor_id": 172,
            "first_name": "GROUCHO"
          },
          "last_update": "2006-02-15T05:05:03.000+0000",
          "actor_id": 172,
          "film_id": 100
        }
      ],
      "original_language_id": null,
      "film_id": 100
    }
  }
}
```

## Configurations
The required configurations are `DataSource` and `FetchPlan`

#### DataSource Configuration:

```javascript
{
  "name": "SAKILA_LOCAL_MYSQL",
  "connection": {
    "driverClass": "com.mysql.cj.jdbc.Driver",
    "jdbcUrl": "jdbc:mysql://localhost:3306/sakila?allowPublicKeyRetrieval=true&useUnicode=true&autoReconnect=true&verifyServerCertificate=false&useSSL=false&serverTimezone=UTC&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false",
    "userName": "root",
    "password": "sakila",
    "databaseName": "sakila",
    "enablePooling": true,
    "pool": {
      "initialSize": 7,
      "minActive": 7,
      "maxActive": 25,
      "testOnBorrow": true,
      "testQuery": "SELECT 1"
    }
  }
}
```

#### Fetch Plan
```javascript
{
  "name": "SAKILA.FETCH_FILMS_BY_ID",
  "qualifiers": [],
  "fetch": {
    "root": "film",
    "parameters": [
      "film_id"
    ]
  },
  "enableJoin": false,
  "databaseMetaData": {
    "schema": "sakila",
    "tables": [
      {
        "name": "film",
        "columns": [
          {
            "name": "film_id"
          },
          {
            "name": "title"
          },
          {
            "name": "description"
          },
          {
            "name": "release_year"
          },
          {
            "name": "language_id"
          },
          {
            "name": "original_language_id"
          },
          {
            "name": "rental_duration"
          },
          {
            "name": "rental_rate"
          },
          {
            "name": "length"
          },
          {
            "name": "replacement_cost"
          },
          {
            "name": "rating"
          },
          {
            "name": "special_features"
          }
        ],
        "references": {
          "filmActors": {
            "collection": true,
            "table": "film_actor",
            "column": "film_id",
            "referenceTo": "film_id"
          },
          "filmCategories": {
            "collection": true,
            "table": "film_category",
            "column": "film_id",
            "referenceTo": "film_id"
          },
          "inventory": {
            "collection": false,
            "table": "inventory",
            "column": "film_id",
            "referenceTo": "film_id"
          }
        }
      },
      {
        "name": "film_actor",
        "columns": [
          {
            "name": "actor_id"
          },
          {
            "name": "film_id"
          },
          {
            "name": "last_update"
          }
        ],
        "references": {
          "actor": {
            "collection": false,
            "table": "actor",
            "column": "actor_id",
            "referenceTo": "actor_id"
          }
        }
      },
      {
        "name": "actor",
        "columns": [
          {
            "name": "actor_id"
          },
          {
            "name": "first_name"
          },
          {
            "name": "last_name"
          },
          {
            "name": "last_update"
          }
        ]
      },
      {
        "name": "film_category",
        "columns": [
          {
            "name": "film_id"
          },
          {
            "name": "category_id"
          },
          {
            "name": "last_update"
          }
        ],
        "references": {
          "category": {
            "collection": false,
            "table": "category",
            "column": "category_id",
            "referenceTo": "category_id"
          }
        }
      },
      {
        "name": "category",
        "columns": [
          {
            "name": "name"
          },
          {
            "name": "category_id"
          },
          {
            "name": "last_update"
          }
        ]
      },
      {
        "name": "inventory",
        "columns": [
          {
            "name": "inventory_id"
          },
          {
            "name": "film_id"
          },
          {
            "name": "store_id"
          },
          {
            "name": "last_update"
          }
        ]
      }
    ]
  }
}
```



## Library Modules
 - Common `jdbc-json-extension-common`: defines interfaces and abstractions
 - Core `jdbc-json-extension-core`: implements the execution and Response in JSON

#### Build
__Maven__
The default `maven` profile builds the library modules (`common` and `core`)
Execute `./mvnw clean install -Psample` from the project root.

## Sample API
Added a `spring-boot` application for sample *configuration* and *API* for executing the planned fetch pattern.
- Module: `jdbc-json-extension-sample` 
- JAVA Version: `1.8`
- Database: `MySQL 8 (sakila's Film database)`
- Default Server Port: `17070`
- API: `http://<host>:<port>/api/data?ds=<datasource>&fp=<fetchplan>&param=<query param>&value=<param value>`

### Build
#### Maven
The sample is part of maven profile `sample`
Execute `./mvnw clean install -Psample` from the project root.

#### Docker
The sample is part of maven profile `docker`. This usages 
Execute `./mvnw clean install -Pdocker` from the project root.
This will create a docker image for the sample

__`Dockerfile` template__


```ruby
FROM openjdk:8-jdk-alpine
MAINTAINER sabuj.das@gmail.com

USER root
RUN apk update && apk add bash
RUN apk add --no-cache bash


# Default to UTF-8 file.encoding
ENV LANG C.UTF-8


# Copy generated JAR
COPY maven/jdbc-json-extension-sample-1.2.0.jar /usr/share/consolefire/jdbc-json-extension-sample/
# Copy logger config
COPY maven/config/logger/log4j2.xml /usr/share/consolefire/jdbc-json-extension-sample/config/logger/log4j2.xml
# Copy entrypoint.sh
COPY maven/scripts/entrypoint.sh /usr/share/consolefire/jdbc-json-extension-sample/run.sh
RUN ["chmod", "+x", "/usr/share/consolefire/jdbc-json-extension-sample/run.sh"]

ENV SERVICE_NAME=jdbc-json-extension-sample \
	APP_JAR_PATH=/usr/share/consolefire/jdbc-json-extension-sample \
	APP_JAR_NAME=jdbc-json-extension-sample-1.2.0.jar \
	LOGGER_CFG_LOCATION=/usr/share/consolefire/jdbc-json-extension-sample/config/logger \
    LOGGER_CFG_FILE=/log4j2.xml
    

WORKDIR /usr/share/consolefire/jdbc-json-extension-sample

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["/bin/sh"]
CMD ["/usr/share/consolefire/jdbc-json-extension-sample/run.sh"]

```

### Boot Run
  Execute `./bootrun` from the project root.
*OR*
  Execute `./mvnw spring-boot:run -Psample` from the project root.
  
### Docker Compose
This sample contains a container definition with MySql database. Also contains a shell script to start/stop the containers.

*__Compose File__*


```yaml
version: "3.3"
networks:
  jdbc-json-ext_bridge:
    driver: bridge
    ipam:
     config:
       - subnet: 192.128.0.0/24
services:
  jdbc-json-ext-mysql:
    image: restsql/mysql-sakila
    restart: always
    hostname: jdbc-json-ext-mysql
    networks:
      jdbc-json-ext_bridge:
        ipv4_address: 192.128.0.10
    ports:
      - 23307:3306
    
  jdbc-json-ext-api:
    image: consolefire/jdbc-json-ext:1.2.0
    restart: always
    hostname: jdbc-json-ext-api
    networks:
      jdbc-json-ext_bridge:
        ipv4_address: 192.128.0.20
    ports:
      - 17077:7070
    volumes:
      - type: bind
        source: ./mysql-sakila
        target: /opt/consolefire/jdbc-json-ext
    environment:
      - SERVER_PORT=7070
      - CXT_CONFIG_CLASSPATH=false
      - CXT_CONFIG_DATA_SOURCE=/opt/consolefire/jdbc-json-ext/context/datasource.cfg.json
      - CXT_CONFIG_FETCH_PLAN=/opt/consolefire/jdbc-json-ext/context/fetchplan.cfg.json
```

__`container.sh`__

Use: `$ ./container.sh [ start | stop | restart | list | tail | status | display ]`


*Options*

- `start` : Starts the complete container in detached mode
- `stop` : Stops and remove the complete container
- `restart <service_name>` : Restart specified container only
- `restart` : Restarts all the containers
- `list` : List all the services in the container
- `status` : Status of all the services in the container
- `tail <service_name>` : Tail the logs of the service
- `display` : Shows the top output for all the services

The sample starts at `7070` in the container [`192.128.0.20`]. This port is mapped to `17077` to the host.



