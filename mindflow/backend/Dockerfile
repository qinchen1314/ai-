FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace
COPY . .
RUN mvn -pl mindflow-api -am package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /workspace/mindflow-api/target/mindflow-api-0.1.0-SNAPSHOT.jar /app/mindflow-api.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/mindflow-api.jar"]
