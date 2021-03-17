FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=/opt/spring/app.jar
ARG PORT=8080
COPY ./target/*.jar ${JAR_FILE}
ENV PORT=${PORT}
ENTRYPOINT ["java","-jar","/opt/spring/app.jar"]
