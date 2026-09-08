FROM maven:3.9.16-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY module-todo/pom.xml module-todo/
COPY module-user/pom.xml module-user/
COPY module-common/pom.xml module-common/
RUN mvn -B -pl module-user -am dependency:go-offline

COPY module-user/src module-user/src
COPY module-common/src module-common/src
RUN mvn -B -pl module-user -am clean package -DskipTests

FROM eclipse-temurin:21-jre AS extractor
WORKDIR /app
COPY --from=builder /app/module-user/target/*.jar /app/app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=extractor /app/extracted/dependencies/ ./
COPY --from=extractor /app/extracted/spring-boot-loader/ ./
COPY --from=extractor /app/extracted/snapshot-dependencies/ ./
COPY --from=extractor /app/extracted/application/ ./
EXPOSE 8078
ENTRYPOINT ["java", "-jar", "app.jar"]
