# Use a base image with Java
FROM amazoncorretto:21-alpine-jdk

# Copy the JAR file into the container
ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

# Expose the port your application listens on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]