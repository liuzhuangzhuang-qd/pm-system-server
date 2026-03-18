FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests clean package

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/pm-system-*.jar app.jar

ENV PM_DB_URL=jdbc:mysql://mysql:3306/pm_system?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
ENV PM_DB_USERNAME=root
ENV PM_DB_PASSWORD=root

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

