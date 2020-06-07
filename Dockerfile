FROM adoptopenjdk/openjdk14:alpine-slim
WORKDIR /othello
EXPOSE 8080
ADD target/scala-2.13/Othello.jar /othello
CMD java -jar Othello.jar
