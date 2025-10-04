FROM openjdk:17-jdk-slim

WORKDIR /app
# COPY ./target/demo-0.0.1-SNAPSHOT.jar /app

COPY . .

CMD ["./mvnw","clean","install"]

EXPOSE 8080

CMD ["./mvnw","spring-boot:run"]

# CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]