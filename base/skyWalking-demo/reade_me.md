搭建过程

### 1.需要先通过docker搭建es
### 2.搭建opa
```shell
docker run --name skywalking-oap \
-e TZ=Asia/Shanghai \
--link elasticsearch:elasticsearch \
-e SW_STORAGE=elasticsearch \
-e SW_STORAGE_ES_CLUSTER_NODES=elasticsearch:9200 \
-d -p 12800:12800 -p 11800:11800 apache/skywalking-oap-server:8.9.1
```
### 3.搭建ui界面
```shell
docker run -d --name skywalking-ui \
-e TZ=Asia/Shanghai \
-p 8088:8080 \
--link skywalking-oap:skywalking-oap \
-e SW_OAP_ADDRESS=http://skywalking-oap:12800 \
apache/skywalking-ui:8.9.1
```

### 4.下载skyWalking Agent
https://archive.apache.org/dist/skywalking/java-agent/8.9.0/apache-skywalking-java-agent-8.9.0.tgz

### 5.配置vm参数
```java
-javaagent:/Users/longhao/tools/skywalking-agent/skywalking-agent.jar -Dskywalking.agent.service_name=skyWalking-demo -Dskywalking.collector.backend_service=127.0.0.1:11800
```

### 6.调用接口 在 `http://127.0.0.1:8088` 查看