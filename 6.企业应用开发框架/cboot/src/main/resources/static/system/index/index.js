//JavaScript代码区域
layui.use(['element'], function () {
    var element = layui.element;
    // var layer = layui.layer;
    var $ = layui.$;
    // 配置tab实践在下面无法获取到菜单元素
    $('.subMenu').on('click', function () {
        var dataid = $(this);
        //这时会判断右侧.layui-tab-title属性下的有lay-id属性的li的数目，即已经打开的tab项数目
        if ($(".layui-tab-title li[lay-id]").length <= 0) {
            //如果比零小，则直接打开新的tab项
            active.tabAdd(dataid.attr("data-url"), dataid.attr("data-id"), dataid.attr("data-title"));
        } else {
            //否则判断该tab项是否以及存在
            var isData = false; //初始化一个标志，为false说明未打开该tab项 为true则说明已有
            $.each($(".layui-tab-title li[lay-id]"), function () {
                //如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
                if ($(this).attr("lay-id") == dataid.attr("data-id")) {
                    isData = true;
                }
            })
            if (isData == false) {
                //标志为false 新增一个tab项
                active.tabAdd(dataid.attr("data-url"), dataid.attr("data-id"), dataid.attr("data-title"));
            }
        }
        //最后不管是否新增tab，最后都转到要打开的选项页面上
        active.tabChange(dataid.attr("data-id"));
    });

    var active = {
        //在这里给active绑定几项事件，后面可通过active调用这些事件
        tabAdd: function (url, id, name) {
            //新增一个Tab项 传入三个参数，分别对应其标题，tab页面的地址，还有一个规定的id，是标签中data-id的属性值
            //关于tabAdd的方法所传入的参数可看layui的开发文档中基础方法部分
            element.tabAdd('demo', {
                title: name,
                content: '<iframe data-frameid="' + id + '" scrolling="auto" frameborder="0" src="' + url + '" style="width:100%;height:99%;"></iframe>',
                id: id //规定好的id
            })
            FrameWH();  //计算ifram层的大小
        },
        tabChange: function (id) {
            //切换到指定Tab项
            element.tabChange('demo', id); //根据传入的id传入到指定的tab项
        },
        tabDelete: function (id) {
            element.tabDelete("demo", id);//删除
        }
    };
    function FrameWH() {
        var h = $(window).height();
        $("iframe").css("height",h+"px");
    }
});