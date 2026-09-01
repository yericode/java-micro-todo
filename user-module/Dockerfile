FROM maven:3.9.16-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY todo-module/pom.xml todo-module/
COPY user-module/pom.xml user-module/
RUN mvn -B -pl user-module -am dependency:go-offline

COPY user-module/src user-module/src
RUN mvn -B -pl user-module -am clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/user-module/target/*.jar /app/app.jar
EXPOSE 8078
ENTRYPOINT ["java", "-jar", "app.jar"]
