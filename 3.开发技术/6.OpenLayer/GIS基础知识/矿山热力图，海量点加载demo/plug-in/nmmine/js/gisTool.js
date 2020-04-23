//创建一个WGS84球体对象
var wgs84Sphere = new ol.Sphere(6378137);
//创建一个当前要绘制的对象
var sketch = new ol.Feature();
//创建一个帮助提示框对象
var helpTooltipElement;
//创建一个帮助提示信息对象
var helpTooltip;
//创建一个测量提示框对象
var measureTooltipElement;
//创建一个测量提示信息对象
var measureTooltip;
//继续绘制多边形的提示信息
var continuePolygonMsg = '单击可继续绘制多边形，双击完成绘制';
//继续绘制线段的提示信息
var continueLineMsg = '单击可继续绘制多边形，双击完成绘制';

 //格式化测量长度
 function (line) {
		var coordinates = line.getCoordinates();//获取坐标串
		var length = 0;//定义长度变量
		var sourceProj = map.getView().getProjection(); //获取源数据的坐标系
		for (var i = 0, ii = coordinates.length - 1; i < ii; ++i) {
			var c1 = ol.proj.transform(coordinates[i], sourceProj, 'EPSG:4326');//第一个点
			var c2 = ol.proj.transform(coordinates[i + 1], sourceProj, 'EPSG:4326');//第二个点
			length += wgs84Sphere.haversineDistance(c1, c2);//获取转换后的球面距离
		}
		var output;
		if (length > 100) {
			output = (Math.round(length / 1000 * 100) / 100) +
				' ' + 'km';
		} else {
			output = (Math.round(length * 100) / 100) +
				' ' + 'm';
		}
		return output;
};
//格式化测量长度
function (polygon) {
            var sourceProj = map.getView().getProjection(); //获取初始坐标系
            var geom = /** @type {ol.geom.Polygon} */(polygon.clone().transform(
                sourceProj, 'EPSG:4326'));
            var coordinates = geom.getLinearRing(0).getCoordinates();
            coords = coordinates;
            var area = Math.abs(wgs84Sphere.geodesicArea(coordinates));
            var output;
            if (area > 10000) {
                output = (Math.round(area / 1000000 * 100) / 100) +
                    ' ' + 'km<sup>2</sup>';
            } else {
                output = (Math.round(area * 100) / 100) +
                    ' ' + 'm<sup>2</sup>';
            }
            return output;
     };


//添加交互式绘图对象的函数
 function addInteraction(type) {
            //创建一个交互式绘图对象
            draw = new ol.interaction.Draw({
                //绘制的数据源
                source: source,
                //绘制类型
                type: (type),
                //样式
                style: new ol.style.Style({
                    fill: new ol.style.Fill({
                        color: 'yellow'
                    }),
                    stroke: new ol.style.Stroke({
                        color: 'rgb(0,0,255)',
                        lineDash: [10, 10],
                        width: 2
                    }),
                    image: new ol.style.Circle({
                        radius: 5,
                        stroke: new ol.style.Stroke({
                            color: 'red '
                        }),
                        fill: new ol.style.Fill({
                            color: 'rgba(255, 255, 255, 1)'
                        })
                    })
                })
            });
};


 //创建帮助提示框
    function createHelpTooltip() {
            //如果已经存在帮助提示框则移除
            if (helpTooltipElement) {
                         helpTooltipElement.parentNode.removeChild(helpTooltipElement);
            }
            //创建帮助提示要素的div
            helpTooltipElement = document.createElement('div');
            //设置帮助提示要素的样式
            helpTooltipElement.className = 'tooltip hidden';
            helpTooltipElement.style.color = 'black';
            //创建一个帮助提示的覆盖标注
            helpTooltip = new ol.Overlay({
                 element: helpTooltipElement,
                 offset: [5, 0],
                 positioning: 'top-left'
            });
            map.addOverlay(helpTooltip);
    }

    //创建测量提示框
    function createMeasureTooltip() {
            if (measureTooltipElement) {
                    measureTooltipElement.parentNode.removeChild(measureTooltipElement);
                }
            measureTooltipElement = document.createElement('div');
            measureTooltipElement.className = 'tooltip tooltip-measure';
            measureTooltipElement.style.color = 'black';
            measureTooltip = new ol.Overlay({
                element: measureTooltipElement,
                offset: [0, 10],
                positioning: 'top-left'
            });
            map.addOverlay(measureTooltip);
    }