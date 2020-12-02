
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>高德地图</title>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
    <script src="plug-in/ns/js/jquery-3.2.0.min.js"></script>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
    <script type="text/javascript"
            src="https://webapi.amap.com/maps?v=1.4.15&key=bb728955845400927a76992ac35b73d7&plugin=AMap.DistrictSearch,AMap.MarkerClusterer"></script>
    <script type="text/javascript" src="https://a.amap.com/jsapi_demos/static/demo-center/js/jquery-1.11.1.min.js" ></script>
    <script type="text/javascript" src="https://a.amap.com/jsapi_demos/static/demo-center/js/underscore-min.js" ></script>
    <script type="text/javascript" src="https://a.amap.com/jsapi_demos/static/demo-center/js/backbone-min.js" ></script>
    <script type="text/javascript" src='https://a.amap.com/jsapi_demos/static/demo-center/js/prety-json.js'></script>
    <script src="//a.amap.com/jsapi_demos/static/china.js"></script>
    <script type="text/javascript" src="https://webapi.amap.com/demos/js/liteToolbar.js"></script>
    <style>
        html, body, #container {
            margin: 0;
            height: 100%;
        }

        .input-item-text {
            width: 7rem;
        }
    </style>
</head>
<body>
<div id="container"></div>

<div class="input-card" style="z-index: 99999999999;">
    <h4>下属行政区查询</h4>
    <div class="input-item">
        <div class="input-item-prepend"><span class="input-item-text" >省市区</span></div>
        <select id='province' style="width:100px" onchange='search(this)'></select>
    </div>
    <div class="input-item">
        <div class="input-item-prepend"><span class="input-item-text" >地级市</span></div>
        <select id='city' style="width:100px" onchange='search(this)'></select>
    </div>
    <div class="input-item">
        <div class="input-item-prepend"><span class="input-item-text" >区县</span></div>
        <select id='district' style="width:100px" onchange='search(this)'></select>
    </div>
    <div class="input-item">
        <div class="input-item-prepend"><span class="input-item-text" >街道</span></div>
        <select id='street' style="width:100px" onchange='setCenter(this)'></select>
    </div>
    <h4>语言切换</h4>
    <div id="lang">
        <div class="input-item"><input id="en" name="language" type="radio" checked="checked"><span class="input-text">英文底图</span>
        </div>
        <div class="input-item"><input id="zh_en" name="language" type="radio"><span class="input-text">中英文对照</span>
        </div>
        <div class="input-item"><input id="zh_cn" name="language" type="radio"><span class="input-text">中文底图</span>
        </div>
    </div>
</div>

<script type="text/javascript">
    var map, district, polygons = [], citycode;
    var citySelect = document.getElementById('city');
    var districtSelect = document.getElementById('district');
    var areaSelect = document.getElementById('street');

    map = new AMap.Map('container', {
        resizeEnable: true,
        zoom: 4,
        center: [112.850953, 32.871318]
    });
    //行政区划查询
    var opts = {
        subdistrict: 1,   //返回下一级行政区
        showbiz:false  //最后一级返回街道信息
    };
    district = new AMap.DistrictSearch(opts);//注意：需要使用插件同步下发功能才能这样直接使用
    district.search('中国', function(status, result) {
        if(status=='complete'){
            getData(result.districtList[0]);
        }
    });
    function getData(data,level) {
        var bounds = data.boundaries;
        if (bounds) {
            for (var i = 0, l = bounds.length; i < l; i++) {
                var polygon = new AMap.Polygon({
                    map: map,
                    strokeWeight: 1,
                    strokeColor: '#0091ea',
                    fillColor: '#80d8ff',
                    fillOpacity: 0.2,
                    path: bounds[i]
                });
                polygons.push(polygon);
            }
            map.setFitView();//地图自适应
        }

        //清空下一级别的下拉列表
        if (level === 'province') {
            citySelect.innerHTML = '';
            districtSelect.innerHTML = '';
            areaSelect.innerHTML = '';
        } else if (level === 'city') {
            districtSelect.innerHTML = '';
            areaSelect.innerHTML = '';
        } else if (level === 'district') {
            areaSelect.innerHTML = '';
        }

        var subList = data.districtList;
        if (subList) {
            var contentSub = new Option('--请选择--');
            var curlevel = subList[0].level;
            var curList =  document.querySelector('#' + curlevel);
            curList.add(contentSub);
            for (var i = 0, l = subList.length; i < l; i++) {
                var name = subList[i].name;
                var levelSub = subList[i].level;
                var cityCode = subList[i].citycode;
                contentSub = new Option(name);
                contentSub.setAttribute("value", levelSub);
                contentSub.center = subList[i].center;
                contentSub.adcode = subList[i].adcode;
                curList.add(contentSub);
            }
        }

    }
    function search(obj) {
        //清除地图上所有覆盖物
        for (var i = 0, l = polygons.length; i < l; i++) {
            polygons[i].setMap(null);
        }
        var option = obj[obj.options.selectedIndex];
        var keyword = option.text; //关键字
        var adcode = option.adcode;
        district.setLevel(option.value); //行政区级别
        district.setExtensions('all');
        //行政区查询
        //按照adcode进行查询可以保证数据返回的唯一性
        district.search(adcode, function(status, result) {
            if(status === 'complete'){
                getData(result.districtList[0],obj.id);
            }
        });
    }
    function setCenter(obj){
        map.setCenter(obj[obj.options.selectedIndex].center)
    }

    // 语言切换
    var radios = document.querySelectorAll("#lang input");
    radios.forEach(function (ratio) {
        ratio.onclick = setLang;
    });

    function setLang() {
        map.setLang(this.id);
    }
</script>
<script type="text/javascript" src="https://webapi.amap.com/demos/js/liteToolbar.js"></script>

</body>
</html>