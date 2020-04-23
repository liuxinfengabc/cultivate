var constant = function () {
    //当前环境区分
    //1: 本地环境
    //2: 正式环境
    //3: 测试环境
    var environment = 1;

    if(environment==2){
        //正式环境
        baseUrl = "/";
    }else if(environment==3){
        //测试环境
        baseUrl = "/";
    }else{
        //本地环境
        baseUrl =  '/';
    }
    //三方（大数据
    var that = this;
    //jquery ajax函数
    var jqAjax = function (opt) {
        $.ajax({
            type: opt.type,
            url: opt.url,
            cache: false,
            dataType: opt.dataType || 'JSON',
            contentType: opt.contentType || "application/json; charset=utf-8",
            async: opt.async,
            data: opt.data || '',
            beforeSend: function (request) {
                if (opt.beforeSend) {
                    opt.beforeSend(request);
                }
                return request.setRequestHeader('Authorization', util.getCash('token') || '');
                //return request.setRequestHeader('Authorization', that.token || '');
            },
            success: function (result) {
                if(typeof(App)!='undefined'){
                    App.stopPageLoading();
                }
                opt.success(result);
            },
            error: function (err) {
                if(typeof(App)!='undefined'){
                    App.stopPageLoading();
                }
                if (JSON.parse(err.responseText).code == 401) {
                    layer.alert('登录信息超时', {
                        skin: 'layui-layer-lan', //
                        closeBtn: 1,
                        anim: 1
                    }, function(){
                        window.location.href = constant.url.imges.LoginIndex;
                    });
                }else if(JSON.parse(err.responseText).code == 412){
                    layer.alert('没有访问该接口的权限', {
                        skin: 'layui-layer-lan', //
                        closeBtn: 1,
                        anim: 1
                    });
                }else if(JSON.parse(err.responseText).code == 413){
                    layer.alert('不被支持的令牌', {
                        skin: 'layui-layer-lan', //
                        closeBtn: 1,
                        anim: 1
                    }, function(){
                        window.location.href = constant.url.imges.LoginIndex;
                    });
                    //constant.dialog.alertInfo("不被支持的令牌");
                }else if(JSON.parse(err.responseText).code == 414){
                    layer.alert('令牌格式错误', {
                        skin: 'layui-layer-lan', //
                        closeBtn: 1,
                        anim: 1
                    }, function(){
                        window.location.href = constant.url.imges.LoginIndex;
                    });
                    // constant.dialog.alertInfo("令牌格式错误");
                }else if(JSON.parse(err.responseText).code == 415){
                    layer.alert('令牌签名失败', {
                        skin: 'layui-layer-lan', //
                        closeBtn: 1,
                        anim: 1
                    }, function(){
                        window.location.href = constant.url.imges.LoginIndex;
                    });
                }else if(JSON.parse(err.responseText).code == 416){
                    layer.alert('错误的参数', {
                        skin: 'layui-layer-lan', //
                        closeBtn: 1,
                        anim: 1
                    }, function(){
                        window.location.href = constant.url.imges.LoginIndex;
                    });
                }else if(JSON.parse(err.responseText).code == 417){
                    layer.alert('没有接收到令牌', {
                        skin: 'layui-layer-lan', //
                        closeBtn: 1,
                        anim: 1
                    }, function(){
                        window.location.href = constant.url.imges.LoginIndex;
                    });
                }else if(JSON.parse(err.responseText).code == 418){
                    layer.alert(JSON.parse(err.responseText).message+'！', {
                        skin: 'layui-layer-lan', //
                        closeBtn: 1,
                        anim: 1
                    });
                    //constant.dialog.alertInfo("没有接收到令牌");
                }else if(JSON.parse(err.responseText).code == 444){
                    layer.alert('当前系统已关闭，请稍后再试！', {
                        skin: 'layui-layer-lan', //
                        closeBtn: 1,
                        anim: 1
                    }, function(){
                        window.location.href = constant.url.imges.LoginIndex;
                    });
                }else{
                    opt.error(err);
                }
            }
        });
    };
    var jqAjaxOption = function (type, arg) {
        if(typeof(App)!='undefined'){
            App.startPageLoading({animate: true});
        }
        var opt = {};
        if (!arg.url || !type) {
            if(typeof(App)!='undefined'){
                App.stopPageLoading();
            }
            return;
        }
        try{
            if (typeof arg.url == "string") {
                //按照字符串传输时单独组合
                opt.url = arg.url;
                opt.type = type;
                opt.success = arg.sf;
                opt.error = arg.ef;
                opt.beforeSend = arg.bf;
                if (arg.data) {
                    if (type.toUpperCase() == 'GET'|| !arg.restful) {
                        opt.contentType = "application/x-www-form-urlencoded";
                        opt.data = arg.data;
                    } else {
                        opt.data = JSON.stringify(arg.data);
                    }
                }
            } else {
                opt.url = arg.url.url;
                if (arg.url.async == undefined || arg.url.async == null) {
                    opt.async = true;
                } else {
                    opt.async = arg.url.async;
                }
                opt.type = type;
                opt.success = arg.url.success;
                opt.error = arg.url.error;
                opt.beforeSend = arg.url.beforeSend;
                if (arg.url.data) {
                    if (type.toUpperCase() == 'GET' || !arg.restful) {
                        opt.contentType = "application/x-www-form-urlencoded";
                        opt.data = arg.url.data;
                    } else {
                        opt.data = JSON.stringify(arg.url.data);
                    }
                }
            }
        }catch(ex){
            if(typeof(App)!='undefined'){
                App.stopPageLoading();
            }
            console.error(ex);
        }
        jqAjax(opt);
    };
    return {
        setToken: function (token) {
            that.token = token;
        },
        url: {
            sc_img: {
                //imgUpdate: baseScImage + '/api/common/imgUpdate',  //百度富文本 图片上传
                // imgUpdate: baseScResources + '/api/v1/ueditorImageUpdate',
                // ueditorConfig: baseScImage + '/api/common/ueditor',
                // videoUpdate: baseScImage + '/api/common/videoUpdate',    //百度富文本 视频上传
                // imgSubmit: baseScImage + '/api/common/imgSubmit',  //图片上传
                // imgDelete: baseScImage + '/api/common/imgDelete/',   //图片删除
            },
            imges: {
                // accountList: baseScAdmin + '/api/v1/sys/account/',    //管理账户
                LoginIndex: baseUrl +'rest/imges/imgesHomeController',
                login: baseUrl +'rest/rbac/loginController/login'
            },
            nmmine: {
                // accountList: baseScAdmin + '/api/v1/sys/account/',    //管理账户
                LoginIndex: baseUrl +'rest/nmmine/nmmineHomeController',
                login: baseUrl +'rest/rbac/loginController/login'
            }
        },
        ajax: {
            post: function (url, data, sf, ef, bf) {
                jqAjaxOption('post', {url: url, data: data, sf: sf, ef: ef, bf: bf,restful:true});
            },
            postAjax:function(url,data,sf,ef,bf){
                jqAjaxOption('post',{url:url,data:data,sf:sf,ef:ef,bf:bf,restful:false});
            },
            get: function (url, data, sf, ef, bf) {
                jqAjaxOption('get', {url: url, data: data, sf: sf, ef: ef, bf: bf,restful:false});
            },
            getAjax:function(url, data, sf, ef, bf){
                //为了和postAjax对照，逻辑同ajax.get
                jqAjaxOption('get', {url: url, data: data, sf: sf, ef: ef, bf: bf,restful:false});
            },
            put: function (url, data, sf, ef, bf) {
                jqAjaxOption('put', {url: url, data: data, sf: sf, ef: ef, bf: bf,restful:true});
            },
            del: function (url, data, sf, ef, bf) {
                jqAjaxOption('delete', {url: url, data: data, sf: sf, ef: ef, bf: bf,restful:true});
            },
            upload: function (url,data, sf, ef, bf,async) {
                //上传图片和视频
                if(typeof(App)!='undefined'){
                    App.startPageLoading({animate: true});
                }

                if(async==null||async==undefined){
                    async = false;
                }
                $.ajax({
                    type: "POST",
                    url: url,
                    enctype: "multipart/form-data",
                    contentType: false,
                    processData: false,
                    beforeSend: function (request) {
                        return request.setRequestHeader('Authorization', util.getCash('token') || '');
                    },
                    cache: false,
                    async: async,
                    data: data,
                    success: function (result) {
                        if(typeof(App)!='undefined'){
                            App.stopPageLoading();
                        }

                        sf(result);
                    },
                    error: function (err) {
                        if(typeof(App)!='undefined'){
                            App.stopPageLoading();
                        }
                        ef(err);
                    }
                });
            }
        },
        datatables: {
            // DataTables初始化选项
            default_option: {
                language: {
                    "sProcessing": "处理中...",
                    "sLengthMenu": "每页 _MENU_ 项",
                    "sZeroRecords": "没有匹配结果",
                    "sInfo": "当前显示第 _START_ 至 _END_ 项，共 _TOTAL_ 项。",
                    "sInfoEmpty": "当前显示第 0 至 0 项，共 0 项",
                    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                    "sInfoPostFix": "",
                    "sSearch": "搜索:",
                    "sUrl": "",
                    "sEmptyTable": "系统暂未收录相关信息",
                    "sLoadingRecords": "载入中...",
                    "sInfoThousands": ",",
                    "oPaginate": {
                        "sFirst": "首页",
                        "sPrevious": "上页",
                        "sNext": "下页",
                        "sLast": "末页",
                        "sJump": "跳转"
                    },
                    "oAria": {
                        "sSortAscending": ": 以升序排列此列",
                        "sSortDescending": ": 以降序排列此列"
                    }
                },
                autoWidth: false, // 禁用自动调整列宽
                order: [], // 取消默认排序查询
                lengthChange: false,
                renderer: "bootstrap",
                pageLength: 10,//每页显示多少条数据
                sort: false,//不排序
                pagingType: "full_numbers", // 分页样式：simple,simple_numbers,full,full_numbers
                processing: false, // 隐藏加载提示,自行处理
                serverSide: true, // 启用服务器端分页
                searching: false,// 禁用原生搜索
                destroy:true//Cannot reinitialise DateTable,解决重新加载表格内容问题
            },
            columns: {
                checkbox: { // 复选框单元格
                    className: "td-checkbox",
                    orderable: false,
                    width: "30px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<input type="checkbox" class="iCheck">';
                    }
                },
                radio: {
                    className: "td-radio",
                    orderable: false,
                    width: "30px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<input type="radio" class="iCheck">';
                    }
                }
            },
            render: {
                //文本显示不开专用渲染函数，挤压部分显示...
                ellipsis: function (data, type, row, meta) {
                    data = data || "";
                    return '<span title="' + data + '">' + data + '</span>';
                }
            }
        },
        dialog: {
            /*
                功能：错误提示
                参数：
                - title：提示的标题（选填）
                - mag：提示的内容（选填）
            */
            alertError: function (title, msg) {
                var thisTitle = title || '出错了,请稍后尝试';
                var thisMsg = msg || '';
                swal(thisTitle, thisMsg, 'error');
            },
            /*
                功能：警告提示
                参数：
                - title：提示的标题（必填）
            */
            alertInfo: function (title, msg) {
                var thisTitle = title || '';
                var thisMsg = msg || '';
                swal(title, msg, 'warning');
            },

            /*
                功能：成功提示
                参数：
                    - title：提示的标题（选填）
            */
            alertSuccess: function (title, msg) {
                var thisTitle = title || '操作成功';
                var thisMsg = msg || '';
                swal(title, msg, 'success');
            },
            /*
                功能：确认提示（有回调函数）
                参数：
                - title：提示框标题（必填）
                - fn：确认按钮点击回调（必填）
            */
            alertConfirm: function (title, fn) {
                swal({
                    'title': title,             //标题
                    'text': '',               //标题
                    'type': 'question',                 //alert类型
                    'showCancelButton': true,          //如果设置为true，“取消”按钮将会显示，用户可以点击取消按钮关闭弹窗。
                    'confirmButtonColor': '#3085d6',   //使用该参数来修改“确认”按钮的背景颜色（必须是十六进制值）。
                    'cancelButtonColor': '#d33',       //使用该参数来修改“确认”按钮的显示文本。
                    'confirmButtonText': '确定',       //使用该参数来修改“确认”按钮的显示文本。
                    'cancelButtonText': '取消',        //使用该参数来修改“取消”按钮的显示文本。
                    'confirmButtonClass': 'btn-confirm btn btn-success',    //可以为“确认”按钮设置自定义类
                    'cancelButtonClass': 'btn btn-danger',                  //可以为“取消”按钮设置自定义类
                    'buttonsStyling': false,    //默认按钮样式使用swal2的样式，如果你想要使用自己的CSS类（例如Bootstrap类），将该参数设置为false。
                    'allowOutsideClick': false,
                    'preConfirm': fn
                });
            }
        },
        func: {
            //常用函数
            replaceTags: function (content) {
                content = content || '';
                content = content.replace(/<\/?.+?>/g, "");
                return content.replace(/ /g, "");
            },
            onImgError: function(url){
                var img = event.srcElement;
                img.src = url;
                img.onerror = null;
            }
        },
        // parenturl:'http://'+document.domain+':8081'
        parenturl:'http://'+document.location.host
    };
}();

var util = function () {
    return {
        isNull: function (obj) {
            return obj == undefined || !obj;
        },
        isString: function (obj) {
            return typeof obj == 'string';
        },
        isEmpty: function (obj) {
            if (typeof obj == "undefined" || obj == null || obj == "" || $.trim(obj) == "") {
                return true;
            } else {
                return false;
            }
        },
        fromJson: function (json) {
            return JSON.stringify(json);
        },
        trim: function (str) {
            if (!str)
                return str;
            return str.replace(/(^\s*)|(\s*$)/g, '');
        },
        setCash: function (name, value) {
            if (util.isNull(value))
                return;
            if (!util.isString(value))
                value = util.fromJson(value);
            value = util.trim(value);
            localStorage.setItem(name, value);
        },
        getCash: function (name) {
            var value = localStorage.getItem(name);
            if (util.isNull(value))
                return null;
            if (value.length > 1
                && (value[0] == '{' || value[0] == '[')
                && (value[value.length - 1] == '}' || value[value.length - 1] == ']'))
                value = JSON.parse(value);
            return value;
        },
        removeCash: function (name) {
            localStorage.removeItem(name);
        },
        //时间如果为单位数补0
        fixZero: function (num, length) {
            var str = "" + num;
            var len = str.length;
            var s = "";
            for (var i = length; i-- > len;) {
                s += "0";
            }
            return s + str;
        },
        formatDate: function (now) {
            var now = new Date(now);
            var year = now.getFullYear();
            var month = now.getMonth() + 1;
            var date = now.getDate();
            var hour = now.getHours();
            var minute = now.getMinutes();
            var second = now.getSeconds();
            return year + "年" + fixZero(month, 2) + "月" + fixZero(date, 2) + "日" + fixZero(hour, 2) + ":" + fixZero(minute, 2) + ":" + fixZero(second, 2);
        },
        isCardId: function (code) {
            var city = {
                11: "北京",
                12: "天津",
                13: "河北",
                14: "山西",
                15: "内蒙古",
                21: "辽宁",
                22: "吉林",
                23: "黑龙江",
                31: "上海",
                32: "江苏",
                33: "浙江",
                34: "安徽",
                35: "福建",
                36: "江西",
                37: "山东",
                41: "河南",
                42: "湖北",
                43: "湖南",
                44: "广东",
                45: "广西",
                46: "海南",
                50: "重庆",
                51: "四川",
                52: "贵州",
                53: "云南",
                54: "西藏",
                61: "陕西",
                62: "甘肃",
                63: "青海",
                64: "宁夏",
                65: "新疆",
                71: "台湾",
                81: "香港",
                82: "澳门",
                91: "国外"
            };
            var tip = "";
            var pass = true;

            if (!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)) {
                tip = "身份证号格式错误";
                pass = false;
            }

            else if (!city[code.substr(0, 2)]) {
                tip = "地址编码错误";
                pass = false;
            }
            else {
                //18位身份证需要验证最后一位校验位
                if (code.length == 18) {
                    code = code.split('');
                    //∑(ai×Wi)(mod 11)
                    //加权因子
                    var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
                    //校验位
                    var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
                    var sum = 0;
                    var ai = 0;
                    var wi = 0;
                    for (var i = 0; i < 17; i++) {
                        ai = code[i];
                        wi = factor[i];
                        sum += ai * wi;
                    }
                    var last = parity[sum % 11];
                    if (parity[sum % 11] != code[17]) {
                        tip = "校验位错误";
                        pass = false;
                    }
                }
            }
            // if (!pass) alert(tip);
            return pass;
        },
        isMobile: function (phonenum) {
            if (!(/^1(3|4|5|7|8)\d{9}$/.test(phonenum))) {
                return false;
            } else {
                return true;
            }
        },
        isFixMobile: function (fixnum) {
            if (!/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test(fixnum)) {
                return false;
            }
        },
        isMail: function (mail) {
            var myReg = /^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;
            var tip = '';
            if (myReg.test(mail)) {
                return true;
            } else {
                tip = "邮箱格式不对!";
                return false;
            }
        },
        isAccount: function (username) {
            var re = /^[a-zA-z]\w{3,15}$/;
            if (re.test(str)) {
                return true;
            } else {
                return false;
            }
        },
        GetUrlParam: function (paraName) {
            var url = document.location.toString();
            var arrObj = url.split("?");
            if (arrObj.length > 1) {
                var arrPara = arrObj[1].split("&");
                var arr;
                for (var i = 0; i < arrPara.length; i++) {
                    arr = arrPara[i].split("=");

                    if (arr != null && arr[0] == paraName) {
                        return arr[1];
                    }
                }
                return "";
            }
            else {
                return "";
            }

        }

    }
}();

//数据字典
// var dic = function () {
//     var dics;
//
//     $.ajax({
//         async: false,
//         type: "get",
//         url: constant.url.sc_admin.dic,
//         dataType: 'JSON',
//         contentType: "application/json; charset=utf-8",
//         success: function (result) {
//             if (result.code == "200") {
//                 dics = (result.data.list);
//             }
//         }
//     });
//
//     return {
//         getByTypeId: function (groupId) {
//             var dictypes;
//             dictypes = dics.filter(function (item) {
//                 return item.type == groupId;
//             });
//             return dictypes;
//         },
//         getByDictId: function (groupId, dictId) {
//             var dictypeids;
//             dictypeids = dics.filter(function (item) {
//                 return (item.type == groupId) && (item.name == dictId);
//             });
//             return dictypeids[0];
//         },
//         getByDictValue: function (groupId, dictValue) {
//             var dictypevalues;
//             dictypevalues = dics.filter(function (item) {
//                 return (item.type == groupId) && (item.code == dictValue);
//             });
//             return dictypevalues[0];
//         }
//     }
// }();

//自定义 返回结果null-->''
var _selffn = function(){
    var flashglg;
    return {
        jsonNullToString : function(obj){
            if(obj){
                if(JSON.stringify(obj).substr(0,1) =="{" && !obj.list){
                    for(attr in obj)if(!obj[attr])obj[attr]='';
                    return obj;
                }else if(obj.length >=0 ){
                    if(obj.length > 0){
                        obj.forEach(function(data,i){
                            for(attr in data)if(!data[attr])data[attr]=''
                        });
                    }
                    return obj;
                }else if(obj.list){
                    if(obj.list.length > 0){
                        obj.list.forEach(function(data,i){
                            for(attr in data)if(!data[attr])data[attr]=''
                        });
                    }
                    return obj;
                }
            }else{
                return false;
            }
        },
        jsonNullToStringOfList : function(obj){
            if(obj && obj != true){
                if(JSON.stringify(obj).substr(0,1) =="{" && !obj.list){
                    for(attr in obj)if(!obj[attr])obj[attr]='';
                    return [obj];
                } else if (obj.length >=0 ) {
                    if (obj.length > 0) {
                        obj.forEach(function (data, i) {
                            for (attr in data) if (!data[attr]) data[attr] = '';
                        });
                    }
                    return obj;
                } else if (obj.list) {
                    if (obj.list.length > 0) {
                        obj.list.forEach(function (data, i) {
                            for (attr in data) if (!data[attr]) data[attr] = '';
                        });
                    }
                    return obj.list;
                }
            }else{
                return [];
            }
        },
        jsonNullToZeroOfList : function(obj){
            if(obj && obj != true){
                if(JSON.stringify(obj).substr(0,1) =="{" && !obj.list){
                    for(attr in obj)if(!obj[attr])obj[attr]=0;
                    return [obj];
                } else if (obj.length >=0 ) {
                    if (obj.length > 0) {
                        obj.forEach(function (data, i) {
                            for (attr in data) if (!data[attr]) data[attr] = 0;
                        });
                    }
                    return obj;
                } else if (obj.list) {
                    if (obj.list.length > 0) {
                        obj.list.forEach(function (data, i) {
                            for (attr in data) if (!data[attr]) data[attr] = 0;
                        });
                    }
                    return obj.list;
                }
            }else{
                return [];
            }
        },
        setfalshcircle : function(flash){
            flashglg = flash;
        },
        getfalshcircle : function(){
            return flashglg;
        }

    }
}();

//表单验证
function form_validation(_this,type) {
    var layer_val=$(_this).val();
    var util_adopt;
    if (type=='CardId'){
        util_adopt=util.isCardId(layer_val)
        tip='身份证号填写错误';
    }else if (type=='Mobile'){
        util_adopt=util.isMobile(layer_val);
        tip = "手机号格式不正确";
    }else if (type=='FixMobile'){
        util_adopt=util.isFixMobile(layer_val);
        tip = "电话号码格式不正确";
    }else if (type=='Mail'){
        util_adopt=util.isMail(layer_val);
        tip = "邮箱格式错误";
    }else if (type=='Account'){
        util_adopt=util.isAccount(layer_val);
        tip = "账户名格式错误";
    }
    if (util_adopt){
        $(_this).removeAttr('lay-cont');
    } else {
        if (!util_adopt) layer.tips(tip,$(_this),{
            tips:4,
            tipsMore: true
        });
        $(_this).attr('lay-cont',-1);
    }
    return util_adopt;
}


//表单提交验证
function lay_submit(_this) {
    var breakEach=true;
    $(_this).parent().siblings().find('input.form_submits').each(function (i,ele) {
        if ($(ele).val()==''){
            $(ele).focus();
            constant.dialog.alertInfo("请将信息填写完整！");
            breakEach=false;
            return breakEach;
        }
    })
    if (breakEach){
        $(_this).parent().siblings().find('input.form_submits').each(function (i,ele) {
            if ($(ele).attr('lay-cont')==-1){
                layer.msg('请输入正确的信息并提交',{},function() {
                    $(ele).focus();
                });
                breakEach=false;
                return breakEach;
            }
        })
    }
    return breakEach;
}


function lay_submits(_this) {
    var breakEach=true;
    $(_this).parents('form').siblings('form').find('input.form_submits').each(function (i,ele) {
        if ($(ele).val()==''){
            $(ele).focus();
            constant.dialog.alertInfo("请将信息填写完整！");
            breakEach=false;
            return breakEach;
        }
    })
    if (breakEach) {
        $(_this).parent().siblings().find('input.form_submits').each(function (i, ele) {
            if ($(ele).val() == '') {
                $(ele).focus();
                constant.dialog.alertInfo("请将信息填写完整！");
                breakEach = false;
                return breakEach;
            }
        })
    }
    if (breakEach){
        $(_this).parents('form').siblings('form').find('input.form_submits').each(function (i,ele) {
            if ($(ele).attr('lay-cont')==-1){
                layer.msg('请输入正确的信息并提交',{},function() {
                    $(ele).focus();
                });
                breakEach=false;
                return breakEach;
            }
        })
    }
    if (breakEach){
        $(_this).parent().siblings().find('input.form_submits').each(function (i,ele) {
            if ($(ele).attr('lay-cont')==-1){
                layer.msg('请输入正确的信息并提交',{},function() {
                    $(ele).focus();
                });
                breakEach=false;
                return breakEach;
            }
        })
    }
    return breakEach;
}


//IE 不支持 filter 解决方案
if (!Array.prototype.filter){
    Array.prototype.filter = function(fun){
        if (this === void 0 || this === null)
            throw new TypeError();

        var t = Object(this);
        var len = t.length >>> 0;
        if (typeof fun !== "function")
            throw new TypeError();

        var res = [];
        var thisArg = arguments.length >= 2 ? arguments[1] : void 0;
        for (var i = 0; i < len; i++){
            if (i in t){
                var val = t[i];
                if (fun.call(thisArg, val, i, t))
                    res.push(val);
            }
        }
        return res;
    };
}

//IE 不支持 MAP 解决方案
if (!Array.prototype.map) {
    Array.prototype.map = function(callback, thisArg) {
        var T, A, k;
        if (this == null) {
            throw new TypeError(" this is null or not defined");
        }
        var O = Object(this);
        var len = O.length >>> 0;
        if (typeof callback !== "function") {
            throw new TypeError(callback + " is not a function");
        }
        if (thisArg) {
            T = thisArg;
        }
        A = new Array(len);
        k = 0;
        while(k < len) {
            var kValue, mappedValue;
            if (k in O) {
                kValue = O[ k ];
                mappedValue = callback.call(T, kValue, k, O);
                A[ k ] = mappedValue;
            }
            k++;
        }
        return A;
    };
}
layer.config({
	extend: 'blue/style.css', //加载您的扩展样式
	skin: 'layui-ext-blue'
});