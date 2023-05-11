FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/user-service-1.3.jar UserService.jar
ENTRYPOINT ["java","-jar","UserService.jar"]