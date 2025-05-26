FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/wallet-service-0.0.1-SNAPSHOT.jar wallet-app.jar
ENTRYPOINT ["java", "-jar", "wallet-app.jar"]