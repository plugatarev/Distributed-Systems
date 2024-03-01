FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y netcat

COPY manager/build/libs/manager-1.0.0-SNAPSHOT.jar manager.jar
COPY rabbit-setup/rabbit-wait.sh rabbit-wait.sh
RUN chmod +x ./rabbit-wait.sh

ENTRYPOINT ["./rabbit-wait.sh", "java","-jar","manager.jar"]