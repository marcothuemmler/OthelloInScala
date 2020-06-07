FROM adoptopenjdk/openjdk14:alpine-slim
WORKDIR /usermodule
EXPOSE 8081
ADD target/scala-2.13/UserModule.jar /usermodule
CMD java -jar UserModule.jar
