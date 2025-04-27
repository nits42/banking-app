# Use an official OpenJDK runtime as a parent image
FROM openjdk:21

# Set the working directory
WORKDIR /app

# Set port number to expose
EXPOSE 8084

# Author
LABEL authors="nitin"

# Copy the JAR file to the container
ADD target/user-service-0.0.1-SNAPSHOT.jar /app/user-service-0.0.1-SNAPSHOT.jar

# Run the application
ENTRYPOINT [ "java","-jar","user-service-0.0.1-SNAPSHOT.jar" ]