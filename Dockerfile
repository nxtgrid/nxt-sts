FROM eclipse-temurin:11-jdk-jammy as builder
WORKDIR /app
COPY target/jambu-1.0-SNAPSHOT.jar jambu-1.0-SNAPSHOT.jar

CMD ["java", "-jar", "jambu-1.0-SNAPSHOT.jar" ]