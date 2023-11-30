FROM jtl-tkgiharbor.hq.bni.co.id/wss-dev/maven:3.8.6-jdk-11-slim AS build
ENV HOME=/usr/app
RUN mkdir -p "$HOME"
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn -Dhttps.proxyHost=192.168.45.105 -Dhttps.proxyPort=8080 -Dserver.address=0.0.0.0 verify --fail-never
#RUN mvn verify --fail-never
ADD . $HOME
RUN mvn -Dhttps.proxyHost=192.168.45.105 -Dhttps.proxyPort=8080 -Dserver.address=0.0.0.0 -Dmaven.test.skip=true clean package
#RUN mvn -Dmaven.test.skip=true clean package

FROM jtl-tkgiharbor.hq.bni.co.id/wss-dev/eclipse-temurin:11-jre-ubi9-minimal
LABEL org.opencontainers.image.authors="darosy"
WORKDIR /app
COPY --from=build /usr/app/target/*.jar ./parameter-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","parameter-service-0.0.1-SNAPSHOT.jar"]
