搭建过程

### 1.需要先通过docker搭建es
### 2.搭建opa
```shell
docker run --name skywalking-oap-server \
-e TZ=Asia/Shanghai \
--link elasticsearch8:elasticsearch \
--network elastic \
-e SW_STORAGE=elasticsearch \
-e SW_STORAGE_ES_CLUSTER_NODES=elasticsearch8:9200 \
-e SW_STORAGE_ES_USER=elastic \
-e SW_STORAGE_ES_PASSWORD=V2BPRJTshkotcat9M5hK \
-d -p 12800:12800 -p 11800:11800 apache/skywalking-oap-server:10.0.1
```
### 3.搭建ui界面
```shell
docker run -d --name skywalking-ui \
-e TZ=Asia/Shanghai \
-p 8088:8080 \
--network elastic \
--link skywalking-oap-server:skywalking-oap-server \
-e SW_OAP_ADDRESS=http://skywalking-oap-server:12800 \
apache/skywalking-ui:10.0.1
```

### 4.下载skyWalking Agent
https://archive.apache.org/dist/skywalking/java-agent/8.9.0/apache-skywalking-java-agent-8.9.0.tgz

1. pom文件中引入相关依赖
2. 创建logback.xml文件，并且配置skyWalking
3. 创建对应的接口

### 5.配置vm参数
```text
-javaagent:/Volumes/980PRO/tools/skywalking-agent/skywalking-agent.jar 
        -Dskywalking.agent.service_name=skyWalking-demo 
        -Dskywalking.collector.backend_service=127.0.0.1:11800
```

### 6.调用接口 在 `http://127.0.0.1:8088` 查看