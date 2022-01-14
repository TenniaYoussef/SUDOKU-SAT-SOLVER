FROM openjdk:11.0.6-jdk-slim

MAINTAINER  Youssef TENNIA 

USER root

RUN apt update && apt install -y minisat

ADD src .

ADD samples .

RUN chmod a+x ./*.java 
RUN javac *.java

CMD ["java", "Main"]