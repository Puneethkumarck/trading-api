# Step 1: Build the application using Gradle from the root directory
FROM eclipse-temurin:21.0.4_7-jdk AS build

WORKDIR /app

# Copy the Gradle wrapper and necessary files from the root
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY gradle.properties gradle.properties

# Copy the source code for all modules (from root)
COPY . .

# Grant execution permission to the Gradle wrapper
RUN chmod +x gradlew

# Run the Gradle build from the root (including skipping tests if necessary)
RUN ./gradlew clean build --no-daemon -x test

# Step 2: Create a minimal image with JRE and the built app
FROM eclipse-temurin:21.0.4_7-jre

WORKDIR /opt

# Copy the built JAR from the build stage
COPY --from=build /app/trading-app/build/libs/trading-app.jar /opt/app.jar

# Copy the entrypoint script
COPY ./deploy/docker/entrypoint.sh /opt/entrypoint.sh

# Grant execution permission to the entrypoint script
RUN chmod +x /opt/entrypoint.sh

# Expose port 8080
EXPOSE 8080

# Set the entrypoint
ENTRYPOINT ["/opt/entrypoint.sh"]
