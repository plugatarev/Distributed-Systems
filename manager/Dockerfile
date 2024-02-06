FROM openjdk:17-alpine

ADD build/libs/manager-*.jar manager.jar

ENTRYPOINT ["java","-jar","manager.jar"]