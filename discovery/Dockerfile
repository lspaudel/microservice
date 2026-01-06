FROM eclipse-temurin:17-jdk
VOLUME /tmp
EXPOSE 8761
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
