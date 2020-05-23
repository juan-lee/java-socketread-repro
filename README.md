# Repro for JDK-8075484 : SocketInputStream.socketRead0 can hang even with soTimeout set

[https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8075484](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8075484)

``` bash
docker build . -t <your registry>/java-socketread-repro:latest

docker push <your registry>/java-socketread-repro:latest

kubectl run java-socketread-repro --image=<your registry>/java-socketread-repro:latest --image-pull-policy='Always'

kubectl logs java-socketread-repro
```
