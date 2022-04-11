FROM openjdk:14-jdk


# Working Directory
ARG PROJECT_DIR=/app


RUN mkdir ${PROJECT_DIR}

# Application directory
WORKDIR ${PROJECT_DIR}

# Resources to copy to working dir
COPY ./target/depthchart-*.jar ./app.jar

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]