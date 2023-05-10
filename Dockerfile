FROM hseeberger/scala-sbt:16.0.1_1.5.4_2.13.6

RUN apt-get update && \
    apt-get install -y libxrender1 libxtst6 libxi6 libgl1-mesa-glx libgtk-3-0

WORKDIR /VierGewinnt

ADD . /VierGewinnt

CMD sbt run

