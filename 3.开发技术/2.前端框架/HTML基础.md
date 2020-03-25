## 1.HTML基础

#### 1.1 概述

##### 1 HTML标记

HTML文档中，分为单标记和双标记。双标记都是按“<标记>和</标记>” 的格式配对使用的，单标记以“<标记/>”的格式使用。标记中可以包含属性，一般格式为：<标记   属性1=“值1”   属性2=“值2” …>
例如：

```
<font size="1" color="#dd0000" face="宋体" >HTML学习</font>
<img alt=" smile"  src="smile.png" />
```

 注意：属性之间必须使用空格隔开，属性名和属性值之间用等号隔开，等号左边是属性名称，右边是属性值。

##### 2 XHTML的基本结构

（1）文档类型定义(DOCTYPE)
（2）文件头部(head)
（3）文件主体(body)

##### 3 HTML与XHTML标记的区别

1. XHTML更严格：
   - 所有元素必须要求正确嵌套

     ```
     以下是正确的嵌套：
     <p>布啦布啦
     <b>独树一帜</b>。
     </p>
     以下是错误的嵌套：
     <p>布啦布啦
     <b>独树一帜</p>
     </b>。
     ```

     

   - 标记名字一定要用小写字母

     ```
     <IMG SRC = "/images/adpics/1/b027.jpg" Alt = "blabla" / >
     以下是正确的：
     <img src = "/images/adpics/1/b027.jpg" alt = "blabla" />
     ```

     

   - 给所有属性值加引号

     ```
     HTML 并不强制要求属性值加双引号。
     比如在HTML你可以写: 
     <table width=100 height=200>…</table>
     但在 XHTML 里，应该写成：
     <table width=“100” height=“200”>…</table>
     ```

     

   - 所有的XHTML元素一定要关闭

     ```
     <p>This is a paragraph      错误。
     <p>This is a paragraph</p>  正确
     ```

     

   - 单标签要结束用 />来结束

     ```
     This  is a break<br>错误。
     This is a break<br/>正确。
     
     ```



#### 1.2文件头部

文件头部主要放置IE浏览器标题栏的名称，或是其他需要给浏览器的信息。文件头部包含在head标记中。<head>头部标记</head> 其头部标记包含有四个标签，分别是：

```
<title></title>，只能有一个。
<base>，只能有一个。
<link>，可以有多个。
<meta>可以有多个，它有两种表示形式：
1、<meta name="" content="">
2、<meta http-equiv=""content=""> 
```



##### 1 base

<base> 标签为页面上的所有链接规定默认URL相对地址。
通常情况下，浏览器会从当前文档的 URL 中获取URL相对地址。
使用 <base> 标签可以手动改变URL相对地址，这其中包括 <a>、<img>、<link>等标签中的 URL相对地址。
<base>标签有两个重要的属性：href属性和target属性。
href属性：规定页面中所有相对链接的基准 URL（即默认URL相对地址）。
target属性：规定网页的打开方式，取值分为：
_self，自我覆盖、默认。
_blank，创建新窗口打开新页面。
_top，在浏览器的整个窗口打开，将会忽略所有的框架结构。
_parent， 指向父框架文档。

```
<html>
<head>
<base href="http://www.w3school.com.cn/i/" />
<base target="_blank" />
</head>
<body>
<img src="eg_smile.gif" />
<h1>
<a href="http://www.w3school.com.cn">W3school</a>
<h1>
</body>
</html>

```

##### 2 link标签

```
用于定义当前文档与其他文档的关系，其主要用途是链接样式表。
<link>标签的常用属性如下：
href:定义被链接文档的位置。
rel:定义当前文档与被链接文档之间的关系。
type:规定被链接文档的 MIME 类型。
如：
<link  href="style.css"  rel="stylesheet"  type="text/css"  />
以上语句确定本网页的CSS样式表为当前路径下的外部文件style.css。
```

##### 3 meta标签

meta标签用来描述一个HTML网页文档的属性，例如作者、日期和时间、网页描述、关键词、页面刷新等。

```
有两种用法：
（1）<meta name="" content="">
（2）<meta http-equiv="" content=""> 
其中，
name属性用于在网页中加入一些关于网页的描述信息，网页的关键字，主要内容、作者和版权等。
http-equiv属性相当于http的文件头作用，它可以向浏览器传回一些有用的信息，以帮助正确和精确地显示网页内容，与之对应的属性值为content，content中的内容其实就是各个参数的变量值。

name属性主要有以下几种可选值，
keywords：网页的关键字，例如 ：
 <meta name=“keywords”  content=“关键词1，关键词2，……”> 
description：网页的主要内容。例如：
 <meta name=“description” content=“你网页的简述”> 
Robots: 相当于一个搜索引擎导航开关，用来告诉搜索引擎页面是否可以被检索。它的属性可选值以及相关意义分别是：
all：文件将被检索，且页面链接可被查询，该值也是默认值。
none：文件将不被检索，且页面链接不可被查询。
index/noindex：文件将被检索/文件将不被检索。
follow/ nofollow ：页面链接可被查询/页面链接不可被查询。
author:标注网页的作者。例如：
<meta name="author" content="作者名">  。
copyright:标注版权。 例如：
<meta name="copyright" content="版权声明">

http-equiv属性主要有以下几种可选值：
content-type：指定网页的类型和字符集。 例如：
<meta http-equiv="content-type" content="text/html" charset="gb2312">  
refresh，指定自动刷新或跳转的开始时间与跳转的网址。例如：
<meta http-epuiv="refresh" content="3">
网页3秒自动刷新一次。
<meta http-epuiv=" refresh" content=" 3";url=http//www.baidu.com>
网页3秒后自动跳转到百度网站。
Explres指定网页在缓存中的过期时间。例如：
<meta http-epuiv= " explres "  content= " Wed,26 Feb 1997 08:21:57 GMT " >。
page-enter和page-exit它是用于指定进入和退出的缓存效果，共有0-23种显示效果，例如（盒状收缩）：
<meta http-equiv="page-enter" content=" revealTrans(duration=1,transition=0)"/>

```

Page-Enter与Page-Exit例子：

```
<html>
<head>
<title>Page-Enter和Page-Exit使用</title>
<meta http-equiv="page-enter" content="revealTrans(Duration=1,Transition=23)">     
<meta http-equiv="page-exit" content="revealTrans(Duration=3,Transition=11)"> 
</head>
<a href="font_color.htm">字体颜色</a><br/>
<a href="font_face.htm">字体字型</a><br/>
<a href="font_size.htm">字体大小</a><br/>
</body>
</html>

```

#### 1.3文件主体

文件主体是将被浏览器显示在窗口中的内容。包括文件内的文字、图片、影像、动画、颜色、音效等。
文件主体包含在<body></body>标签对内部。例子：

```
<html>
<head>
    <title>我的第一个主页</title>
</head>
<body>
  <h3>欢迎光临我的主页</h3>
  <p>这是我第一次做主页，无论怎么样，我都会努力做好！</p>
</body>
</html>

```



#### 1.4 HTML标记

##### 1 HTML标记一览

| 标签                                                | 描述                                               |
| :-------------------------------------------------- | :------------------------------------------------- |
| <!--...-->                                          | 定义注释。                                         |
| <[!DOCTYPE](https://baike.baidu.com/item/!DOCTYPE)> | 定义文档类型。                                     |
| <a>                                                 | 定义锚。                                           |
| <abbr>                                              | 定义缩写。                                         |
| <acronym>                                           | 定义只取首字母的缩写。                             |
| <address>                                           | 定义文档作者或拥有者的联系信息。                   |
| <applet>                                            | 不赞成使用。定义嵌入的 applet。                    |
| <area>                                              | 定义图像映射内部的区域。                           |
| <article>                                           | 定义文章。                                         |
| <aside>                                             | 定义页面内容之外的内容。                           |
| <audio>                                             | 定义声音内容。                                     |
| <b>                                                 | 定义粗体字。                                       |
| <base>                                              | 定义页面中所有链接的默认地址或默认目标。           |
| <basefont>                                          | 不赞成使用。定义页面中文本的默认字体、颜色或尺寸。 |
| <bdi>                                               | 定义文本的文本方向，使其脱离其周围文本的方向设置。 |
| <bdo>                                               | 定义文字方向。                                     |
| <big>                                               | 定义大号文本。                                     |
| <blockquote>                                        | 定义长的引用。                                     |
| <body>                                              | 定义文档的主体。                                   |
| <br>                                                | 定义简单的折行。                                   |
| <button>                                            | 定义按钮 (push button)。                           |
| <canvas>                                            | 定义图形。                                         |
| <caption>                                           | 定义表格标题。                                     |
| <center>                                            | 不赞成使用。定义居中文本。                         |
| <cite>                                              | 定义引用(citation)。                               |
| <code>                                              | 定义计算机代码文本。                               |
| <col>                                               | 定义表格中一个或多个列的属性值。                   |
| <colgroup>                                          | 定义表格中供格式化的列组。                         |
| <command>                                           | 定义命令按钮。                                     |
| <datalist>                                          | 定义下拉列表。                                     |
| <dd>                                                | 定义定义列表中项目的描述。                         |
| <del>                                               | 定义被删除文本。                                   |
| <details>                                           | 定义元素的细节。                                   |
| <dir>                                               | 不赞成使用。定义目录列表。                         |
| <div>                                               | 定义文档中的节。                                   |
| <dfn>                                               | 定义定义项目。                                     |
| <dialog>                                            | 定义对话框或窗口。                                 |
| <dl>                                                | 定义定义列表。                                     |
| <dt>                                                | 定义定义列表中的项目。                             |
| <em>                                                | 定义强调文本。                                     |
| <embed>                                             | 定义外部交互内容或插件。                           |
| <fieldset>                                          | 定义围绕表单中元素的边框。                         |
| <figcaption>                                        | 定义 figure 元素的标题。                           |
| <figure>                                            | 定义媒介内容的分组，以及它们的标题。               |
| <font>                                              | 不赞成使用。定义文字的字体、尺寸和颜色。           |
| <footer>                                            | 定义 section 或 page 的页脚。                      |
| <form>                                              | 定义供用户输入的 HTML 表单。                       |
| <frame>                                             | 定义框架集的窗口或框架。                           |
| <frameset>                                          | 定义框架集。                                       |
| <h1> to <h6>                                        | 定义 HTML 标题，可以改变标题的大小。               |
| <head>                                              | 定义关于文档的信息。                               |
| <header>                                            | 定义 section 或 page 的页眉。                      |
| <hr>                                                | 定义水平线。                                       |
| <html>                                              | 定义 HTML 文档。                                   |
| <i>                                                 | 定义斜体字。                                       |
| <iframe>                                            | 定义内联框架。                                     |
| <img>                                               | 定义图像。                                         |
| <input>                                             | 定义输入控件。                                     |
| <ins>                                               | 定义被插入文本。                                   |
| <isindex>                                           | 不赞成使用。定义与文档相关的可搜索索引。           |
| <kbd>                                               | 定义键盘文本。                                     |
| <keygen>                                            | 定义生成密钥。                                     |
| <label>                                             | 定义 input 元素的标注。                            |
| <legend>                                            | 定义 fieldset 元素的标题。                         |
| <li>                                                | 定义列表的项目。                                   |
| <link>                                              | 定义文档与外部资源的关系。                         |
| <map>                                               | 定义图像映射。                                     |
| <mark>                                              | 定义有记号的文本。                                 |
| <menu>                                              | 定义菜单列表。                                     |
| <meta>                                              | 定义关于 HTML 文档的元信息。                       |
| <meter>                                             | 定义预定义范围内的度量。                           |
| <nav>                                               | 定义导航链接。                                     |
| <noframes>                                          | 定义针对不支持框架的用户的替代内容。               |
| <noscript>                                          | 定义针对不支持客户端脚本的用户的替代内容。         |
| <object>                                            | 定义内嵌对象。                                     |
| <ol>                                                | 定义有序列表。                                     |
| <optgroup>                                          | 定义选择列表中相关选项的组合。                     |
| <option>                                            | 定义选择列表中的选项。                             |
| <output>                                            | 定义输出的一些类型。                               |
| <p>                                                 | 定义段落。                                         |
| <param>                                             | 定义对象的参数。                                   |
| <pre>                                               | 定义预格式文本。                                   |
| <progress>                                          | 定义任何类型的任务的进度。                         |
| <q>                                                 | 定义短的引用。                                     |
| <rp>                                                | 定义若浏览器不支持 ruby 元素显示的内容。           |
| <rt>                                                | 定义 ruby 注释的解释。                             |
| <ruby>                                              | 定义 ruby 注释。                                   |
| <s>                                                 | 定义加删除线的文本。                               |
| <samp>                                              | 定义计算机代码样本。                               |
| <script>                                            | 定义客户端脚本。                                   |
| <section>                                           | 定义 section。                                     |
| <select>                                            | 定义选择列表（下拉列表）。                         |
| <small>                                             | 定义小号文本。                                     |
| <source>                                            | 定义媒介源。                                       |
| <span>                                              | 定义文档中的节。                                   |
| <strike>                                            | 不赞成使用。定义加删除线文本。                     |
| <strong>                                            | 定义强调文本。                                     |
| <style>                                             | 定义文档的样式信息。                               |
| <sub>                                               | 定义下标文本。                                     |
| <summary>                                           | 为 <details> 元素定义可见的标题。                  |
| <sup>                                               | 定义上标文本。                                     |
| <table>                                             | 定义表格。                                         |
| <tbody>                                             | 定义表格中的主体内容。                             |
| <td>                                                | 定义表格中的单元。                                 |
| <textarea>                                          | 定义多行的文本输入控件。                           |
| <tfoot>                                             | 定义表格中的表注内容（脚注）。                     |
| <th>                                                | 定义表格中的表头单元格。                           |
| <thead>                                             | 定义表格中的表头内容。                             |
| <time>                                              | 定义日期/时间。                                    |
| <title>                                             | 定义文档的标题。                                   |
| <tr>                                                | 定义表格中的行。                                   |
| <track>                                             | 定义用在媒体播放器中的文本轨道。                   |
| <tt>                                                | 定义打字机文本。                                   |
| <u>                                                 | 定义下划线文本。                                   |
| <ul>                                                | 定义无序列表。                                     |
| <var>                                               | 定义文本的变量部分。                               |
| <video>                                             | 定义视频。                                         |
| <wbr>                                               | 定义视频。                                         |
| <xmp>                                               | 定义预格式文本。                                   |

##### 2. h1至h6标记



##### 3. p标签和br标记



##### 4. 文字样式标记



##### 5. 文字修饰标记



##### 6. 列表制作标记



##### 7. 超链接标记



##### 8. img标记



##### 9. 表格标记

## 2.网页布局与样式

CSS，Cascading style sheet，层叠样式表，是为了弥补HTML在布局和样式方面的不足而产生的。
在XHTML网页中，通常用CSS对页面整体布局和样式控制

### 2.1 CSC布局应运而生

表格最初的目的是：显示表格状的数据。后来，表格被用于网页布局时，设置其 border=“0” ，使得网页设计师可以将图片和文本放在这无形的网格中。表格布局的主要弊端如下：

```
1. 把样式和内容混合在一起，使得文件的大小无谓变大，减慢速度，浪费用户的流量费用。
2. 重新设计现有的站点和内容代价昂贵。
3. 极难保持整个站点的视觉的一致性，花费也极高。
```

为了避免表格网页布局与生俱来的弊端，CSS布局应运而生。利用CSS布局，网页设计者可使页面的实际内容与呈现格式的逻辑分离。 CSS布局的主要优势如下：

```
1.一次编写，重复使用。
2.把样式和内容分离，内容简单规范，方便代码维护。
3.缩小文件体积，CSS文件无需重复下载，增快网络访问速度。
4.在不改变HTML的情况下，可快速修改和切换网站的整体设计和风格。
5.帮助网站保持视觉的一致性。

```



