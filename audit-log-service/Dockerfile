FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/audit-log-service-1.0-SNAPSHOT.jar audit-log-service.jar
ENTRYPOINT ["java", "-jar", "/audit-log-service.jar"]
