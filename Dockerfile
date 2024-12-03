# Stage 1: Build the application
FROM maven:3.9-openjdk-23 AS build

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create a runtime image
FROM debian:bookworm-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/GateInGateOut-0.0.1-SNAPSHOT.jar GateInGateOut.jar

# Expose the port the application listens on
EXPOSE 8088

# Run the application
CMD ["java", "-jar", "GateInGateOut.jar"]
