# Example of custom Java runtime using jlink in a multi-stage container build
FROM maven:3.9.3-amazoncorretto-20 as jre-build
RUN mkdir /opt/app
EXPOSE 8080
WORKDIR /opt/app