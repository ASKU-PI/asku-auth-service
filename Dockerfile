FROM maven:3.8.3-openjdk-17 AS build

COPY pom.xml .

RUN mvn -B dependency:go-offline

COPY src src

RUN mvn -B package -DskipTests

FROM openjdk:17
COPY --from=build target/asku-auth-service.jar .
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "asku-auth-service.jar"]