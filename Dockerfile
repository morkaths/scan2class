# Build stage
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM tomcat:9.0-jdk17-corretto
# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*
# Disable Tomcat shutdown port to prevent "Invalid shutdown command" logs
RUN sed -i 's/port="8005"/port="-1"/g' /usr/local/tomcat/conf/server.xml
# Copy WAR file from build stage to Tomcat webapps directory as ROOT.war
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
