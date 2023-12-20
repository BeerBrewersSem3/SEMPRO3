FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar Brewing.jar
EXPOSE 8080
EXPOSE 4840
ENTRYPOINT ["java", "-jar", "Brewing.jar"]





