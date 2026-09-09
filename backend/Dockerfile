# Базовый образ с Java 17 (Eclipse Temurin)
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта
COPY pom.xml .
COPY src ./src

# Устанавливаем Maven и собираем JAR
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests && \
    mv target/*.jar app.jar && \
    apk del maven

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]