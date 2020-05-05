FROM gradle:jdk11
LABEL author="Sofus Albertsen"
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowjar
RUN cp /home/gradle/src/build/libs/app-*-all.jar app.jar
EXPOSE 8000
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar app.jar
