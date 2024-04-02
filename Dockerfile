# Base image used to build image
FROM openjdk:23-slim-bullseye
# Infor about author
LABEL authors="Hoangvuvan"
# Set default working directory
WORKDIR /app
# Copy from your host(pc, laptop...) to container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
# Run this inside the image
RUN ./mvnw dependency:go-offline
# Run build app inside container
CMD ["./mvnw", "spring-boot:run"]
EXPOSE 8080