FROM maven:3.6.3-adoptopenjdk-11 AS MAVEN_BUILDER
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=/opt/spring/app.jar
ARG PORT=8080
COPY --from=MAVEN_BUILDER /tmp/target/*.jar ${JAR_FILE}
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ENV PORT=${PORT}
ENTRYPOINT ["java","-jar","/opt/spring/app.jar"]
