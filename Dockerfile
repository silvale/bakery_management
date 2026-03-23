# Stage 1: Build (Chạy Maven bên trong Docker)
# Dùng image Maven hỗ trợ Java 21 (hoặc 17) tạm thời vì Docker Hub chưa có sẵn Maven-Java-25 chính chủ
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy file cấu hình trước để cache dependencies (tăng tốc build lần sau)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy code và build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run (Chỉ lấy file JAR đã build xong)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/bakery-management-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]