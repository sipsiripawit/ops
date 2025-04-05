FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/repair-service-1.0-SNAPSHOT.jar repair-service.jar
ENTRYPOINT ["java", "-jar", "/repair-service.jar"]
