# GitHub与Git使用

## 1、Git简介

github是通过Git进行版本控制的软件源代码托管服务平台。github除了Git代码仓库托管及基本的Web管理界面以外，它还提供了一些方便社会化共同软件开发的功能，即一般人口中的社群功能，包括允许用户追踪其他用户、组织、软件库的动态，对[软件代码](https://baike.sogou.com/lemma/ShowInnerLink.htm?lemmaId=4196027&ss_c=ssc.citiao.link)的改动和bug提出评论等。

### 1.1、工作原理 / 流程

![image-20200223111840767](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223111840767.png)

上图展示了git的整体架构，以及和各部分相关的主要命令，其中涉及的各部分。

l Workspace：工作区

l Index / Stage：暂存区

l Repository：或本地仓库

l Remote：远程仓库（Github上建的仓库就相当于远程仓库）

***\*工作区(working directory)，\****简言之就是你工作的区域。对于git而言，就是的本地工作目录。工作区的内容会包含提交到暂存区和版本库(当前提交点)的内容，同时也包含自己的修改内容。

***\*暂存区(stage area, 又称为索引区index)，\****是git中一个非常重要的概念。是我们把修改提交版本库前的一个过渡阶段。查看GIT自带帮助手册的时候，通常以index来表示暂存区。在工作目录下有一个.git的目录，里面有个index文件，存储着关于暂存区的内容。git add命令将工作区内容添加到暂存区。

***\*本地仓库(local repository)，\****版本控制系统的仓库，存在于本地。当执行git commit命令后，会将暂存区内容提交到仓库之中。在工作区下面有.git的目录，这个目录下的内容不属于工作区，里面便是仓库的数据信息，暂存区相关内容也在其中。这里也可以使用merge或rebase将***\*远程仓库副本\****合并到本地仓库。图中的只有merge，注意这里也可以使用rebase。

***\*远程版本库(remote repository)，\****与本地仓库概念基本一致，不同之处在于一个存在远程，可用于远程协作，一个却是存在于本地。通过push/pull可实现本地与远程的交互；

***\*远程仓库副本，\****可以理解为存在于本地的远程仓库缓存。如需更新，可通过git fetch/pull命令获取远程仓库内容。使用fech获取时，并未合并到本地仓库，此时可使用git merge实现远程仓库副本与本地仓库的合并。git pull 根据配置的不同，可为git fetch + git merge 或 git fetch + git rebase。rebase和merge的区别可以自己去网上找些资料了解下。

### 1.2、Git安装与基本配置

####  一、准备工作

1. Git官网（https://git-scm.com/）下载Git。
2. GitHub官网（https://github.com/）注册账号。
3. 测试仓库 https://github.com/liuxinfengabc/cultivate.git 

#### 二、安装Git



1. 点击安装，选择目录为根目录，文件夹为Git，然后Next->默认安装。
2. 安装完成后打开根目录，找到Git文件夹，右键git-bash.exe可执行文件将其固定的工具栏，点击git-bash.exe运行。
3. 输入指令 ls 查看文件目录。

![image-20200222201309260](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222201309260.png)

**此为运行正常安装完成！**

#### 三、运行Git

1. 设置用户名和邮箱。

   设置用户名：$ git config --global user.name "Your Name"。

   设置邮箱：  $ git config --globaluser,email "email@example.com"。

   设置用户名和邮箱，修改后提交到远程仓库能够看到是谁提交的

2. 输入 $ git init 启动Git。

#### 四、在GitHub上设置公钥

1. .新建/连接主存储库。
2. 输入 $ ssh-keygen -t rsa -C "[email@example.com](mailto:"13963639551@163.com")" 生成公钥，在C:\Users\Administrator\.ssh下找到id_rsa.pub文件，记事本打开复制公钥。
3. 在GitHub上新建密钥，自定义标题（title）将公钥粘贴保存。

#### 五、创建git库

　　git init  #在当前目录中生成一个.git 目录（含有.git目录的目录即是git仓库）

#### 六、注册git用户

--->用于在团队合作开发中，表明代码作者。

　　git config --global user.name XXX  #用户名

　　git config --global user.email XXX  #用户邮箱

　　git config --list  #查看用户信息

​			注：加--global，全局设置。



## 2.栏目介绍

![image-20200222194828116](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222194828116.png)

- code：代码所在地
- issues：讨论、bug跟踪
- pull request：提交请求，进行code view
- project：项目管理、工作计划
  1. To Do：待完成任务
  2. Doing：正在进行中
  3. Done：已完成的
- Wiki：项目介绍，资料存放处（可放md格式）

## 3.操作

#### 3.1、创建新库

![image-20200218144737464](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200218144737464.png)

![image-20200218144913586](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200218144913586.png)





​		**创建好的仓库**

![image-20200222203649960](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222203649960.png)

#### 3.2初始化一个新的Git仓库

本地创建一个文件夹用来存放从GitHub上clone下来的文件

![image-20200222204147521](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222204147521.png)



**1、创建文件夹**

![image-20200222204345996](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222204345996.png)

**2、在文件夹内初始化Git（创建Git仓库）**

![image-20200222204804904](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222204804904.png)



#### 3.3向远程仓库添加文件

主程序员GitHub地址：https://github.com/2116354540/test

我的（开发人员）GitHub地址：

说明：先从主程序员GitHub中fork，然后再clone到自己的Github中，并进行添加文件

##### 从自己的工作区-->自己的远程仓库

![image-20200222212731019](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222212731019.png)

![image-20200222212851246](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222212851246.png)

**接下来进行Git操作**

![image-20200222213035974](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222213035974.png)

![image-20200222213134871](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222213134871.png)

![image-20200222213327307](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222213327307.png)

**接下来会用一些Linux命令进行操作，也可以使用界面直接进行操作**

![image-20200222213622335](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222213622335.png)

![image-20200222213753878](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222213753878.png)



![image-20200222213952828](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222213952828.png)

![image-20200222214931817](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222214931817.png)

**这时候只是在自己的GitHub仓库，还没有到主程序员的GitHub仓库**

![image-20200222214957505](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222214957505.png)

##### 开发人员GitHub仓库-->主程序员GitHub仓库

![image-20200222221153506](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222221153506.png)

![image-20200222221419183](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222221419183.png)

**这时候已经提交给主程序员了，可以通知他让他同意**

![image-20200222221858863](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222221858863.png)

![image-20200222221943789](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222221943789.png)

![image-20200222222059393](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222222059393.png)

![image-20200222222146329](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222222146329.png)

**这样就从开发人员的GitHub中更新到自己原来fork的仓库主程序员中了**

#### 3.4、同步主程序员仓库中的更新

**说明：开发人员仓库和主程序员的仓库是两个独立的仓库，主程序员仓库更新后不会自动更新到开发人员仓库中**



##### 主程序员仓库

![image-20200222222735438](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222222735438.png)

##### 开发人员的仓库

![image-20200222222927007](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222222927007.png)

可以看出这时候开发人员的仓库还没更新

![image-20200222225834676](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222225834676.png)

![image-20200222224752723](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222224752723.png)

**此时，开发人员本地库已经和主程序员的原仓库已经完全同步了。但是注意，此时只是你电脑上的本地库和远程的github原仓库同步了，你自己的github仓库还没有同步，此时需要使用“git push”命令把你本地的仓库提交到github中。**

![image-20200222225149051](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222225149051.png)

![image-20200222225407661](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222225407661.png)

![image-20200222225429532](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222225429532.png)

**开发人员仓库中更新了**

#### 3.5、版本回退

**说明：如果发现某次的版本提交完毕后有错误，需要回退，可以用下面的方法**

![image-20200222231335512](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222231335512.png)

![image-20200222231719188](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222231719188.png)

![image-20200222231733144](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222231733144.png)

![image-20200222232302058](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222232302058.png)

![image-20200222232320868](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222232320868.png)

**此时回退版本已经完毕**

命令总结：

mkdir 文件名：创建文件夹

cd 文件夹名：进入文件夹

touch 文件名：创建文件

git init：在本地初始化，创建Git仓库

git status：查看当前状态

git add 【文件名】或者【 . (全部)】：将文件从工作去提交到暂存区

git commit -m "提交描述"：将暂存区提交到本地仓库

git push：将本地仓库提交到远程仓库

git pull：将远程仓库的改动更新到本地工作区

git remote -v：查看origin和upstream源

git remote add origin/upstream git地址：添加origin/upstream源

git fetch upstream：获取upstream源仓库的提交和改变

git checkout master：切换到master

git merge upstream/master：合并远程的master

git rm 文件名

#### 3.6、GitHub中提交文件

![image-20200218164434616](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200218164434616.png)

![image-20200218164930448](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200218164930448.png)

![image-20200218165053615](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200218165053615.png)

![image-20200218165226798](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200218165226798.png)

随后需要的步骤pull request和后面一样

用git还是github上直接操作二者选其一

## 4、开发环境中使用

## 4.1、tortoiseGit使用（windows操作）

第一步：详细见（Git本地仓库创建及操作）

第二步：修改了文件后，右键选择commit，输入Message后，点Commit提交到本地仓库。

![img](file:///C:\Users\RHHR1314\AppData\Local\Temp\ksohtml18700\wps3.png) 

第三步：拉取服务器文件，git pull，点击OK，后点击close

![img](file:///C:\Users\RHHR1314\AppData\Local\Temp\ksohtml18700\wps4.png) 

如果文件出现感叹号则表示有冲突，必须先解决冲突。

冲突解决方法：

1.在点击close后可以直接在弹出的窗口中点击yes来查看冲突，也可以通过点击Diff看本地仓库文件和服务器上面的文件哪里冲突了。

2.在弹出的窗口中双击打开冲突文件，解决冲突问题。

3.冲突解决完之后，删除在目录下生成出的多余文件，再次commit。

![img](file:///C:\Users\RHHR1314\AppData\Local\Temp\ksohtml18700\wps5.png) 

 

第四步：提交代表到服务器， git push

![img](file:///C:\Users\RHHR1314\AppData\Local\Temp\ksohtml18700\wps6.png) 

 

新增篇：

第一步：在仓库中新增文件后，先pull，然后可以在diff中查看和仓库区别

第二步：右键->commit，选择你需要提交的文件，然后点击commit

第三步：push文件

## 4.2、eclipse中使用Git导入工程

1. 打开Git Repositories窗口，点击右上角Add an existing local Git Repository to this view图标
2. 在Directory中选择你本地Git总仓库，在下方选择你想导入的Git仓库目录
3. 打开你导入的仓库->Working Tree，选择你想导入的工程，右键->Import Projects...
4. 调整选项，导入工程
5. 右键已导入工程->Gradle->Refresh Gradle Project

## 4.3、eclipse中使用Git同步工程

1. 右键工程->Team->Synchronize Workspace进入同步窗口
2. 对双向红箭头文件，打开，进行代码合并操作，操作完成后 右键->Mark asMerged
3. 3.选择要上传的文件，右键->Add to Index/在Unstaged Changes中右键->Add to Index
4. 在Staged Changes中可以查看要提交的文件，在Commit Message中输入提交内容备注，以及作者相关，点击commit至本地
5. Pull拉取服务器上更新文件
6. 最后Push至服务器

## 4.3、idea中使用Git/GitHub

**配置Git和GitHub**

File-->Settings-->Verdion Control-->Git/GitHub

![image-20200222235204939](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222235204939.png)

![image-20200222235239154](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222235239154.png)



**将项目传到github上**

![image-20200222234240245](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222234240245.png)

![image-20200222234341314](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222234341314.png)

**点击Share**

![image-20200222234527903](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222234527903.png)

**点击Add，就可以将项目传到GitHub上了，不存在仓库就会创建一个名字为testJava的仓库**

![image-20200222234621484](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222234621484.png)

**看到Successfully，表示创建成功**

![image-20200222234750674](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222234750674.png)

**现在我讲本地的testJava项目删除，咱们试一下怎么在idea中直接从GitHub上clone项目**

**前提，当然是idea中已经配置好了Git和GitHub**

![image-20200222234926629](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222234926629.png)

![image-20200222235447325](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222235447325.png)

**点击Clone就行了**

![image-20200222235614107](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200222235614107.png)

**已经克隆完毕**

![image-20200223000755898](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223000755898.png)

VCS-->Git-->Commit Changes

![image-20200223000919666](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223000919666.png)

![image-20200223001043046](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223001043046.png)

![image-20200223001108163](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223001108163.png)

![image-20200223001158304](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223001158304.png)

![image-20200223001227742](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223001227742.png)

**看到这个说明已经push成功**

![image-20200223001340225](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223001340225.png)

**如果往主程序员的仓库更新，就参考前面讲的pull request**



**若别人更新了仓库，在idea中怎么办呢？**

![image-20200223001756484](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223001756484.png)

![image-20200223001855840](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223001855840.png)

**这三个地方都可以**

![](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223001948419.png)

## 4.4、idea中使用Git命令

**File-->Settings-->**

![image-20200223002248860](https://github.com/CHonghaozmr/cultivate/blob/master/5.Git/img/GitHub%E4%B8%8EGit%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E%E6%96%87%E6%A1%A3/image-20200223002248860.png)