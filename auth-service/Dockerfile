FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/auth-service-0.0.1-SNAPSHOT.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "/auth-service.jar"]
