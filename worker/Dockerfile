FROM openjdk:17-alpine

ADD build/libs/worker-*.jar worker.jar

ENTRYPOINT ["java","-jar","worker.jar"]