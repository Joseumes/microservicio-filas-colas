FROM openjdk:17-jdk-slim
COPY target/microservicio-filas-colas.jar /app/microservicio-filas-colas.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/microservicio-filas-colas.jar"]