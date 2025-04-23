# Use an official Tomcat base image
FROM tomcat:10-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY /target/rxflow-backend.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]