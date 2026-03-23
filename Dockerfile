# Stage 1: Сборка
FROM gradle:8.14-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Собираем проект, пропуская тесты для скорости (потом включим)
RUN gradle build --no-daemon -x test

# Stage 2: Запуск
FROM eclipse-temurin:21-jre
EXPOSE 8080
# Копируем только собранный jar-файл из предыдущего этапа
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]