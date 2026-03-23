# Sử dụng trực tiếp JRE 25 để chạy (Bản này đã có sẵn trên Docker Hub)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copy file jar đã build từ thư mục target vào container
COPY target/bakery-management-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]