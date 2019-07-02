FROM hseeberger/scala-sbt
WORKDIR /othello
ADD . /othello
CMD sbt run
