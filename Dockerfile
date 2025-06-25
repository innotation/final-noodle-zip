FROM openjdk:17
LABEL maintainer="admin <inho.mun8063@gmail.com>"
LABEL version="0.0.1"
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]