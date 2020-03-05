# Docker下Nginx+Tomcat实现负载均衡

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200301205634645.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3p5cTM1Nw==,size_16,color_FFFFFF,t_70)

#### 1.在宿主机安装docker并启动

#### 2.拉取nginx、tomcat镜像。

#### 3.启动tomcat镜像并创建新首页

```
docker run -d -p 8081:8080 --name tomcat1 tomcat镜像ID
docker run -d -p 8082:8080 --name tomcat2 tomcat镜像ID
docker run -d -p 8083:8080 --name tomcat3 tomcat镜像ID
```

##### 配置新首页

vi index1.jsp
hello world

vi index2.jsp
hello world2

vi index3.jsp``
hello world3



##### 三个容器分别执行

```
docker cp index1.jsp tomcat1:/usr/local/tomcat/webapps/ROOT/index.jsp
docker cp index2.jsp tomcat2:/usr/local/tomcat/webapps/ROOT/index.jsp
docker cp index3.jsp tomcat3:/usr/local/tomcat/webapps/ROOT/index.jsp
```

#### 

#### 4.启动nginx容器

```
docker run -p 82:80 --name nginx1 -d daocloud.io/library/nginx
```

####

#### 5.修改nginx配置文件

##### 进入nginx1

```
docker exec -it nginx1 bash
```

##### 进入配置文件

```
cd etc
cd nginx
vim nginx.conf
```

##### 如果没有vim编辑器的话可以下载插件

```
apt-get  update
apt-get install vim
```

##### nginx.conf配置文件：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200301200426737.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3p5cTM1Nw==,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200301200506496.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3p5cTM1Nw==,size_16,color_FFFFFF,t_70)

##### 其中以下为新增部分

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200301201148410.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3p5cTM1Nw==,size_16,color_FFFFFF,t_70)

##### 退出nginx.conf并保存

```
docker commit 容器ID nginx1
```

##### 查看容器

```
CONTAINER ID        IMAGE                       COMMAND                  CREATED             STATUS              PORTS                    NAMES
b07acfee03d2        daocloud.io/library/nginx   "nginx -g 'daemon of…"   22 minutes ago      Up 4 seconds        0.0.0.0:82->80/tcp       nginx1
fd07b5a52dc9        273c6a7e33d5                "catalina.sh run"        32 minutes ago      Up 32 minutes       0.0.0.0:8083->8080/tcp   tomcat3
70504c263f1c        273c6a7e33d5                "catalina.sh run"        32 minutes ago      Up 32 minutes       0.0.0.0:8082->8080/tcp   tomcat2
286c80246e27        273c6a7e33d5                "catalina.sh run"        32 minutes ago      Up 32 minutes       0.0.0.0:8081->8080/tcp   tomcat1
```

##### 将容器重新启动

```
docker restart nginx1
docker restart tomcat1
docker restart tomcat2
docker restart tomcat3
```

##### 最后出现页面 ，证明成功

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200301202717547.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3p5cTM1Nw==,size_16,color_FFFFFF,t_70)