# Use a base image with Java runtime
FROM openjdk:17-jdk-alpine

# Set a working directory
WORKDIR /app

# Copy the application JAR to the container
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8086

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
