FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y netcat

COPY worker/build/libs/worker-1.0.0-SNAPSHOT.jar worker.jar
COPY rabbit-setup/rabbit-wait.sh rabbit-wait.sh
RUN chmod +x ./rabbit-wait.sh

ENTRYPOINT ["./rabbit-wait.sh", "java","-jar","worker.jar"]