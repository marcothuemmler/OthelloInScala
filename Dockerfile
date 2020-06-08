FROM adoptopenjdk/openjdk14:alpine-slim AS othello-root
WORKDIR /othello
EXPOSE 8080
ADD target/scala-2.13/Othello.jar /othello
CMD java -jar Othello.jar
ENV DOCKER_ENV 'true'

FROM adoptopenjdk/openjdk14:alpine-slim AS boardmodule
WORKDIR /boardmodule
EXPOSE 8081
ADD BoardModule/target/scala-2.13/BoardModule.jar /boardmodule
CMD java -jar BoardModule.jar

FROM adoptopenjdk/openjdk14:alpine-slim AS usermodule
WORKDIR /usermodule
EXPOSE 8082
ADD UserModule/target/scala-2.13/UserModule.jar /usermodule
CMD java -jar UserModule.jar
