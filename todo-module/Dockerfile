FROM maven:3.9.16-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY todo-module/pom.xml todo-module/
COPY user-module/pom.xml user-module/
RUN mvn -B -pl todo-module -am dependency:go-offline

COPY todo-module/src todo-module/src
RUN mvn -B -pl todo-module -am clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/todo-module/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
