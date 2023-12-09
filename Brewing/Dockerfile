FROM openjdk:11
WORKDIR /app
COPY Simulation/start.bat /app/
RUN ["chmod","+x","/app/start.bat"]
RUN /app/start.bat
COPY app.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

ARG JAR_FILE=target/*app.jar
COPY ${JAR_FILE} app.jar
