FROM eclipse-temurin as builder
WORKDIR /app
COPY target/nxt-sts-1.0.0.jar nxt-sts-1.0.0.jar

CMD ["java", "-jar", "nxt-sts-1.0.0.jar" ]
