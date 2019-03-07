FROM maven:3.6-jdk-8-slim

# First the maven file, that changes not so often. This way dependencies aren't re-downloaded on every build

ADD pom.xml /tmp
RUN mkdir /tmp/legalwhen-core && mkdir /tmp/legalwhen-rest
ADD legalwhen-core/pom.xml /tmp/legalwhen-core
ADD legalwhen-rest/pom.xml /tmp/legalwhen-rest/pom.xml

RUN mkdir /var/maven/ && chmod -R 777 /var/maven  && umask 0777 /var/maven
ENV MAVEN_CONFIG /var/maven/
#RUN cd /tmp && mvn -B -e -C -T 1C -Dexcludes=biz.poolparty.learning* -Duser.home=/var/maven org.apache.maven.plugins:maven-dependency-plugin:3.0.2:go-offline

#ADDED
#ENV CLASSPATH .;./tmp;./var/maven/;./var/maven/.m2;./tmp/legalwhen-rest;./tmp/legalwhen-core;

#RUN cd /tmp/legalwhen-core && mvn -B -e -C -T 1C -Duser.home=/var/maven org.apache.maven.plugins:maven-dependency-plugin:3.0.2:go-offline
ENV MAVEN_CONFIG /var/maven/.m2
#ENV MAVEN_OPTS="-Dmaven.repo.local=/var/maven/.m2"

#RUN cd /tmp/legalwhen-rest && mvn -o -B -C -T 1C -Dexcludeartifactids=oeg.legalwhen:legalwhen-core -Duser.home=/var/maven org.apache.maven.plugins:maven-dependency-plugin:3.0.2:go-offline


RUN mkdir /app
ADD pom.xml /app
RUN cd /app && mkdir ./legalwhen-core && chmod -R 777 ./legalwhen-core
RUN cd /app && mkdir ./legalwhen-rest && chmod -R 777 ./legalwhen-rest
ADD legalwhen-core /app/legalwhen-core
ADD legalwhen-rest /app/legalwhen-rest

WORKDIR /app

#RUN mvn -Duser.home=/var/maven clean
RUN mvn -Duser.home=/var/maven -Dmaven.repo.local=/var/maven/.m2 -f pom.xml install -DskipTests

EXPOSE 8080

##CMD mvn -Duser.home=/var/maven -f legalwhen-rest/pom.xml spring-boot:run

WORKDIR /app/legalwhen-rest/
CMD ["java", "-jar", "target/legalwhen-rest-0.1.0.jar"]
