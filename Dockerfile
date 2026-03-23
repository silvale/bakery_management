# Stage 1: Build dùng Java 25
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Cài đặt Maven thủ công trong Alpine
RUN apk add --no-cache maven

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/bakery-management-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]