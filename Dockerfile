FROM maven:3.9-openjdk-23 as builder
COPY . .
RUN mvn clean package -DskipTests

FROM debian:bookworm-slim
COPY --from=build / target/GateInGateOut-0.0.1-snapshot.jar GateInGateOut.jar
expose 8088
CMD ["java", "-jar", "app.jar"]