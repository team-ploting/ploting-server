# build stage
FROM gradle:7.6.4-jdk17 AS build

WORKDIR /apps
COPY . .
RUN gradle clean build --no-daemon -x test


# runtime stage
FROM openjdk:jre-alpine

WORKDIR /apps
COPY --from=build /apps/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
