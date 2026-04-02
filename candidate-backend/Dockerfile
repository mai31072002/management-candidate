# Stage 1: Build (Biên dịch code bằng Gradle)
FROM gradle:8.5-jdk17-jammy AS build
WORKDIR /app

# Copy các file cấu hình trước để cache dependencies (giúp build nhanh hơn ở các lần sau)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
RUN ./gradlew dependencies --no-daemon

# Copy toàn bộ code và build file .jar
COPY src ./src
RUN ./gradlew bootJar --no-daemon

# Stage 2: Run (Chỉ lấy file chạy cuối cùng)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy file .jar từ stage build sang (Spring Boot Gradle mặc định để ở build/libs)
COPY --from=build /app/build/libs/*.jar app.jar

# Bảo mật: Chạy bằng user thường, không dùng root (Tiêu chuẩn Bank)
RUN useradd -m devuser
USER devuser

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]