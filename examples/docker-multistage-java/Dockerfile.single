FROM maven:3.5-jdk-8

COPY src /usr/src/myapp/src
COPY pom.xml /usr/src/myapp
RUN mvn -f /usr/src/myapp/pom.xml clean package

ENV WILDFLY_VERSION 10.1.0.Final
ENV WILDFLY_HOME /usr

RUN cd $WILDFLY_HOME && curl http://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz | tar zx && mv $WILDFLY_HOME/wildfly-$WILDFLY_VERSION $WILDFLY_HOME/wildfly

RUN cp /usr/src/myapp/target/people-1.0-SNAPSHOT.war $WILDFLY_HOME/wildfly/standalone/deployments/people.war

EXPOSE 8080

CMD ["/usr/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
