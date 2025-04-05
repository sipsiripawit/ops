FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/notification-service-1.0-SNAPSHOT.jar notification-service.jar
ENTRYPOINT ["java", "-jar", "/notification-service.jar"]
