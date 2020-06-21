FROM adoptopenjdk/openjdk14:alpine-slim AS othello-root
WORKDIR /othello
ADD target/scala-2.13/Othello.jar .
ADD wait-for .
RUN chmod +x wait-for
ENV DOCKER_ENV 'true'

FROM adoptopenjdk/openjdk14:alpine-slim AS boardmodule
WORKDIR /boardmodule
ADD BoardModule/target/scala-2.13/BoardModule.jar .
ADD wait-for .
RUN chmod +x wait-for
ENV DOCKER_ENV 'true'

FROM adoptopenjdk/openjdk14:alpine-slim AS usermodule
WORKDIR /usermodule
ADD UserModule/target/scala-2.13/UserModule.jar .
ADD wait-for .
RUN chmod +x wait-for
ENV DOCKER_ENV 'true'

#FROM mongo:latest AS othello-mongodb
FROM mysql:latest AS othello-mysql
