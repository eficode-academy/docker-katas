FROM maven:3.5-jdk-8 as BUILD

COPY src /usr/src/myapp/src
COPY pom.xml /usr/src/myapp
RUN mvn -f /usr/src/myapp/pom.xml package

FROM jboss/wildfly:10.1.0.Final

COPY --from=BUILD /usr/src/myapp/target/people-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/people.war
