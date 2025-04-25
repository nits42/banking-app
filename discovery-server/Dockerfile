# Use an official OpenJDK runtime as a parent image
FROM openjdk:21

# Set the working directory
WORKDIR /opt/src

# Set port number to expose
EXPOSE 8761

# Author
LABEL authors="nitin"

# Copy the JAR file to the container
ADD target/discovery-server-0.0.1-SNAPSHOT.jar /opt/src/discovery-server-0.0.1-SNAPSHOT.jar

# Run the application
ENTRYPOINT [ "java","-jar","discovery-server-0.0.1-SNAPSHOT.jar" ]