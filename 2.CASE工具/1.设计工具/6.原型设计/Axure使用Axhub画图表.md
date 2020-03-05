

## 如何在Axure 内使用Axhub Charts

下载：访问 https://axhub.im/charts/ 通过侧边栏下载 Axhub Charts 元件库

导入：在 Axure 内的 Libraries 面板，通过菜单导入下载的元件库





![img](Axure使用Axhub画图表.assets/u3.png)

如何使用Axhub Charts的图表

#### **使用：**

在 Axure 内的 Libraries 面板选择对应图表，拖拽入目标页面即可，预览或生成即可看到图表。



#### **更改图表高宽：**

你只需更改元件高宽，生成的图表会自动响应式更改高宽。



#### **更改图表数据：**

每个图表元件，都有一个对应的data中继器，更改该中继器的数据，即可更改生成图表的数据。



#### **更改图表的背景：**

图表元件的背景是透明的，您可以自由为图表增加一层背景。此外，还可以通过图表对应的config中继器更改主题，目前提供了default主题适合亮色背景，dark主题时候暗色背景。



**更改图表样式：**

每个图表元件，都有一个对应的config中继器，可以通过更改这里的配置项来控制图表样式，目前有以下几个可配置项（如需增加可通过axhub公众号反馈）：

theme：主题颜色，可选 default 和 dark（适合深色背景）

showLegend：是否显示图例标记，可选auto（多于1项显示）、true（显示）、false（不显示）

showAxis：是否显示坐标轴，可选true（显示）、false（不显示）

showTooltips：是否显示提示，true（显示）、false（不显示）

labelType：饼图类标签显示方式，可选default（默认）、inner（显示在扇形内）、none（不显示）

chartColor：自定义图表颜色，多个用,分隔，例如#0050B3, #1890FF, #40A9FF，元件会按顺序取色

formHeader ：自定义中继器表头，解决表头不能输入中文的问题，每列用,分隔，

​              例如 月份,东京,伦敦 （效果见下文）

xSticks：自定义横坐标刻度的数量和值，多个用,分隔

ySticks：自定义纵坐标刻度的数量和值，多个用,分隔

showText：是否显示图形中间标签文本，true（显示）、false（不显示）



#### **实现动态图表：**

你可以通过Axure的事件（Case）来更新图表对应的data中继器数据，图表本身也会自动更新，效果如下：

![image-20200305195848221](Axure使用Axhub画图表.assets/image-20200305195848221.png)

#### 常见问题

##### **为什么预览或生成后没有自动生成图表？**

首先请检查你的网络，Axhub Charts需要加载在线的antv或echarts的库文件来渲染图表

其次请确保不要改变元件分组Group内的axhub-xxx-chart、axhub-xxx-data、axhub-xxx-config的子元件名称，此外，且不要随意删除或移动这些子元件（Group的元件名称是可以修改的）。



##### **能否更改axhub-xxx-chart元件的内容？**

可以的，**axhub-xxx-chart** 的文本，边框，背景色都是可以随意编辑。会影响到图表生成的，只有该元件的高宽及可见性。



##### **有大量数据时，Axure中继器的数据编辑起来效率不高，有办法么？**

中继器支持重excel表格直接复制数据哦~



##### **中继器表头不支持输入中文，怎么办？**

V0.2.0版本的config利增加了formHeader配置项，解决表头不能输入中文的问题，值每列用,分隔，例如 月份,东京,伦敦 ，下面是折现统计图的效果

![image-20200305195952864](Axure使用Axhub画图表.assets/image-20200305195952864.png)



##### **能否更改图表字体、坐标轴或者图形的颜色**

图形颜色可以通过config里的chartColor配置项自定义，坐标轴刻度可以通过xSticks、ySticks配置项自定义。

其他配置后续会考虑在config开放，目前该阶段可以使用config关闭原有坐标轴，再加上自己的坐标轴的方式来实现。关闭后的图形如下：



![image-20200305200020533](Axure使用Axhub画图表.assets/image-20200305200020533.png)

