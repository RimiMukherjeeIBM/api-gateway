FROM openjdk:8-jre
FROM maven:alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
   LOGGING_PATH=/var/log \
   JAVA_OPTS=""

# create app directory
RUN mkdir -p /usr/src/app/

# set app as the base directory
WORKDIR /usr/src/app

# copy everything from our root dir to the docker app dir.
COPY . .

RUN mvn initialize

RUN mvn install

EXPOSE 9090

CMD java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar ./target/*-SNAPSHOT.jar
