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



# Docker常用

docker IP：192.168.99.100

docker默认账号密码：docker  tcuser

 

## 镜像

查询镜像：docker search+镜像名字

拉取镜像：docker pull+镜像名字

删除镜像：docker rmi+镜像ID

 

启动并进去容器：docker run -it+镜像ID /bin/bash

后台启动容器：docker run -itd+镜像ID /bin/bash

​       docker run -itd --privileged=true+镜像ID /sbin/init

提交镜像：docker commit -m=”提交信息” -a=”作者”+容器ID+目标名

## 容器

进入容器：docker exec -it+容器ID /bin/bash

停止容器：docker stop+容器ID

停止所有容器：docker stop $(docker ps -a -q)

启动容器：docker start+容器ID

重启容器：docker restart+容器ID

查看所有容器：ps -a

查看后台运行容器：docker ps

退出容器：exit

退出但不关闭容器：Ctrl+P+Q

删除所有容器：docker rm $(docker ps -a -q)

 

 

# Linux常用

## 目录

ls：列出目录

cd：

   cd+目录：进入指定目录；

   cd .：当前目录

   cd ..：返回上层目录

pwd：显示当前目录

mkdir：创建新目录

rm -rf：删除目录

## 文件

touch：创建新文件

rm：删除文件

cp -p+文件+目标目录：复制文件 

mv+文件+目标目录：剪切文件

tar -zxvf+文件名：解压文件

 

vi和vim编辑器：

   vi+文件，按 i 进去insert模式

   

   按esc进去命令模式：

​     w：保存；

​     q：退出

​     q！：强制退出不保存

​     wq!：强制保存并退出

## 删除

   删除一行：在要删除的行上按两次d

   删除一行内的几个字：d+字数+左右方向键

   删除多行：y+行数+上下方向键

## 复制

   复制一行：在要复制的行上按两次y

   复制一行内的几个字：y+字数+左右方向键

   复制多行：y+行数+上下方向键

 

## 权限

授权目录下所有文件权限：chmod -R 777 /usr/lib/*