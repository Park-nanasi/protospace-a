FROM gradle:8.8-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:21-alpine
COPY --from=build /app/build/libs/protospace-a-0.0.1-SNAPSHOT.jar protospace.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "protospace.jar", "--spring.profiles.active=prod", "--debug"]