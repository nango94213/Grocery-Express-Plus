# Compile our java files in this container
FROM maven:3.8.5-openjdk-17-slim AS builder
COPY src /app/src/
COPY pom.xml /app/
WORKDIR /app
RUN mvn clean package

FROM openjdk:17-slim AS executor
COPY --from=builder /app/target/delivery-service-system-project-jar-with-dependencies.jar /usr/app/app.jar
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
