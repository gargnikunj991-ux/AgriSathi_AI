FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY agrisathi-backend/pom.xml .

RUN mvn dependency:go-offline -B

COPY agrisathi-backend/src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

RUN mkdir -p /app/logs

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -XX:+ExitOnOutOfMemoryError -Dserver.port=${PORT:-8080} -Dserver.address=0.0.0.0 -jar app.jar"]
