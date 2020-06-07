FROM adoptopenjdk/openjdk14:alpine-slim
WORKDIR /boardmodule
EXPOSE 8081
ADD target/scala-2.13/BoardModule.jar /boardmodule
CMD java -jar BoardModule.jar