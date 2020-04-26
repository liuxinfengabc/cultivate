# mui入坑笔记

mui是Dcloud官方推出的一个基于html5+标准的框架，用于移动端界面，同时拥有h5组件和原生组件，原生组件依赖于html5+运行环境，也就是原生app里面的webview组件（能加载显示网页，可以将其视为一个浏览器），所以mui里面的原生组件不能用于浏览器环境，可以通过mui里面的mui.os.plus进行判断，如果是plus环境会返回true，否则会返回undefined。开发者可以根据自己的需要进行代码适配，对于APP使用增强的原生组件，对于普通浏览器里面运行的页面使用h5组件。同时用户还可以使用mui.os.android、mui.os.ios及mui.os.wechat对平台进行检测，然后书写不同的逻辑代码。对于mui里面没有封装的原生组件，大家可以根据自己的需要基于html5+标准和native.js语法进行个性化定制。因此这里我们可以有一个基本影响就是我们开始可以直接上手mui，不过需要明白mui与其他UI框架的区别在于，mui拥有独有的原生组件，而且这个是依赖于html5+标准的，所以mui里面的很多组件实现方法甚至用户就是html5+里面的标准写法，对html5+标准有一定了解有助于我们理解mui的一些使用方法。

在mui app开发中，若要使用[HTML5+扩展api](https://www.html5plus.org/#specification)，必须等plusready事件发生后才能正常使用，mui将该事件封装成了`mui.plusReady()`方法，涉及到HTML5+的api，建议都写在mui.plusReady方法中。

## 首页设计

当我进行mui的index首页设计时，考虑到要与移动APP尽量相似的情况下，选择了使用底部选项卡跳转页面的方式进行。但在mui中进行底部选项卡跳转页面的实例中要理解双webview的意义，即双页面（主页面+子页面）：

<img src="D:/Text/MarkDown/images/image-20200425202035463.png" alt="image-20200425202035463" style="zoom: 67%;" />

从上图可以看出，若想完成底部选项卡实现点击跳转，需设置一个主页面和三个子页面，主页面就是容器，将子页面放入其中。以下是我的设计代码，我会用注释说明每一步的作用：

```javascript
var subpages = ['first.html', 'message.html','user.html'];//定义子页面数组
			var subpage_style = {//子页面加载样式，距主页面上部0px，下部51px（选项卡高度）
				top: '0px',
				bottom: '51px'
			};
			var aniShow = {};//定义是否展示的容器
			//创建子页面，首个选项卡页面显示，其它均隐藏；
			mui.plusReady(function() {
				var self = plus.webview.currentWebview();//加载主页面
				for(var i = 0; i < 4; i++) {
					var temp = {};
					var sub = plus.webview.create(subpages[i], subpages[i], subpage_style);//主页面中创建子页面
					if(i != 0) {
						sub.hide();//隐藏其他子页面
					} else {
						temp[subpages[i]] = "true";
						mui.extend(aniShow, temp);//合并，展示首页
					}
					self.append(sub); //sub作为self的子页面
				}
            });
			//当前激活选项
			var activeTab = subpages[0];
			//选项卡点击事件
			mui('.mui-bar-tab').on('tap', 'a', function(e) {
				var targetTab = this.getAttribute('href');//读取a标签href中链接
				if(targetTab == activeTab) { //链接与首页地址相同，所选为首页，不做动作 
					return;
				}
                //显示目标选项卡
				//若为iOS平台或非首次显示，则直接显示
				if(mui.os.ios || aniShow[targetTab]) {
					plus.webview.show(targetTab);
				} else {
					//否则，使用fade-in动画，且保存变量
					var temp = {};
					temp[targetTab] = "true";
					mui.extend(aniShow, temp);
					plus.webview.show(targetTab,"fade-in",200);
				}
				//隐藏当前;
				plus.webview.hide(activeTab);
				//更改当前活跃的选项卡
				activeTab = targetTab;
			});
```



## 页面跳转

在进行单页面跳转时，mui给出了很多过场形式，这里就引用官方文档的示例来为展示。需注意mui的页面跳转为每个页面都赋值了一个id，当进行返回刷新等一些方法时会用到id，要保持每个页面有独立的id。另外除url和id以外，其他方法均可省略，留下要使用的即可：

```js
mui.openWindow({
    url:new-page-url,//新页面地址
    id:new-page-id,//跳转页面id，注意一致
    //以下内容没用到的可省略
    styles:{
      top:newpage-top-position,//新页面顶部位置
      bottom:newage-bottom-position,//新页面底部位置
      width:newpage-width,//新页面宽度，默认为100%
      height:newpage-height,//新页面高度，默认为100%
      ......
    },
    extras:{//自定义扩展参数，可以用来处理页面间传值
      .....
    },
    createNew:false,//是否重复创建同样id的webview，默认为false:不重复创建，直接显示
    show:{
      autoShow:true,//页面loaded事件发生后自动显示，默认为true
      aniShow:animationType,//页面显示动画，默认为”slide-in-right“；
      duration:animationTime//页面动画持续时间，Android平台默认100毫秒，iOS平台默认200毫秒；
    },
    waiting:{
      autoShow:true,//自动显示等待框，默认为true
      title:'正在加载...',//等待对话框上显示的提示内容
      options:{
        width:waiting-dialog-widht,//等待框背景区域宽度，默认根据内容自动计算合适宽度
        height:waiting-dialog-height,//等待框背景区域高度，默认根据内容自动计算合适高度
        ......
      }
    }
})
```

当使用到参数传递时，如何在目标页面接收并显示传递的参数呢？示例

```javascript
//传递页面
mui.openWindow({
    url: 'index.html',
    id: 1,
    extras:{
      name: '首页'
    }
})
...................................
//目标页面
mui.plusReady(function(){
	//接收上个页面传输过来的信息。
    var curView = plus.webview.currentWebview();  
    var name = curView.name;//定义并接收参数
})
```

## mui.ajax()

当我们在数据库中定义好了需要使用的数据，并在后台用java、Python写好了调用函数，这时候我们只需要使用后台写好的调用函数将数据库的数据信息传到前台显示即可。而如何从前台请求后台函数接口呢？这时便要用到js中常用的请求响应方法：Ajax+JSON。

```
1.ajax的概念：局部刷新技术。不是一门新技术，是多种技术的组合，是浏览器端的技术
2 为什么要使用ajax？
      传统的模式是：发送请求到服务器 ，服务器经过业务处理。返回一个页面给客户端浏览器。这样的做法，浪费资源，可能多次请求同一个页面资源。
      所以我们需要ajax。首先只需要请求一次页面，之后所有的数据交互都无需重新加载当前页面。
3.ajax的作用： 实现在当前结果页中显示其他请求的响应内容
4.ajax的使用：ajax的基本流程
            发送请求
            创建ajax引擎对象
            复写onreadystatement函数
                判断ajax状态码
            		判断响应状态码
            			获得响应内容
            				处理响应内容
                ajax的状态码
                   redayState:0,1,2,3,4//4:表示内容被成功接受
                   响应状态码：
                        200:表示一切ok
                        404：资源为找到
                        500：服务器繁忙
          ajax的异步和同步
               ajax.open(method,url,async);
               async:表示设置同步代码还是异步代码执
                      true:代表异步  默认异步
                      false:代表同步
```

```
JSON是一种数据交换格式，用于规范系统间的数据传输，易于人阅读和编写，同时也易于机器解析和生成，有效地提升网络传输效率。
```

在mui中，它自定义了ajax的格式要求，基本与普遍的ajax格式相似。若使用的后台系统自定义了json格式（如mgeids-boot系统中的Result.java文件便是自定义的数据传输格式），需注意数据输出时的方法。

以下为mui.ajax()的格式要求，我在请求成功的函数中放入了我所使用的系统定义的数据交换格式和获得的响应格式常用方法：

```js
mui.ajax(url,{//url是请求的接口地址
    //以下为处理函数，部分可省略，也可添加其他
	data:{//随地址传入的参数
		username:'username',//注意逗号
		password:'password'
	},
	dataType:'json',//服务器返回json格式数据
	type:'post',//HTTP请求类型，post或get
	timeout:10000,//超时时间设置为10秒；          
	success:function(data){//接口请求成功后，返回响应的参数
        //注意data格式，若返回的是mgeids-boot系统中Result格式参数:
        data.status, //解析接口状态,true或false
        data.msg, //解析提示文本
        data.data.total, //解析数据长度
        data.data.list //解析数据列表，这里才是真正的数据信息
		...
        //mgeds-boot响应成功后常用方法格式
        if(data.status){
            mui.each(data.data.list,function(i, item){
                ...//嵌套循环
            });
        }else{
            mui.alert(data.msg);
        }
	},
	error:function(){
		//异常处理；
		mui.alert('数据异常！');
	}
});
```

## 循环遍历

当获取到多条数据信息时，我们需要将每条数据信息都读出来并放入正确的位置，这就是循环遍历。一般我们在各种语言中使用for()，while()等方法进行循环遍历数组信息，而在mui中，它自定义了一种方法对数据进行循环遍历：mui.each()。

当ajax响应成功后，我们有了一条可能装有多个数据信息的json数据，这时就需用到mui.each()方法，对json数据进行循环遍历。这里我举个简单的使用mui.each()的数组遍历，遍历json的示例去目录中的完整示例查看。

示例：输出当前数组中每个元素的平方

```javascript
var array = [1,2,3]; //定义数组
mui.each(array,function(index,item){ 
    //function(index,item)为每个元素执行的回调函数；index表示当前元素的下标或key，item表示当前匹配元素
  console.log(item*item);
});
```

## 页面嵌套

当我们需要在js中定义某个样式，或是将在ajax中响应获取的数据放入某个样式中，并将这些定义的样式插入到我们将要显示的页面中时，我们就将使用到页面嵌套的方法了，

mui页面嵌套的方式与其他HTML文件基本相似，以下是两种嵌套方式：

### 1.HTML DOM innerHTML 属性

innerHTML在JS是双向功能：获取对象的内容 或 向对象插入内容；
如：

```html
<div id="a">这是内容</div>
<div id="b"></div>
```

我们可以通过 document.getElementById(‘a‘).innerHTML 来获取id为a的对象的内嵌内容；
也可以对某对象插入内容，如 

```javascript
var div = ’这是被插入的内容’;
document.getElementById(‘b’).innerHTML=div;
```

这样就能向id为b的对象插入内容。



### 2.原生js操作dom方法：insertAdjacentHTML

insertAdjacentHTML() 将指定的文本解析为HTML或XML，并将结果节点插入到DOM树中的指定位置。它不会重新解析它正在使用的元素，因此它不会破坏元素内的现有元素。这避免了额外的序列化步骤，使其比直接innerHTML操作更快。

mui示例：

```javascript
mui('name')[0].insertAdjacentHTML('position', div);
```

mui('name')[0]为mui的绑定元素方法，name是元素的标签名称，id，class。[]内为下标，从0开始。

position是相对于元素的位置，并且必须是以下字符串之一：
beforebegin: 元素自身的前面。
afterbegin: 插入元素内部的第一个子节点之前。
beforeend: 插入元素内部的最后一个子节点之后。
afterend: 元素自身的后面。

div是定义的嵌套到页面的内容。

## 完整示例，mui.ajax() + mui.each() + innerHTML

这是我基于资金系统的接口使用mui.ajax()成功的一个小案例，这里的data便是返回的json数据，在资金系统中已自定义了格式（Result.java）：

- data.status, //解析接口状态,true或false
- data.msg, //解析提示文本
- data.data.total, //解析数据长度
- data.data.list //解析数据列表，这里才是真正的数据信息

```javascript
//详情页面插入资金信息		
//ajaxUrl是我定义的系统地址，建议单独放在一个js文件中，方便修改
mui.ajax(ajaxUrl+'/rest/fund/fundReportController/getAssignDetailByAssign',{
	data:{fundAssignId: fundAssignId},
	dataType:'json',//服务器返回json格式数据
	timeout:10000,//超时时间设置为10秒；
	success:function(data){
		if (data.status) {
			var ftr = '<tr style="font-size: 20px;"><td>资金信息</td></tr>'
				 +'<tr><th style="width: 150px;">资金来源组织名称</th><th>资金属性</th>'
            	 +'<th style="width: 120px;">资金数额（万元）</th></tr>';
			mui.each(data.data.list, function(i, item){
				ftr +='<tr><td>'+item.fundOrganName+'</td><td>'
					+getfundType(item.fundProperty)+'</td><td>'
					+item.fundAmount+'</td></tr>';
			})
			fundBlockTable.innerHTML = ftr;
		}
	},
	error:function(){
		mui.alert("数据获取失败！","资金块信息");
	}
});
```

## 事件点击

当循环遍历输出列表信息时，我遇到了一个问题，所有的列表信息都是经过循环输出的，因此所有的列表中除去插入的数据其他都相同，那么当我点击某个列表中的按钮时，我怎么保证能获取该按钮所在的列表内容呢？毕竟所有的元素属性都是相同。

这时我们可使用onclick属性绑定按钮的点击事件。我们知道，onclick属性定义了一个触发函数，而在这个函数中，我们可将需要的参数一同传入函数中，因此当循环遍历时，我们将要用到的参数直接放入函数中，当点击后，可直接将参数传到所需要的函数中（注意当参数是字符时，需用引号一同传入）。

简单示例：

```javascript
<button type="button" onclick="yes('+item.fundAssignId+')">确认</button>
<button type="button" onclick="no(\''+item.fundName+'\')">取消</button>
<script type="text/javascript">
    function yes(object){//object即为点击传入的参数，方法中便可使用ajax。
    	...
	}
    function no(object){//当参数为String型时，需注意要用引号传入，在HTML中注意使用转义字符"\"
    	...
	}
</script>
```

