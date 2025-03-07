FROM maven:3.9.4-eclipse-temurin-17 as build

COPY src src
COPY pom.xml pom.xml

RUN mvn clean package

FROM bellsoft/liberica-openjdk-debian:17

RUN addUser --system spring-boot && addgroup --system spring-boot && adduser spring-boot
USER spring-boot

WORKDIR /app

COPY --from=build target/TaskManagementService-0.0.1-SNAPSHOT.jar ./application.jar