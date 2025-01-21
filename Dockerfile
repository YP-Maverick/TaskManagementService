FROM amazoncorretto:22-alpine-jdk
# Директория в контейнере для вашего приложения
WORKDIR /app
# Копируем jar-файл приложения в контейнер (предполагается, что jar-файл уже собран)
COPY target/*.jar "TaskManagementService-0.0.1-SNAPSHOT.jar"
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "TaskManagementService-0.0.1-SNAPSHOT.jar"]
