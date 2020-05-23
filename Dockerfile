FROM azul/zulu-openjdk:8u131

COPY OneReaderThread.java .
COPY poll.c .

RUN apt-get update && apt-get install -y build-essential

RUN gcc -o poll.so -shared poll.c -ldl -fPIC
RUN javac OneReaderThread.java

ENV LD_PRELOAD=./poll.so
CMD ["java", "-cp", ".", "OneReaderThread"]
