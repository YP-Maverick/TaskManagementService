FROM amazoncorretto:22-alpine-jdk
ARG JAR_FILE=target/*.jar
COPY ./target/TaskManagementService-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
