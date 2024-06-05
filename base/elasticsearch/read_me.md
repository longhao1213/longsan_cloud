### 拉取docker镜像
```shell
docker pull elasticsearch:8.13.4
```
### 创建es在docker中的网络
```shell
docker network create elastic
```

### 创建容器
```shell
docker run -d --name elasticsearch8 --restart=always --net elastic --privileged -p 9200:9200 -p 9300:9300 -e ES_JAVA_OPTS="-Xms1g -Xmx2g" -e "discovery.type=single-node" elasticsearch:8.13.4
```

### 进入容器，设置账号密码
```shell
# 先使用root用户进入容器
docker exec -u 0 -it kibana bash
# 更新并且下载vim命令
apt-get update
apt-get install vim
# 修改配置文件
vim /usr/share/elasticsearch/config/elasticsearch.yml
# 取消密码验证，取消https验证
xpack.security.enabled: false
xpack.security.enrollment.enabled: false

# 如果不想取消密码 那么可以通过以下命令来配置获取密码
# 进入容器
docker exec -it elasticsearch8 bash

# 修改密码 有两种方式 auto自动设置密码，interactive 手动设置密码
cd /usr/share/elasticsearch/bin/
./elasticsearch-setup-passwords auto

# 获得相关密码
Changed password for user apm_system
PASSWORD apm_system = 2HTjKDDvsh7F6tqzDoVz

Changed password for user kibana_system
PASSWORD kibana_system = vULliTRWYJ3STuKYW59J

Changed password for user kibana
PASSWORD kibana = vULliTRWYJ3STuKYW59J

Changed password for user logstash_system
PASSWORD logstash_system = 0zmlNCsd68iH8jo4o3QG

Changed password for user beats_system
PASSWORD beats_system = JUr5QgZ3Itk8mGJt9jRr

Changed password for user remote_monitoring_user
PASSWORD remote_monitoring_user = NQjsF3riCVFSoFziYXwX

Changed password for user elastic
PASSWORD elastic = V2BPRJTshkotcat9M5hK
```

### 根据上面的方式，获得了访问用的账号和密码 
账号 elastic
密码 V2BPRJTshkotcat9M5hK

### 安装kibana
```shell
docker pull kibana:8.13.4

docker run -d --name kibana --network elastic -p 5601:5601  -e ELASTICSEARCH_URL=http://elasticsearch8:9200 kibana:8.13.4

# 启动完kibana容器后访问
http://localhost:5601
# 如果上面es安装把https和密码给禁用了 就不用做下面的步骤
# 会提示输入token，使用es来生成token
docker exec -it elasticsearch8 /usr/share/elasticsearch/bin/elasticsearch-create-enrollment-token -s kibana
# 然后会提示输入6位验证码，使用kibana来生成
docker exec -it kibana bin/kibana-verification-code
#然后输入es的账号密码登陆
# kibana设置中文
# 先使用root用户进入容器
docker exec -u 0 -it kibana bash
# 更新并且下载vim命令
apt-get update
apt-get install vim
# 修改配置文件
vim /usr/share/kibana/config/kibana.yml
# 在文件最后添加
i18n.locale: "zh-CN"
# 退出并且重启容器
```

### 访问控制台
http://127.0.0.1:5601