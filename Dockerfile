# Stage 1: Build (Dùng JDK 21 để build vì Maven 3.9 tương thích cực tốt)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy file cấu hình trước để cache dependencies (tăng tốc build lần sau)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy code và build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime (Dùng đúng JDK 25 để chạy)
FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app

# Copy file jar từ stage build sang
COPY --from=build /app/target/bakery-management-*.jar app.jar

# Port mà ứng dụng của bạn chạy (mặc định Spring Boot là 8080)
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]