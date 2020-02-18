## python爬虫

### 1.ProxyHandler处理器（代理）：
1. 代理的原理：在请求目的网站之前，先请求代理服务器，然后让代理服务器去请求目的网站，代理服务器拿到目的网站的数据后，再转发给我们的代码。
2. http://httpbin.org：这个网站可以方便的查看http请求的一些参数。
3. 在代码中使用代理：
    * 使用`urllib.request.ProxyHandler`，传入一个代理，这个代理是一个字典，字典的key依赖于代理服务器能够接收的类型，一般是`http`或者`https`，值是`ip:port`。
    * 使用上一步创建的`handler`，以及`request.build_opener`创建一个`opener`对象。
    * 使用上一步创建的`opener`，调用`open`函数，发起请求。
示例代码如下：
```python
from urllib import request
url = 'http://httpbin.org/ip'
# 1. 使用ProxyHandler，传入代理构建一个handler
handler = request.ProxyHandler({"http":"223.241.78.43:8010"})
# 2. 使用上面创建的handler构建一个opener
opener = request.build_opener(handler)
# 3. 使用opener去发送一个请求
resp = opener.open(url)
print(resp.read())
```





### 2. requests请求

###### 发送get请求：

发送get请求，直接调用`requests.get`就可以了。想要发送什么类型的请求，就调用什么方法。

```python
response = requests.get("https://www.baidu.com/")
```

###### response的一些属性：

```python
import requests

kw = {'wd':'中国'}

headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"}

# params 接收一个字典或者字符串的查询参数，字典类型自动转换为url编码，不需要urlencode()
response = requests.get("http://www.baidu.com/s", params = kw, headers = headers)

# 查看响应内容，response.text 返回的是Unicode格式的数据
print(response.text)

# 查看响应内容，response.content返回的字节流数据
print(response.content)

# 查看完整url地址
print(response.url)

# 查看响应头部字符编码
print(response.encoding)

# 查看响应码
print(response.status_code)
```

###### response.text和response.content的区别：

1. response.content：这个是直接从网络上面抓取的数据。没有经过任何解码。所以是一个bytes类型。其实在硬盘上和在网络上传输的字符串都是bytes类型。
2. response.text：这个是str的数据类型，是requests库将response.content进行解码的字符串。解码需要指定一个编码方式，requests会根据自己的猜测来判断编码的方式。所以有时候可能会猜测错误，就会导致解码产生乱码。这时候就应该使用`response.content.decode('utf-8')`进行手动解码。

###### 发送post请求：

发送post请求非常简单。直接调用`requests.post`方法就可以了。
如果返回的是json数据。那么可以调用`response.json()`来将json字符串转换为字典或者列表。

使用代理：

在请求方法中，传递`proxies`参数就可以了。

处理cookie：

如果想要在多次请求中共享cookie。那么应该使用session。示例代码如下：

```python
import requests
url = "http://www.renren.com/PLogin.do"
data = {"email":"970138074@qq.com",'password':"pythonspider"}
headers = {
    'User-Agent': "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"
}

session = requests.Session()

session.post(url,data=data,headers=headers)

response = session.get('http://www.renren.com/880151247/profile')
with open('renren.html','w',encoding='utf-8') as fp:
    fp.write(response.text)
```

### 3.BeautifulSoup：

###### find_all的使用：
1. 在提取标签的时候，第一个参数是标签的名字。然后如果在提取标签的时候想要使用标签属性进行过滤，那么可以在这个方法中通过关键字参数的形式，将属性的名字以及对应的值传进去。或者是使用`attrs`属性，将所有的属性以及对应的值放在一个字典中传给`attrs`属性。
2. 有些时候，在提取标签的时候，不想提取那么多，那么可以使用`limit`参数。限制提取多少个。

###### find与find_all的区别：
1. find：找到第一个满足条件的标签就返回。说白了，就是只会返回一个元素。
2. find_all:将所有满足条件的标签都返回。说白了，会返回很多标签（以列表的形式）。

###### 使用find和find_all的过滤条件：
1. 关键字参数：将属性的名字作为关键字参数的名字，以及属性的值作为关键字参数的值进行过滤。
2. attrs参数：将属性条件放到一个字典中，传给attrs参数。

###### 获取标签的属性：
1. 通过下标获取：通过标签的下标的方式。
    ```python
    href = a['href']
    ```
2. 通过attrs属性获取：示例代码：
    ```python
    href = a.attrs['href']
    ```

###### string和strings、stripped_strings属性以及get_text方法：
1. string：获取某个标签下的非标签字符串。返回来的是个字符串。如果这个标签下有多行字符，那么就不能获取到了。
2. strings：获取某个标签下的子孙非标签字符串。返回来的是个生成器。
2. stripped_strings：获取某个标签下的子孙非标签字符串，会去掉空白字符。返回来的是个生成器。
4. get_text：获取某个标签下的子孙非标签字符串。不是以列表的形式返回，是以普通字符串返回。


###### CSS选择器：
1. 根据标签的名字选择，示例代码如下：
    ```css
    p{
        background-color: pink;
    }
    ```
2. 根据类名选择，那么要在类的前面加一个点。示例代码如下：
    ```css
    .line{
        background-color: pink;
    }
    ```
3. 根据id名字选择，那么要在id的前面加一个#号。示例代码如下：
    ```css
    #box{
        background-color: pink;
    }
    ```
4. 查找子孙元素。那么要在子孙元素中间有一个空格。示例代码如下：
    ```css
    #box p{
        background-color: pink;
    }
    ```
5. 查找直接子元素。那么要在父子元素中间有一个>。示例代码如下：
    ```css
    #box > p{
        background-color: pink;
    }
    ```
6. 根据属性的名字进行查找。那么应该先写标签名字，然后再在中括号中写属性的值。示例代码如下：
    ```css
    input[name='username']{
        background-color: pink;
    }
    ```
7. 在根据类名或者id进行查找的时候，如果还要根据标签名进行过滤。那么可以在类的前面或者id的前面加上标签名字。示例代码如下：
    ```css
    div#line{
        background-color: pink;
    }
    div.line{
        background-color: pink;
    }
    ```

###### BeautifulSop中使用css选择器：
在`BeautifulSoup`中，要使用css选择器，那么应该使用`soup.select()`方法。应该传递一个css选择器的字符串给select方法。


###### 常见的四种对象：
1. Tag：BeautifulSoup中所有的标签都是Tag类型，并且BeautifulSoup的对象其实本质上也是一个Tag类型。所以其实一些方法比如find、find_all并不是BeautifulSoup的，而是Tag的。
2. NavigableString：继承自python中的str，用起来就跟使用python的str是一样的。
3. BeautifulSoup：继承自Tag。用来生成BeaufifulSoup树的。对于一些查找方法，比如find、select这些，其实还是Tag的。
4. Comment：这个也没什么好说，就是继承自NavigableString。

###### contents和children：
返回某个标签下的直接子元素，其中也包括字符串。他们两的区别是：contents返回来的是一个列表，children返回的是一个迭代器。

### 4.Scrapy

###### 安装scrapy框架：
1. 安装`scrapy`：通过`pip install scrapy`即可安装。
2. 如果在windows下，还需要安装`pypiwin32`，如果不安装，那么以后运行scrapy项目的时候就会报错。安装方式：`pip install pypiwin32`。
3. 如果是在ubuntu下，还需要安装一些第三方库：`sudo apt-get install python-dev python-pip libxml2-dev libxslt1-dev zlib1g-dev libffi-dev libssl-dev`。

###### 创建项目和爬虫：
1. 创建项目：`scrapy startproject [爬虫的名字]`。
2. 创建爬虫：进入到项目所在的路径，执行命令：`scrapy genspider [爬虫名字] [爬虫的域名]`。注意，爬虫名字不能和项目名称一致。

###### 项目目录结构：
1. items.py：用来存放爬虫爬取下来数据的模型。 
2. middlewares.py：用来存放各种中间件的文件。 
3. pipelines.py：用来将items的模型存储到本地磁盘中。 
4. settings.py：本爬虫的一些配置信息（比如请求头、多久发送一次请求、ip代理池等）。 
5. scrapy.cfg：项目的配置文件。 
6. spiders包：以后所有的爬虫，都是存放到这个里面。

###### 糗事百科Scrapy爬虫笔记：
1. response是一个`scrapy.http.response.html.HtmlResponse`对象。可以执行`xpath`和`css`语法来提取数据。
2. 提取出来的数据，是一个`Selector`或者是一个`SelectorList`对象。如果想要获取其中的字符串。那么应该执行`getall`或者`get`方法。
3. getall方法：获取`Selector`中的所有文本。返回的是一个列表。
4. get方法：获取的是`Selector`中的第一个文本。返回的是一个str类型。
5. 如果数据解析回来，要传给pipline处理。那么可以使用`yield`来返回。或者是收集所有的item。最后统一使用return返回。
6. item：建议在`items.py`中定义好模型。以后就不要使用字典。
7. pipeline：这个是专门用来保存数据的。其中有三个方法是会经常用的。
    * `open_spider(self,spider)`：当爬虫被打开的时候执行。
    * `process_item(self,item,spider)`：当爬虫有item传过来的时候会被调用。
    * `close_spider(self,spider)`：当爬虫关闭的时候会被调用。
    要激活piplilne，应该在`settings.py`中，设置`ITEM_PIPELINES`。示例如下：
    ```python
    ITEM_PIPELINES = {
       'qsbk.pipelines.QsbkPipeline': 300,
    }
    ```

###### JsonItemExporter和JsonLinesItemExporter：
保存json数据的时候，可以使用这两个类，让操作变得得更简单。
1. `JsonItemExporter`：这个是每次把数据添加到内存中。最后统一写入到磁盘中。好处是，存储的数据是一个满足json规则的数据。坏处是如果数据量比较大，那么比较耗内存。示例代码如下：
    ```python
    from scrapy.exporters import JsonItemExporter

    class QsbkPipeline(object):
        def __init__(self):
            self.fp = open("duanzi.json",'wb')
            self.exporter = JsonItemExporter(self.fp,ensure_ascii=False,encoding='utf-8')
            self.exporter.start_exporting()

        def open_spider(self,spider):
            print('爬虫开始了...')

        def process_item(self, item, spider):
            self.exporter.export_item(item)
            return item

        def close_spider(self,spider):
            self.exporter.finish_exporting()
            self.fp.close()
            print('爬虫结束了...')
    ```
2. `JsonLinesItemExporter`：这个是每次调用`export_item`的时候就把这个item存储到硬盘中。坏处是每一个字典是一行，整个文件不是一个满足json格式的文件。好处是每次处理数据的时候就直接存储到了硬盘中，这样不会耗内存，数据也比较安全。示例代码如下：
    ```python
    from scrapy.exporters import JsonLinesItemExporter
    class QsbkPipeline(object):
        def __init__(self):
            self.fp = open("duanzi.json",'wb')
            self.exporter = JsonLinesItemExporter(self.fp,ensure_ascii=False,encoding='utf-8')

        def open_spider(self,spider):
            print('爬虫开始了...')

        def process_item(self, item, spider):
            self.exporter.export_item(item)
            return item

        def close_spider(self,spider):
            self.fp.close()
            print('爬虫结束了...')
    ```


###### CrawlSpider：
需要使用`LinkExtractor`和`Rule`。这两个东西决定爬虫的具体走向。
1. allow设置规则的方法：要能够限制在我们想要的url上面。不要跟其他的url产生相同的正则表达式即可。
2. 什么情况下使用follow：如果在爬取页面的时候，需要将满足当前条件的url再进行跟进，那么就设置为True。否则设置为Fasle。
3. 什么情况下该指定callback：如果这个url对应的页面，只是为了获取更多的url，并不需要里面的数据，那么可以不指定callback。如果想要获取url对应页面中的数据，那么就需要指定一个callback。

###### Scrapy Shell：
1. 可以方便我们做一些数据提取的测试代码。
2. 如果想要执行scrapy命令，那么毫无疑问，肯定是要先进入到scrapy所在的环境中。
3. 如果想要读取某个项目的配置信息，那么应该先进入到这个项目中。再执行`scrapy shell`命令。

###### 模拟登录人人网：
1. 想要发送post请求，那么推荐使用`scrapy.FormRequest`方法。可以方便的指定表单数据。
2. 如果想在爬虫一开始的时候就发送post请求，那么应该重写`start_requests`方法。在这个方法中，发送post请求。

### 5.xpath语法：

###### 使用方式：
使用//获取整个页面当中的元素，然后写标签名，然后再写谓词进行提取。比如：
```
//div[@class='abc']
```

###### 需要注意的知识点：
1. /和//的区别：/代表只获取直接子节点。//获取子孙节点。一般//用得比较多。当然也要视情况而定。
2. contains：有时候某个属性中包含了多个值，那么可以使用`contains`函数。示例代码如下：
    ```
    //div[contains(@class,'job_detail')]
    ```
3. 谓词中的下标是从1开始的，不是从0开始的。

###### 使用lxml解析HTML代码：
1. 解析html字符串：使用`lxml.etree.HTML`进行解析。示例代码如下：
    ```python
    htmlElement = etree.HTML(text)
    print(etree.tostring(htmlElement,encoding='utf-8').decode("utf-8"))
    ```
2. 解析html文件：使用`lxml.etree.parse`进行解析。示例代码如下：
    ```python
    htmlElement = etree.parse("tencent.html")
    print(etree.tostring(htmlElement, encoding='utf-8').decode('utf-8'))
    ```
    这个函数默认使用的是`XML`解析器，所以如果碰到一些不规范的`HTML`代码的时候就会解析错误，这时候就要自己创建`HTML`解析器。
    ```python
    parser = etree.HTMLParser(encoding='utf-8')
    htmlElement = etree.parse("lagou.html",parser=parser)
    print(etree.tostring(htmlElement, encoding='utf-8').decode('utf-8'))
    ```

    ###### lxml结合xpath注意事项：
    1. 使用`xpath`语法。应该使用`Element.xpath`方法。来执行xpath的选择。示例代码如下：
        ```python
        trs = html.xpath("//tr[position()>1]")
        ```
    `xpath函数`返回来的永远是一个列表。
    2. 获取某个标签的属性：
        ```python
        href = html.xpath("//a/@href")
        # 获取a标签的href属性对应的值
        ```
    3. 获取文本，是通过`xpath`中的`text()`函数。示例代码如下：
        ```python
        address = tr.xpath("./td[4]/text()")[0]
        ```
    4. 在某个标签下，再执行xpath函数，获取这个标签下的子孙元素，那么应该在斜杠之前加一个点，代表是在当前元素下获取。示例代码如下：
        ```python
         address = tr.xpath("./td[4]/text()")[0]
        ```