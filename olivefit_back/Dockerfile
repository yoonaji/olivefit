FROM gradle:8.5.0-jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle bootjar --no-daemon

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]