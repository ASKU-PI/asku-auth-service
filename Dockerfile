FROM openjdk:16

ADD ./target/asku-auth-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/asku-auth-service.jar"]

EXPOSE 8888