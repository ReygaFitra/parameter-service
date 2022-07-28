FROM maven:3.8.6-jdk-11-slim AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn -Dhttps.proxyHost=192.168.45.105 -Dhttps.proxyPort=8080 -Dserver.address=0.0.0.0 verify --fail-never
ADD . $HOME
RUN mvn -Dhttps.proxyHost=192.168.45.105 -Dhttps.proxyPort=8080 -Dserver.address=0.0.0.0 -Dmaven.test.skip=true clean package

FROM openjdk:11-jdk-slim
MAINTAINER darosy
WORKDIR /app
COPY --from=build /usr/app/target/*.jar ./parameter-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","parameter-service-0.0.1-SNAPSHOT.jar"]