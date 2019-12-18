FROM openjdk:8-jre-slim

WORKDIR /usr/src/app

COPY target/demo-0.0.1-SNAPSHOT.jar ./

EXPOSE 8122

ENTRYPOINT ["java", "-Xmx512m", "-jar", "/usr/src/app/demo-0.0.1-SNAPSHOT.jar"]
