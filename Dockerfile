# Build
FROM maven:3.8.6-ibmjava-8 AS build

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

RUN mvn install -Ddependency-check.skip=true

# Run
FROM ibm-semeru-runtimes:open-8-jdk
RUN apt-get update -y
RUN apt-get upgrade -y

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
   LOGGING_PATH=/var/log \
   JAVA_OPTS=""

COPY --from=build /usr/src/app/target/*.jar /usr/app/

EXPOSE 8082

CMD java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /usr/app/*.jar
