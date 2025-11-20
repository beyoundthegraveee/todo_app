FROM eclipse-temurin:21-jdk-alpine
COPY target/task-0.0.1-SNAPSHOT.jar todo-app.jar
ENTRYPOINT ["java", "-jar", "/todo-app.jar"]

