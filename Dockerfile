FROM amazoncorreto:17-alpine-jdk

COPY target/Springboot-0.0.1-SNAPSHOT.jar app-notes.jar

ENTRYPOINT ["java","-jar","app.jar"]