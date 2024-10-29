FROM openjdk:17

ARG JAR_FILE_PATH=build/libs/*.jar

WORKDIR /apps

COPY $JAR_FILE_PATH app.jar

EXPOSE 8082

CMD ["java", "--enable-preview", "-jar", "app.jar"]
