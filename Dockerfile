FROM sbtscala/scala-sbt:graalvm-ce-22.3.0-b2-java17_1.8.3_3.2.2

WORKDIR /VierGewinnt

ADD . /VierGewinnt

CMD sbt run

