FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/workflow-service-1.0-SNAPSHOT.jar workflow-service.jar
ENTRYPOINT ["java", "-jar", "/workflow-service.jar"]
