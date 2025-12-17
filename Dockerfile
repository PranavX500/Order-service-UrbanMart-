FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy jar into container with a fixed name
COPY target/Order-Service-0.0.1-SNAPSHOT.jar app.jar

# Expose service port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
