(function (window, ol, $, factory) {
    "use strict";
    var Efgis = factory(window, ol, $);
    window.efgis = Efgis;
})(window, ol, $, function (window, ol, $) {
    ol.source.Cluster.prototype.setDistance = function (dis) {
        this.ta = dis;
        this.getSource().changed();
    };
    var Efgis = function (selector, setting) {
        if (window.commonWebgis) {
            if (window.commonWebgis.hasOwnProperty('_map')) {
                if (window.commonWebgis._map != null) {
                    window.commonWebgis._map.setTarget(null);
                    window.commonWebgis._map = null;
                }
            }
        }
        var commonWebgis = new Efgis.fn._init(selector, setting);
        window.commonWebgis = commonWebgis;
        return commonWebgis;
    };

    //设置常量
    function _setconst() {
        Efgis.prototype._srsNameMap = 'EPSG:3857';
        Efgis.prototype._srsNameDataSource = 'EPSG:4326';
        // 127.0.0.1
        // 192.168.1.104
        Efgis.prototype.planeServiceIP = "http://127.0.0.1:9800/";
        Efgis.prototype.tileServiceIP = "http://127.0.0.1:8080/";
        //satellite url
        Efgis.prototype.satelliteUrl = Efgis.prototype.tileServiceIP + "china(weixing)/googlemaps/satellite/";
        // road url
        Efgis.prototype.roadUrl = Efgis.prototype.tileServiceIP + "googleroad/";
        //terrain url
        Efgis.prototype.terrainUrl = Efgis.prototype.tileServiceIP + "googleterrain/";
        //geoserver address
        Efgis.prototype.mapUrl = Efgis.prototype.planeServiceIP + "geoserver/pollution/wms";
        //wfs api
        Efgis.prototype.mapUrlWfs = Efgis.prototype.planeServiceIP + "geoserver/pollution/wfs";
        //wuhui address
        Efgis.prototype.wqtUrl = Efgis.prototype.planeServiceIP + "geoserver/china/wms";

        Efgis.prototype.vectorImgAndBuddleLayer = null;

        Efgis.prototype.setting = {
            provinceCallBack: function () {
            }
        };
    }

    var defaultDistance = 40, globalClusterSource;

    var defaultCenter = [13067909.290260985, 3789581.306222211], defaultZoom = 7.8,
        defaultResolution = 6918.290052377097;
    var vectorPointLayer = null;
    Efgis.fn = Efgis.prototype = {
        _init: function (selector, setting) {
            this.setting = $.extend(this.setting, setting);
            this._createMap(selector);
            this._setSjLayer();
            this._initTgyLine();
            this._setTdLayer();
            this._clearLayer = void 0;
        },
        getMap: function () {
            return this._map;
        },
        _createMap: function (m) {
            if (typeof m === "string")
                m = document.getElementById(m);
            // if(this._timer){
            //     clearInterval(this._timer);
            //     this._timer = void 0;
            // }
            //中心点
            var that = this;
            this._mapElement = m;
            this._map = new ol.Map({
                layers: [],
                controls: [],
                target: m,
                view: new ol.View({
                    center: defaultCenter,
                    zoom: defaultZoom,
                    resolution: defaultResolution,//分辨率-，每个象元代表的实地距离
                    minZoom: 4,
                    maxZoom: 16
                })
            });
            this._map.getView().setZoom(defaultZoom);

            /*this._map.events.register("zoomend", this, function (e) {
            	alert(this._map.getZoom())
    	        ssssssssif (this._map.getZoom() < 17)
    	        {
    	            //map.zoomTo(17);
    	        	this._map.setCenter(map.getCenter(),17);
    	        }
    	    });*/

            this._map.getView().on("change:resolution", function (evt) {
                // console.log(1111,that._map.getView().getZoom())
                var currentZoom = that._map.getView().getZoom();
                if (currentZoom > 12) {
                    that._initLineGt();
                } else {
                    that.removeGtLayer();
                }
                // 切换县级图层
                if (currentZoom > 9) {
                    that._setCountyLayer();
                    // 此判断与业务耦合，后面删掉
                    if (currentState == 'wx') {
                        that._setCountyWeixLayer();
                    }
                } else if (currentZoom < 9) {
                    that.removeCountyLayer();
                    if (currentState == 'wx') {
                        that.removeCountyWeixLayer();
                    }
                }
            });
            this._map.getView().on('propertychange', function (e) {
                switch (e.key) {
                    case 'resolution':
                        var newValue = e.target.getResolution();
                        if (newValue < 70) {
                            if (globalClusterSource) globalClusterSource.setDistance(0);
                        } else {
                            if (globalClusterSource) globalClusterSource.setDistance(defaultDistance);
                        }
                        break;
                }
            });
            this._map.on("singleclick", function (evt) {
                if (that.sjLayer) {
                    var view = that._map.getView();
                    var viewResolution = view.getResolution();
                    var tgySource = that.tgyLayer.getSource();
                    var tgyUrl = tgySource.getGetFeatureInfoUrl(evt.coordinate, viewResolution, view.getProjection(), {
                        'INFO_FORMAT': 'application/json',
                        'FEATURE_COUNT': 50
                    });
                    // console.log(tgyUrl, tgySource.getParams().MARKFLAG);
                    var sjSource = that.sjLayer.getSource();
                    var sjUrl = sjSource.getGetFeatureInfoUrl(evt.coordinate, viewResolution, view.getProjection(), {
                        'INFO_FORMAT': 'application/json',
                        'FEATURE_COUNT': 50
                    });
                }

            });
            var evet;

            function addTip(feature) {
                // debugger;
                var geoType = feature.getGeometry().getType();
                if (!that.prveForEachFeature) {
                    that.prveForEachFeature = feature;
                    that.mapTip = that._mapMouseMove(feature, geoType, evet.coordinate);
                }
                if (that.mapTip) {
                    that.mapTip = $(that.mapTip.selector);
                    //鼠标为放在图标上时，如果页面上有弹出框，则移除弹出框
                    that.mapTip.on("mouseover", function () {
                        isOnTip = true;
                        that.mapTip.on("mouseleave", function () {
                            isOnTip = false;
                            if (that.mapTip) that.mapTip.remove();
                            that.mapTip = void 0;
                            that.prveForEachFeature = void 0;
                        });
                    });
                } else {
                    that.prveForEachFeature = void 0;
                }
                return true;
            }

            var isOnTip = false;
            this._map.on("pointermove", function(evt){
                evet = evt;
                if(that._map.hasFeatureAtPixel(evt.pixel)){
                    that._map.forEachFeatureAtPixel(evt.pixel, addTip);
                }else{
                    if(that.mapTip && !isOnTip){
                        isOnTip = false;
                        if(that.mapTip) that.mapTip.remove();
                        that.mapTip = void 0;
                        that.prveForEachFeature = void 0;
                    }
                    var view = that._map.getView();
                    var viewResolution = view.getResolution();
                    var tgySource = that.tgyLayer.getSource();
                    // 不显示特高压弹框
                    // var tgyUrl = tgySource.getGetFeatureInfoUrl(evt.coordinate, viewResolution, view.getProjection(), {'INFO_FORMAT': 'application/json', 'FEATURE_COUNT': 50});
                    // if(tgyUrl){
                    //     $.ajax({
                    //         url: tgyUrl,
                    //         dataType: "json",
                    //         success: function (res) {
                    //             if(res.features && res.features.length){
                    //                 for(var i = 0; i < res.features.length; i++){
                    //                     var properties = res.features[i].properties;
                    //                     properties.title = properties.zxlmc;
                    //                 }
                    //                 var feature = new ol.format.GeoJSON().readFeatures(res, {dataProjection: this._srsNameDataSource, featureProjection: this._srsNameMap});
                    //                 addTip(feature[0]);
                    //             }
                    //         }
                    //     });
                    // }

                }
            });
            // this._map.on("pointermove", function (evt) {
            //     evet = evt;
            //     if (that._map.hasFeatureAtPixel(evt.pixel)) {
            //         that._map.forEachFeatureAtPixel(evt.pixel, addTip);
            //     } else {
            //         //220kv点的滑过弹窗
            //         if (that.Layer220kv) {
            //             if (that.mapTip && !isOnTip) {
            //                 isOnTip = false;
            //                 if (that.mapTip) that.mapTip.remove();
            //                 that.mapTip = void 0;
            //                 that.prveForEachFeature = void 0;
            //             }
            //             var view = that._map.getView();
            //             var viewResolution = view.getResolution();
            //             var layer220kvSource = that.Layer220kv.getSource();
            //             var layer220kvUrl = layer220kvSource.getGetFeatureInfoUrl(evt.coordinate, viewResolution, view.getProjection(), {
            //                 'INFO_FORMAT': 'application/json',
            //                 'FEATURE_COUNT': 50
            //             });
            //             // console.log(layer220kvUrl)
            //             if (layer220kvUrl) {
            //                 $.ajax({
            //                     url: layer220kvUrl,
            //                     dataType: "json",
            //                     success: function (res) {
            //
            //                         if (res.features && res.features.length) {
            //                             for (var i = 0; i < res.features.length; i++) {
            //                                 var properties = res.features[i].properties;
            //                                 properties.title = properties.plant_name;
            //                                 properties.type = properties.plant_type;
            //                                 properties.city = properties.city;
            //                                 properties.kv_name = properties.kv_name;
            //                                 properties.coordinates = res.features[i].geometry.coordinates;
            //                             }
            //                             var feature = new ol.format.GeoJSON().readFeatures(res, {
            //                                 dataProjection: this._srsNameDataSource,
            //                                 featureProjection: this._srsNameMap
            //                             });
            //                             // addTip220kv(feature[0])
            //                         }
            //                     }
            //                 });
            //             }
            //         }
            //
            //
            //     }
            //
            // });
            //
            // function addTip220kv(feature) {
            //     // debugger;
            //     var geoType = feature.getGeometry().getType();
            //     if (!that.prveForEachFeature) {
            //         that.prveForEachFeature = feature;
            //         that.mapTip = that._mapMouseMove220kv(feature, geoType, evet.coordinate);
            //     }
            //     if (that.mapTip) {
            //         that.mapTip = $(that.mapTip.selector);
            //         //鼠标为放在图标上时，如果页面上有弹出框，则移除弹出框
            //         that.mapTip.on("mouseover", function () {
            //             isOnTip = true;
            //             that.mapTip.on("mouseleave", function () {
            //                 isOnTip = false;
            //                 if (that.mapTip) that.mapTip.remove();
            //                 that.mapTip = void 0;
            //                 that.prveForEachFeature = void 0;
            //             });
            //         });
            //     } else {
            //         that.prveForEachFeature = void 0;
            //     }
            //     return true;
            // }

            return this;
        },
        _setTdLayer: function () {
            if (this.tdLayer)
                this._map.removeLayer(this.tdLayer);

            var td1 = new ol.Feature({
                geometry: new ol.geom.Point(ol.proj.transform([117.34308, 31.3399], 'EPSG:4326', 'EPSG:3857'))   //在中心位置实例化一个要素，设置要素的样式
            });
            var td2 = new ol.Feature({
                geometry: new ol.geom.Point(ol.proj.transform([118.14835, 30.98758], 'EPSG:4326', 'EPSG:3857'))   //在中心位置实例化一个要素，设置要素的样式
            });
            var td3 = new ol.Feature({
                geometry: new ol.geom.Point(ol.proj.transform([117.6764949, 30.65024429], 'EPSG:4326', 'EPSG:3857'))   //在中心位置实例化一个要素，设置要素的样式
            });
            var td4 = new ol.Feature({
                geometry: new ol.geom.Point(ol.proj.transform([118.9436544, 30.85761288], 'EPSG:4326', 'EPSG:3857'))   //在中心位置实例化一个要素，设置要素的样式
            });

            function style1(name) {
                return new ol.style.Style({
                    text: new ol.style.Text({ //文本样式
                        text: name,
                        font: '22px Calibri,sans-serif',
                        fill: new ol.style.Fill({
                            color: '#FF0'
                        })
                    })
                })
            }

            td1.setStyle(style1("吉泉直流"));
            td2.setStyle(style1("芜湖南陵通道"));
            td3.setStyle(style1("池州九华通道"));
            td4.setStyle(style1("宣城宣广通道"));

            this.tdLayer = new ol.layer.Vector({
                source: new ol.source.Vector({
                    features: [td1, td2, td3, td4]
                }),
                zIndex: 9
            });
            this._map.addLayer(this.tdLayer);
            return this;
        },
        _setSjLayer: function () {
            //省界Layer
            if (this.sjLayer)
                this._map.removeLayer(this.sjLayer);
            this.sjLayer = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    ratio: 3,
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        LAYERS: 'pollution:shengjie',
                        MARKFLAG: "shengji"
                    }
                }),
                zIndex: 5
            });
            this._map.addLayer(this.sjLayer);
            return this;
        },
        /**
         * 设置县级图层
         * @returns {Efgis}
         * @private
         */
        _setCountyLayer: function () {
            if (this.countyLayer)
                this._map.removeLayer(this.countyLayer);
            this.countyLayer = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    ratio: 1,
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        STYLES: '',
                        LAYERS: 'pollution:anhui_xianji_polygon',
                        MARKFLAG: "xianji",
                        tiled: true
                    }
                })
            });
            this._map.addLayer(this.countyLayer);
            return this;
        },
        /**
         * 移除县级图层
         */
        removeCountyLayer: function () {
            if (this.countyLayer) {
                this._map.removeLayer(this.countyLayer);
                this.countyLayer = void 0;
            }
        },
        /**
         * 设置县级卫星图显示时图层
         * @returns {Efgis}
         * @private
         */
        _setCountyWeixLayer: function () {
            if (this.countyWeixLayer)
                this._map.removeLayer(this.countyWeixLayer);
            this.countyWeixLayer = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    ratio: 1,
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        STYLES: '',
                        LAYERS: 'pollution:anhui_xianji_wx_polygon',
                        MARKFLAG: "xianji_wx"
                    }
                }),
                zIndex: 6
            });
            this._map.addLayer(this.countyWeixLayer);
            return this;
        },
        /**
         * 移除县级卫星图显示时图层
         */
        removeCountyWeixLayer: function () {
            if (this.countyWeixLayer) {
                this._map.removeLayer(this.countyWeixLayer);
                this.countyWeixLayer = void 0;
            }
        },
        /**
         * 设置地市级卫星图显示时图层
         * @returns {Efgis}
         * @private
         */
        _setDiShiWeixLayer: function () {
            if (this.dsWeixLayer)
                this._map.removeLayer(this.dsWeixLayer);
            this.dsWeixLayer = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    ratio: 1,
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        STYLES: '',
                        LAYERS: 'pollution:anhui_dijishi_wx_polygon',
                        MARKFLAG: "xianji_wx"
                    }
                }),
                zIndex: 6
            });
            this._map.addLayer(this.dsWeixLayer);
            return this;
        },
        /**
         * 移除地市级卫星图显示时图层
         */
        removeDiShiWeixLayer: function () {
            if (this.dsWeixLayer) {
                this._map.removeLayer(this.dsWeixLayer);
                this.dsWeixLayer = void 0;
            }
        },
        resetCenter: function () {
            var view = this._map.getView();
            var zoom = ol.animation.zoom({
                duration: 900,
                resolution: view.getResolution()
            });
            this._map.beforeRender(zoom);
            var pan = ol.animation.pan({
                duration: 1100,
                source: view.getCenter()
            });
            this._map.beforeRender(pan);
            view.setResolution(defaultResolution);
            view.setZoom(defaultZoom);
            view.setCenter(defaultCenter);
        },
        addSjLayer: function () {
            return this._setSjLayer();
        },
        removeSjLayer: function () {
            if (this.sjLayer) {
                this._map.removeLayer(this.sjLayer);
                this.sjLayer = void 0;
            }
            return this;
        },
        _setSatelliteLayer: function () {
            //卫星图
            var that = this;
            if (!this.satelliteLayer)
                this._map.removeLayer(this.satelliteLayer);
            this.satelliteLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    url: that.satelliteUrl + '{z}/{x}/{y}.jpg'
                }),
                zIndex: 6
            });
            this._map.addLayer(this.satelliteLayer);
            return this;
        },
        addSatelliteLayer: function () {
            return this._setSatelliteLayer();
        },
        removeSatelliteLayer: function () {
            if (this.satelliteLayer) {
                this._map.removeLayer(this.satelliteLayer);
                this.satelliteLayer = void 0;
            }
            return this;
        },
        _setSjLayer_white: function () {
            if (this.sjLayerWhite)
                this._map.removeLayer(this.sjLayerWhite);
            this.sjLayerWhite = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    ratio: 1,
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        STYLES: 'pollution:shengpolygon_white',
                        LAYERS: 'pollution:jcdl_qg_shengjie_polygon_white',
                        tiled: true
                    }
                })
            });
            this._map.addLayer(this.sjLayerWhite);
            return this.sjLayerWhite;
        },
        setDiJiShiLayer: function () {
            //地级市Layer
            if (!this.diJiShiLayer) {
                this._map.removeLayer(this.diJiShiLayer);
                this.diJiShiLayer = void 0;
            }
            /*this.diJiShiLayer = new ol.layer.Tile({
                source: new ol.source.TileWMS({
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        STYLES: '',
                        LAYERS: 'pollution:jcdl_qg_dijishi_polygon',
                        MARKFLAG: "dijishi",
                        tiled: true
                    }
                }),
                zIndex: 10
            });*/

            this.diJiShiLayer = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    ratio: 1,
                    url: this.mapUrl,
                    params: {
                        'FORMAT': 'image/png',
                        'VERSION': '1.1.1',
                        STYLES: '',
                        LAYERS: 'pollution:jcdl_qg_dijishi_polygon',
                    }
                })
            });
            this._map.addLayer(this.diJiShiLayer);
            return this;
        },
        removeDiJiShiLayer: function () {
            if (this.diJiShiLayer)
                this._map.removeLayer(this.diJiShiLayer);
        },
        setGuoDaoLayer: function () {
            //地级市Layer
            if (!this.guoDaoLayer) {
                this._map.removeLayer(this.guoDaoLayer);
                this.guoDaoLayer = void 0;
            }
            this.guoDaoLayer = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    url: this.mapUrl,
                    ratio: 1,
                    params: {
                        FORMAT: 'image/png',
                        STYLES: '',
                        LAYERS: 'pollution:jcdl_qg_guodao',
                        MARKFLAG: "guodao",
                        tiled: true
                    }
                }),
                zIndex: 10
            });
            // this.guoDaoLayer.setZIndex(10);
            this._map.addLayer(this.guoDaoLayer);
            return this;
        },
        removeGuoDaoLayer: function () {
            if (this.guoDaoLayer)
                this._map.removeLayer(this.guoDaoLayer);
        },
        _initTgyLine: function () {
            if (!this.tgyLayer)
                this._map.removeLayer(this.tgyLayer);
            this.tgyLayer = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    ratio: 1,
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        LAYERS: 'pollution:line_quan',
                        STYLES: '',
                        MARKFLAG: 'tgyline'
                    },
                    crossOrigin: 'Anonymous',
                    serverType: 'geoserver'
                }),
                visible: true
            });
            this.tgyLayer.setZIndex(10);
            this._map.addLayer(this.tgyLayer);
            return this;
        },
        _initLineGt: function () {
            if (this.gtLayer)
                this._map.removeLayer(this.gtLayer);
            this.gtLayer = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    ratio: 1,
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        LAYERS: 'pollution:t_sb_gt',
                        MARKFLAG: 'gtline'
                    },
                    crossOrigin: 'Anonymous',
                    serverType: 'geoserver'
                }),
                visible: true
            });
            this.gtLayer.setZIndex(10);
            this._map.addLayer(this.gtLayer);
            return this;
        },
        removeGtLayer: function () {
            if (this.gtLayer) {
                this._map.removeLayer(this.gtLayer);
                this.gtLayer = void 0;
            }
            return this;
        },
        _mapWindow: function (feature, html) {
            // debugger;
            // console.log("feature",feature)
            var bubstr;
            // 属性对象
            var properties = feature.getProperties();

            if (feature.getProperties().features) {
                if (feature.getProperties().features.length < 1) {
                    return;
                }
                properties = feature.getProperties().features[0].getProperties();
            }

            if (!html || html === "") {
                // div字符串
                var div = "";
                // 按钮
                var btn = "";
                // 参数
                var params = properties.params;
                var btns = properties.btns;
                // 非空验证
                if (!params && !btns) {
                    console.error("parameters(params and btns) is not defined");
                    return;
                }
                // 属性遍历
                if (params) {
                    for (var pnum = 0; pnum < params.length; pnum++) {
                        // 属性拼装
                        div = div + "<p>" + params[pnum].key + "：<label>" + params[pnum].value + "</label></p>";
                    }
                }
                // 按钮遍历
                if (btns) {
                    for (var bnum = 0; bnum < btns.length; bnum++) {
                        // 按钮拼装
                        btn += "<a id='" + btns[bnum].id + "'  data-id='" + btns[bnum].id + "' data-value='" + properties.SBID + "' href='#'>" + btns[bnum].name + "</a>";
                    }
                }
                // 拼装
                bubstr = "<div class='map_tip' style='z-index:100'>" +
                    "<div class='properties_title'>" +
                    "<span class='title_name'>" + properties.title + "</span>" +
                    "</div>" +
                    "<div class='properties_info'>" +
                    div +
                    "</div>" +
                    "<div class='check_report'>" +
                    btn +
                    "</div>" +
                    "</div>";
            } else {
                bubstr = "<div class='map_tip' style='z-index:100'>" +
                    "<div class='properties_title'>" +
                    "<span class='title_name'>" + properties.title + "</span>" +
                    "</div>" +
                    "<div class='properties_info'>" +
                    html +
                    "</div>" +
                    "</div>";
            }
            bubstr = "<div id='map_tip' class='map_tip1'> " + bubstr + " </div>";
            // 没有该对象则加载
            var mapTip = $('#map_tip');
            if (mapTip.length === 0) {
                $("body").append(bubstr);
            } else {
                // 替换
                mapTip.remove();
                $("body").append(bubstr);
            }
            // overlay 对象
            var popup = new ol.Overlay({
                element: document.getElementById("map_tip")
            });
            // 地图加载
            this._map.addOverlay(popup);
            // 坐标集合
            var coordinate = [];
            // 点
            if (feature.getGeometry().getType() === "Point") {
                // 取坐标
                coordinate = feature.getGeometry().getCoordinates();
            }
            // 坐标设置
            popup.setPosition(coordinate);
            // 关闭
            $(document).one('click', function () {
                if (mapTip) {
                    mapTip.remove();
                }
            });
            // 点击事件
            // $("#map_tip .check_report a").on('click', function(){
            //     alert($(this).data().id + "----" + $(this).data().value);
            // });
            return mapTip;
        },
        _mapWindow220kV: function (feature, html) {
            // debugger;
            // console.log("feature",feature)
            var bubstr;
            // 属性对象
            var properties = feature.getProperties();

            if (feature.getProperties().features) {
                if (feature.getProperties().features.length < 1) {
                    return;
                }
                properties = feature.getProperties().features[0].getProperties();
            }

            if (!html || html === "") {
                // div字符串
                var div = "";
                // 按钮
                var btn = "";
                // 参数
                var params = [{
                    key: "类型",
                    value: properties.plant_name
                }, {
                    key: "kv",
                    value: properties.kv_name
                }]
                var btns = "暂无";
                // 非空验证
                if (!params && !btns) {
                    console.error("parameters(params and btns) is not defined");
                    return;
                }
                // 属性遍历
                if (params) {
                    for (var pnum = 0; pnum < params.length; pnum++) {
                        // 属性拼装
                        div = div + "<p>" + params[pnum].key + "：<label>" + params[pnum].value + "</label></p>";
                    }
                }
                // // 按钮遍历
                // if(btns){
                //     for (var bnum = 0; bnum < btns.length; bnum++) {
                //         // 按钮拼装
                //         btn += "<a id='" + btns[bnum].id + "'  data-id='" + btns[bnum].id + "' data-value='" + properties.SBID + "' href='#'>" + btns[bnum].name + "</a>";
                //     }
                // }
                // 拼装
                bubstr = "<div class='map_tip' style='z-index:100'>" +
                    "<div class='properties_title'>" +
                    "<span class='title_name'>" + properties.plant_name + "</span>" +
                    "</div>" +
                    "<div class='properties_info'>" +
                    div +
                    "</div>" +
                    "<div class='check_report'>" +
                    btn +
                    "</div>" +
                    "</div>";
            } else {
                bubstr = "<div class='map_tip' style='z-index:100'>" +
                    "<div class='properties_title'>" +
                    "<span class='title_name'>" + properties.plant_name + "</span>" +
                    "</div>" +
                    "<div class='properties_info'>" +
                    html +
                    "</div>" +
                    "</div>";
            }
            bubstr = "<div id='map_tip' class='map_tip1'> " + bubstr + " </div>";
            // 没有该对象则加载
            var mapTip = $('#map_tip');
            if (mapTip.length === 0) {
                $("body").append(bubstr);
            } else {
                // 替换
                mapTip.remove();
                $("body").append(bubstr);
            }
            // overlay 对象
            var popup = new ol.Overlay({
                element: document.getElementById("map_tip")
            });
            // 地图加载
            this._map.addOverlay(popup);
            // 坐标集合
            var coordinate = [];
            // 点
            if (feature.getGeometry().getType() === "Point") {
                // 取坐标
                coordinate = feature.getGeometry().getCoordinates();
            }
            // 坐标设置
            popup.setPosition(coordinate);
            // 关闭
            $(document).one('click', function () {
                if (mapTip) {
                    mapTip.remove();
                }
            });
            // 点击事件
            // $("#map_tip .check_report a").on('click', function(){
            //     alert($(this).data().id + "----" + $(this).data().value);
            // });
            return mapTip;
        },
        _mapWindowLine: function (feature, coordinate) {
            var properties = feature.getProperties();
            var title = properties.name;
            if (!title) return;
            // 拼装
            var bubstr = "<div class='map_tip' style='z-index:100'>" +
                "<div>" +
                "<span>" + title + "</span>" +
                "</div>" +
                "</div>";
            bubstr = "<div id='map_tip' class='map_tip1'> " + bubstr + " </div>";
            // 没有该对象则加载
            var mapTip = $('#map_tip');
            if (mapTip.length === 0) {
                $("body").append(bubstr);
            } else {
                // 替换
                mapTip.remove();
                $("body").append(bubstr);
            }
            // overlay 对象
            var popup = new ol.Overlay({
                element: document.getElementById("map_tip")
            });
            // 地图加载
            this._map.addOverlay(popup);
            // 坐标设置
            popup.setPosition(coordinate);
            // 关闭
            $(document).one('click', function () {
                if (mapTip) {
                    mapTip.remove();
                }
            });
            return mapTip;
        },
        _mapMouseMove: function (feature, geoType, coordinate) {
            var html, mapTip;
            if (geoType === 'Point') {
                if (feature.getProperties().hasOwnProperty("divhtml")) {
                    html = feature.getProperties().divhtml;
                } else if (feature.getProperties().hasOwnProperty("divHtml")) {
                    html = feature.getProperties().divHtml;
                }
                if (html) {
                    mapTip = this._mapWindow(feature, html);
                } else {
                    mapTip = this._mapWindow(feature);
                }
            } else if (geoType === 'LineString') {
                mapTip = this._mapWindowLine(feature, coordinate);
            }
            return mapTip;
        },
        _mapMouseMove220kv: function (feature, geoType, coordinate) {
            var html, mapTip;
            if (geoType === 'Point') {
                if (feature.getProperties().hasOwnProperty("divhtml")) {
                    html = feature.getProperties().divhtml;
                } else if (feature.getProperties().hasOwnProperty("divHtml")) {
                    html = feature.getProperties().divHtml;
                }
                if (html) {
                    mapTip = this._mapWindow220kV(feature, html);
                } else {
                    mapTip = this._mapWindow220kV(feature);
                }
            } else if (geoType === 'LineString') {
                mapTip = this._mapWindowLine(feature, coordinate);
            }
            return mapTip;
        },
        _mapClick: function (feature) {
            var html, mapTip;
            if (feature.getProperties().hasOwnProperty("divhtml")) {
                html = feature.getProperties().divhtml;
            } else if (feature.getProperties().hasOwnProperty("divHtml")) {
                html = feature.getProperties().divHtml;
            }
            if (html && html !== "") {
                mapTip = this._mapWindow(feature, html);
            } else {
                mapTip = this._mapWindow(feature);
            }
            return mapTip;
        },
        removeLayer: function (layer) {
            this._map.removeLayer(layer);
        },
        getMap: function () {
            return this._map;
        },
        getMapElement: function () {
            return this._mapElement;
        },
        _resoutionAdaptByFeatureExtent: function (short, extend, setting) {
            var defaultSetting = {
                isAutoCenter: true,
                isAutoZoom: false,
                zoom: 6,
                zIndex: 0
            };
            var mySetting = $.extend(defaultSetting, setting);
            var zoom = this._map.getView().getZoom();
            if (!zoom) {
                zoom = defaultZoom;
            }
            var dzWdith = extend[2] - extend[0];
            var dzHeight = extend[3] - extend[1];
            var _mapElement = $(this._mapElement);
            var resW = dzWdith / (_mapElement.width() - short);
            var resH = dzHeight / (_mapElement.height() - short);
            var res = resW > resH ? resW : resH;
            if (res === 0) {
                res = 0.125;
            }
            this._map.getView().setResolution(res);
            var pan = ol.animation.pan({
                duration: 1200,
                source: /** @type {ol.Coordinate} */ (this._map.getView().getCenter())
            });
            this._map.beforeRender(pan);
            if (mySetting.isAutoCenter) {
                var center = [(extend[2] + extend[0]) / 2, (extend[3] + extend[1]) / 2];
                this._map.getView().setCenter(center);
            }
            if (mySetting.isAutoZoom) {
                this._map.getView().setZoom(mySetting.zoom);
            } else {
                this._map.getView().setZoom(zoom);
            }
        },
        _resoutionAdapt: function (source, setting) {
            var extent, feature;
            if (source.getSource) {
                extent = source.getSource().getExtent();
                feature = source.getSource().getFeatures();
            } else {
                extent = source.getExtent();
                feature = source.getFeatures();
            }
            //计算与屏幕的边框的距离
            var short = 150;
            if (feature.length > 0) {
                if (feature[0].getGeometry().getType() === "LineString") {
                    short = 250;
                }
            }
            this._resoutionAdaptByFeatureExtent(short, extent, setting);
        },
        _renderLayer: function (layer, setting) {
            this._map.addLayer(layer);
            if (setting && setting.zIndex) {
                layer.setZIndex(setting.zIndex);
            }
            this._resoutionAdapt(layer.getSource(), setting);
        },
        vectorProvienceByName: function (provience) {
            if (!provience || provience.length < 1) {
                return;
            }
            var param = '', that = this;
            if (typeof provience === "string") {
                provience = [provience];
            }
            for (var i = 0; i < provience.length; i++) {
                if (i !== 0) {
                    param += ' or ';
                }
                param += ' fname like \'%' + provience[i] + "%\' ";
            }
            $.ajax({
                url: that.mapUrlWfs,
                type: 'GET',
                data: {
                    service: 'WFS',
                    request: 'GetFeature',
                    typename: 'pollution:jcdl_qg_shengjie_polygon',
                    srsname: 'EPSG:3857',
                    outputFormat: 'json',
                    cql_filter: param
                },
                dataType: 'json',
                success: function (res, status) {
                    if (status === 'success') {
                        var top, left, bottom, right;
                        for (var fraIdx = 0; fraIdx < res.features.length; fraIdx++) {
                            var feature = res.features[fraIdx];
                            var coordinateArr = feature.geometry.coordinates[0][0];
                            for (var i = 0; i < coordinateArr.length; i++) {
                                if (!top && !left && !bottom && !right) {
                                    top = coordinateArr[i];
                                    left = coordinateArr[i];
                                    bottom = coordinateArr[i];
                                    right = coordinateArr[i];
                                } else {
                                    if (top[1] > coordinateArr[i][1]) {
                                        top = coordinateArr[i];
                                    }
                                    if (bottom[1] < coordinateArr[i][1]) {
                                        bottom = coordinateArr[i];
                                    }
                                    if (left[0] > coordinateArr[i][0]) {
                                        left = coordinateArr[i];
                                    }
                                    if (right[0] < coordinateArr[i][0]) {
                                        right = coordinateArr[i];
                                    }
                                }
                            }
                        }
                        // extend : [minx, miny, maxx, maxy]
                        var extend = [left[0], top[1], right[0], bottom[1]];
                        top = ol.proj.transform(top, 'EPSG:3857', 'EPSG:4326');
                        bottom = ol.proj.transform(bottom, 'EPSG:3857', 'EPSG:4326');
                        left = ol.proj.transform(left, 'EPSG:3857', 'EPSG:4326');
                        right = ol.proj.transform(right, 'EPSG:3857', 'EPSG:4326');
                        var lyaer = [[left[0], top[1]], [right[0], top[1]], [left[0], bottom[1]], [right[0], bottom[1]]];
                        //计算与屏幕的边框的距离
                        var short = 150;
                        that._resoutionAdaptByFeatureExtent(short, extend, {
                            isAutoCenter: true,
                            isAutoZoom: true,
                            zoom: 6
                        });
                        // if(mySetting.zoom){
                        //     this._map.getView().setZoom(mySetting.zoom);
                        // }
                        var arr = [
                            {
                                sblx: "102000", ywid: "ganta", title: "预警点",
                                sbarray: [
                                    {
                                        sbid: "00119007947172", color: "red",
                                        "coordinates": lyaer[0],
                                        btns: [],
                                        params: [
                                            {key: "最上坐标点", value: lyaer[0]}
                                        ]
                                    }
                                ]
                            },
                            {
                                sblx: "102000", ywid: "ganta", title: "预警点",
                                sbarray: [
                                    {
                                        sbid: "00119007947172", color: "red",
                                        "coordinates": lyaer[1],
                                        btns: [],
                                        params: [
                                            {key: "最下坐标点", value: lyaer[1]}
                                        ]
                                    }
                                ]
                            },
                            {
                                sblx: "102000", ywid: "ganta", title: "预警点",
                                sbarray: [
                                    {
                                        sbid: "00119007947172", color: "red",
                                        "coordinates": lyaer[2],
                                        btns: [],
                                        params: [
                                            {key: "最左坐标点", value: lyaer[2]}
                                        ]
                                    }
                                ]
                            },
                            {
                                sblx: "102000", ywid: "ganta", title: "预警点",
                                sbarray: [
                                    {
                                        sbid: "00119007947172", color: "red",
                                        "coordinates": lyaer[3],
                                        btns: [],
                                        params: [
                                            {key: "最右坐标点", value: lyaer[3]}
                                        ]
                                    }
                                ]
                            }
                        ];
                        // that.vectorPointByCoordinate(arr);
                    }
                }
            });
        },
        vectorFeatures: function (features, setting) {
            // debugger
            var sourceTemp = new ol.source.Vector({
                features: features
            });
            var clusterSource = new ol.source.Cluster({
                distance: 40,
                source: sourceTemp
            });
            globalClusterSource = clusterSource;
            var styleCache = {};
            var vectorPointLayer = new ol.layer.Vector({
                source: clusterSource,
                style: styleFunction
            });

            function styleFunction(feature) {
                var style, size = feature.get('features').length;
                var ywid = feature.get('features')[0].get('professionName');
                var scale = 0.4;
                var sizesum = 5;
                if (ywid == "fire") {
                    scale = 0.9;
                    sizesum = 1000
                }
                if (size > 5) {
                    style = new ol.style.Style({
                        image: new ol.style.Circle({
                            radius: 15,
                            stroke: new ol.style.Stroke({
                                color: '#a09e1f'
                            }),
                            fill: new ol.style.Fill({
                                color: '#fff636'
                            })
                        }),
                        text: new ol.style.Text({ //文本样式
                            text: size + "",
                            textAlign: 'center',
                            textBaseline: 'middle',
                            font: '17px Calibri,sans-serif',
                            fill: new ol.style.Fill({
                                color: '#000'
                            })
                        })
                    });
//                    styleCache[size] = style;
                } else {

//                    style = styleCache[size];
                    if (!style) {
                        style = new ol.style.Style({
                            image: new ol.style.Icon({
                                src: 'image/index/' + ywid + '.png',
                                scale: scale
                            })
                        });
//                        styleCache[size] = style;
                    }
                }
                return style;
            };
            this._renderLayer(vectorPointLayer, setting);
            return vectorPointLayer;
        },
        vectorFeatures_bdz: function (fts, zoomFlag) {

            var createTextStyle = function (feature, ywid) {
                var offsety = 20;
                var offsetx = 0;
                var name = feature.getProperties().SBMC;
                return new ol.style.Text({
                    text: name,
                    offsetY: offsety,
                    offsetX: offsetx,
                    scale: 1.1,
                    fill: new ol.style.Fill({color: 'blue'}),
                    stroke: new ol.style.Stroke({color: 'rgba(174, 32, 171, 1.0)', width: 1})
                });
            };
            var styleFunction = function (feature, resolution) {
                var style;
                var src;
                var size;
                var ywid = feature.get('professionName');
                var bdzname = feature.get('title');
                // size = parseFloat(feature.get('size'));
                size = 0.4;
                src = 'image/index/' + ywid + '.png';
                // debugger;
                // console.log(1111,src)
                    style = new ol.style.Style({
                        image: new ol.style.Icon({
                            src: src,
                            scale: size
                        }),
                        text: new ol.style.Text({ //文本样式
                            text: bdzname,
                            offsetY: -15,
                            textAlign: 'center',            //位置
                            textBaseline: 'bottom',         //基准线
                            font: 'normal 14px 微软雅黑',
                            fill: new ol.style.Fill({
                                color: '#fff'
                            }),
                            stroke: new ol.style.Stroke({
                                color: '#F00',
                                width: 2
                            })
                        }),
                        zIndex: 21,
                    });
                return [style];
            };
            if (vectorPointLayer == null) {
                vectorPointLayer = new ol.layer.Vector({
                    source: new ol.source.Vector({
                        features: fts,
                    }),
                    style: styleFunction
                });
                this._map.addLayer(vectorPointLayer);
                vectorPointLayer.setZIndex(21);

            } else {

                vectorPointLayer.getSource().addFeatures(fts);
                var source = new ol.source.Vector({
                    features: fts,
                });

            }
        },
        vectorFeatures_220kv: function (fts, zoomFlag) {

            var createTextStyle = function (feature, ywid) {
                var offsety = 20;
                var offsetx = 0;
                var name = feature.getProperties().SBMC;
                return new ol.style.Text({
                    text: name,
                    offsetY: offsety,
                    offsetX: offsetx,
                    scale: 1.1,
                    fill: new ol.style.Fill({color: 'blue'}),
                    stroke: new ol.style.Stroke({color: 'rgba(174, 32, 171, 1.0)', width: 1})
                });
            };
            var styleFunction = function (feature, resolution) {
                var style;
                var src;
                var size;
                var ywid = feature.get('professionName');
                var bdzname = feature.get('title');
                // size = parseFloat(feature.get('size'));
                size = 0.4;
                src = 'image/index/' + ywid + '.png';
                // debugger;
                // console.log(1111,src)
                style = new ol.style.Style({
                    image: new ol.style.Icon({
                        src: src,
                        scale: size
                    }),
                    text: new ol.style.Text({ //文本样式
                        text: bdzname,
                        offsetY: -15,
                        textAlign: 'center',            //位置
                        textBaseline: 'bottom',         //基准线
                        font: 'normal 14px 微软雅黑',
                        fill: new ol.style.Fill({
                            color: '#fff'
                        }),
                        stroke: new ol.style.Stroke({
                            color: '#820a51',
                            width: 2
                        })
                    }),
                    zIndex: 21,
                });
                return [style];
            };
            if (this.vectorPointLayer220kv == null) {
                this.vectorPointLayer220kv = new ol.layer.Vector({
                    source: new ol.source.Vector({
                        features: fts
                    }),
                    style: styleFunction
                });
                this._map.addLayer(this.vectorPointLayer220kv);
                this.vectorPointLayer220kv.setZIndex(21);

            } else {

                this.vectorPointLayer220kv.getSource().addFeatures(fts);
                var source = new ol.source.Vector({
                    features: fts
                });

            }
        },
        vectorPointMark: function (arr) {
            var coordinate = ol.proj.transform([117.34308, 31.3399], 'EPSG:3857', 'EPSG:4326');
            var anchor_value = new ol.Feature({
                geometry: new ol.geom.Point(coordinate)   //在中心位置实例化一个要素，设置要素的样式
            });
            // 设置文字style
            anchor_value.setStyle(new ol.style.Style({
                text: new ol.style.Text({
                    font: '15px Microsoft YaHei',
                    text: '222222222222222222',
                    fill: new ol.style.Fill({
                        color: '#222'
                    })
                })
            }));

            vectorPointLayer_wz = new ol.layer.Vector({
                source: new ol.source.Vector({
                    features: anchor_value,
                })
            });

            this._map.addLayer(vectorPointLayer_wz);
            vectorPointLayer_wz.setZIndex(21);
        },
        vectorPointByGeoJson: function (json, setting) {
            if (this._clearLayer) {
                this.removeLayer(this._clearLayer);
                this._clearLayer = void 0;
            }
            if (setting && typeof setting === "number") {
                setting = {zoom: setting};
            }
            if (!setting) {
                setting = {};
            }
            setting.isAutoCenter = true;
            setting.isAutoZoom = false;
            setting.zIndex = 10;
            var fts = new ol.format.GeoJSON().readFeatures(json, {
                dataProjection: this._srsNameDataSource,
                featureProjection: this._srsNameMap
            });
            this._clearLayer = this.vectorFeatures(fts, setting);
            return this._clearLayer;
        },
        vectorPointByCoordinate: function (arr, setting) {

            if (this._clearLayer) {
                this.removeLayer(this._clearLayer);
                this._clearLayer = void 0;
            }
            if (setting && typeof setting === "number") {
                setting = {zoom: setting};
            }
            if (!setting) {
                setting = {};
            }
            setting.zIndex = 11;
            var feaStr = '';
            for (var i = 0; i < arr.length; i++) {
                var geoType = "Point";
                for (var j = 0; j < arr[i].sbarray.length; j++) {
                    if (arr[i].sbarray[j].coordinates === "" || arr[i].sbarray[j].coordinates === undefined || arr[i].sbarray[j].coordinates.length === 0)
                        continue;
                    if (i === 0 && j === 0) {
                        feaStr = '{"type":"Feature","properties":{"SBID":"' + arr[i].sbarray[j].sbid + '","professionName":"' + arr[i].ywid + '",' +
                            '"ywid":"' + arr[i].ywid + '","title":"' + arr[i].title + '","params":' + JSON.stringify(arr[i].sbarray[j].params) + ',"btns":' + JSON.stringify(arr[i].sbarray[j].btns) + ',' +
                            '"divhtml":' + JSON.stringify(arr[i].sbarray[j].divhtml === void 0 ? "" : arr[i].sbarray[j].divhtml) + '},' +
                            '"geometry":{"type":"' + geoType + '",' + '"coordinates":' + JSON.stringify(arr[i].sbarray[j].coordinates) + '}}';
                    } else {
                        feaStr += ',{"type":"Feature","properties":{"SBID":"' + arr[i].sbarray[j].sbid + '","professionName":"' + arr[i].ywid + '",' +
                            '"ywid":"' + arr[i].ywid + '","title":"' + arr[i].title + '","params":' + JSON.stringify(arr[i].sbarray[j].params) + ',"btns":' + JSON.stringify(arr[i].sbarray[j].btns) + ',' +
                            '"divhtml":' + JSON.stringify(arr[i].sbarray[j].divhtml === void 0 ? "" : arr[i].sbarray[j].divhtml) + '},' +
                            '"geometry":{"type":"' + geoType + '",' + '"coordinates":' + JSON.stringify(arr[i].sbarray[j].coordinates) + '}}';
                    }
                }
            }
            // debugger;
            var json = '{"type":"FeatureCollection","features":[' + feaStr + ']}';
            var geoJsonFormat = new ol.format.GeoJSON();
            var fts = geoJsonFormat.readFeatures(json, {
                dataProjection: this._srsNameDataSource,
                featureProjection: this._srsNameMap
            });
            // debugger
            this._clearLayer = this.vectorFeatures(fts, setting);
            return this._clearLayer;
        },
        vectorPointByCoordinate_bdz: function (arr, zoomFlag) {
            var feaStr = '';
            for (var i = 0; i < arr.length; i++) {
                var geoType = "Point";
                for (var j = 0; j < arr[i].sbarray.length; j++) {
                    if (arr[i].sbarray[j].coordinates == "" || arr[i].sbarray[j].coordinates == undefined || arr[i].sbarray[j].coordinates.length == 0) {
                        continue;
                    }
                    // if(!ol.extent.containsCoordinate(extent,
                    // arr[i].sbarray[j].coordinates))
                    // continue
                    if (i == 0 && j == 0) {
                        feaStr = '{"type":"Feature","properties":{"SBID":"' + arr[i].sbarray[j].sbid + '","professionName":"' + arr[i].ywid + '",' +
                            '"ywid":"' + arr[i].ywid + '","title":"' + arr[i].title + '","params":' + JSON.stringify(arr[i].sbarray[j].params) + ',"btns":' + JSON.stringify(arr[i].sbarray[j].btns) + '},' +
                            '"geometry":{"type":"' + geoType + '",' + '"coordinates":' + JSON.stringify(arr[i].sbarray[j].coordinates) + '}}';
                    } else {
                        feaStr += ',{"type":"Feature","properties":{"SBID":"' + arr[i].sbarray[j].sbid + '","professionName":"' + arr[i].ywid + '",' +
                            '"ywid":"' + arr[i].ywid + '","title":"' + arr[i].title + '","params":' + JSON.stringify(arr[i].sbarray[j].params) + ',"btns":' + JSON.stringify(arr[i].sbarray[j].btns) + '},' +
                            '"geometry":{"type":"' + geoType + '",' + '"coordinates":' + JSON.stringify(arr[i].sbarray[j].coordinates) + '}}';
                    }
                    // var coordinate = ol.proj.transform(arr[i].sbarray[j].coordinates, 'EPSG:4326', 'EPSG:3857');

                }
            }
            var json = '{"type":"FeatureCollection","features":[' + feaStr + ']}';
            var geojsonFormat = new ol.format.GeoJSON();
            var fts = geojsonFormat.readFeatures(json, {
                dataProjection: this._srsNameDataSource,
                featureProjection: this._srsNameMap
            });
            // console.log("ftsbdz", fts)
            // debugger;
            this.vectorFeatures_bdz(fts, zoomFlag);
        },
        vectorPointByCoordinate_220kv: function (arr, zoomFlag) {

            var feaStr = '';
            for (var i = 0; i < arr.length; i++) {
                var geoType = "Point";
                for (var j = 0; j < arr[i].sbarray.length; j++) {
                    if (arr[i].sbarray[j].coordinates == "" || arr[i].sbarray[j].coordinates == undefined || arr[i].sbarray[j].coordinates.length == 0)
                        continue;
                    // if(!ol.extent.containsCoordinate(extent,
                    // arr[i].sbarray[j].coordinates))
                    // continue
                    if (i == 0 && j == 0) {
                        feaStr = '{"type":"Feature","properties":{"SBID":"' + arr[i].sbarray[j].sbid + '","professionName":"' + arr[i].ywid + '",' +
                            '"ywid":"' + arr[i].ywid + '","title":"' + arr[i].title + '","params":' + JSON.stringify(arr[i].sbarray[j].params) + ',"btns":' + JSON.stringify(arr[i].sbarray[j].btns) + '},' +
                            '"geometry":{"type":"' + geoType + '",' + '"coordinates":' + JSON.stringify(arr[i].sbarray[j].coordinates) + '}}';
                    } else {
                        feaStr += ',{"type":"Feature","properties":{"SBID":"' + arr[i].sbarray[j].sbid + '","professionName":"' + arr[i].ywid + '",' +
                            '"ywid":"' + arr[i].ywid + '","title":"' + arr[i].title + '","params":' + JSON.stringify(arr[i].sbarray[j].params) + ',"btns":' + JSON.stringify(arr[i].sbarray[j].btns) + '},' +
                            '"geometry":{"type":"' + geoType + '",' + '"coordinates":' + JSON.stringify(arr[i].sbarray[j].coordinates) + '}}';
                    }
                    // var coordinate = ol.proj.transform(arr[i].sbarray[j].coordinates, 'EPSG:4326', 'EPSG:3857');
                }
            }
            // debugger;
            var json = '{"type":"FeatureCollection","features":[' + feaStr + ']}';
            var geojsonFormat = new ol.format.GeoJSON();
            var fts = geojsonFormat.readFeatures(json, {
                dataProjection: this._srsNameDataSource,
                featureProjection: this._srsNameMap
            });
            // console.log("fts", fts)
            // console.log(fts)
            this.vectorFeatures_220kv(fts, zoomFlag);
        },
        vectorLineByGeoJson: function (json, setting) {
            if (!json) return;
            var features = new ol.format.GeoJSON().readFeatures(json, {
                dataProjection: this._srsNameDataSource,
                featureProjection: this._srsNameMap
            });
            var styleFunction = function (f) {
                var style = new ol.style.Style({
                    stroke: new ol.style.Stroke({
                        width: 3,
                        color: '#FF0000'
                    })
                });
                return [style];
            };

            var layer = new ol.layer.Vector({
                source: new ol.source.Vector({
                    features: features
                }),
                style: styleFunction
            });
            this._renderLayer(layer, setting);
            return layer;
        },
        vectorLineByCoordinate: function (arr, setting) {
            if (!setting) {
                setting = {};
            }
            setting.zIndex = 11;
            var feaStr = '';
            for (var i = 0; i < arr.length; i++) {
                var geoType = "LineString";
                var nowArr = arr[i];
                feaStr += '{ "type": "Feature", "geometry": {"type": "' + geoType + '", "coordinates": [';
                for (var j = 0; j < nowArr.sbarray.length; j++) {
                    var nowSbArray = nowArr.sbarray[j];
                    feaStr += '[' + nowSbArray[0] + ', ' + nowSbArray[1] + ']';
                    if (j !== nowArr.sbarray.length - 1)
                        feaStr += ',';
                }
                feaStr += ']}, "properties": {"xlid": "' + nowArr.xlid + '", "title": "' + nowArr.title + '"}}';
                if (i !== arr.length - 1)
                    feaStr += ',';
            }
            var json = '{"type":"FeatureCollection","features":[' + feaStr + ']}';
            return this.vectorLineByGeoJson(json, setting);
        },
        vectorLineByCoordinateCoruscate: function (arr, setting) {
            if (!setting) {
                setting = {};
            }
            setting.zIndex = 11;
            setting.isAutoZoom = false;
            setting.isAutoCenter = false;
            var feaStr = '';
            for (var i = 0; i < arr.length; i++) {
                var geoType = "LineString";
                var nowArr = arr[i];
                feaStr += '{ "type": "Feature", "geometry": {"type": "' + geoType + '", "coordinates": [';
                for (var j = 0; j < nowArr.sbarray.length; j++) {
                    var nowSbArray = nowArr.sbarray[j];
                    feaStr += '[' + nowSbArray[0] + ', ' + nowSbArray[1] + ']';
                    if (j !== nowArr.sbarray.length - 1)
                        feaStr += ',';
                }
                feaStr += ']}, "properties": {"xlid": "' + nowArr.xlid + '", "title": "' + nowArr.title + '"}}';
                if (i !== arr.length - 1)
                    feaStr += ',';
            }
            var json = '{"type":"FeatureCollection","features":[' + feaStr + ']}';
            var lineLayer = this.vectorLineByGeoJson(json, setting);
            this.flashStyle(lineLayer, 500);
            return lineLayer;
        },
        flashStyle: function (layer, timeout) {
            var features = layer.getSource().getFeatures();
            for (var i = 0; i < features.length; i++) {
                var feature = features[i];
                var style1, style2;
                if (typeof layer.getStyle() === 'function') {
                    style1 = layer.getStyle()();
                } else {
                    style1 = layer.getStyle();
                }
                style2 = new ol.style.Style({
                    stroke: new ol.style.Stroke({
                        width: 3,
                        color: '#FFFF00'
                    })
                });
                var flashStyleIndex = 0;
                (function (feature, flashStyleIndex, style1, style2) {
                    setInterval(function () {
                        if (flashStyleIndex === 1) {
                            feature.setStyle(style2);
                            flashStyleIndex = 0;
                        } else {
                            feature.setStyle(style1);
                            flashStyleIndex = 1;
                        }
                    }, timeout);
                })(feature, flashStyleIndex, style1, style2);
            }
        },
        coordinateLocation: function (coordinate, zoom) {
            if (coordinate && coordinate.length) {
                var pan = ol.animation.pan({
                    duration: 1200,
                    source: /** @type {ol.Coordinate} */ (this._map.getView().getCenter())
                });
                this._map.beforeRender(pan);
                var c = ol.proj.transform(coordinate, 'EPSG:4326', 'EPSG:3857');
                this._map.getView().setCenter(c);
            }
            if (zoom)
                this._map.getView().setZoom(zoom);
        },
        setAllWqtLayer: function () {
            if (!this.allwqLayer)
                this._map.removeLayer(this.allwqLayer);
            this.allwqLayer = new ol.layer.Tile({
                source: new ol.source.TileWMS({
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        LAYERS: 'pollution:chinawuqu',
                        STYLES: '',
                        tiled: true
                    },
                    crossOrigin: 'Anonymous',
                    serverType: 'geoserver'
                }),
                visible: true
            });
            this._map.addLayer(this.allwqLayer);
            return this;
        },
        playPicAnimate: function (prevPath, fileArray, speed, filetimeArray, loopCallBack, completionCallBack) {
            /**
             * prevPath: 路径前缀
             * fileArray: 文件名集合
             * speed:   图片切换速度
             * barId: 进度调元素ID
             * completionCallBack: 播放完成事件后的回调函数
             */
                //playStatus 播放状态 ready准备播放 play播放中 pause暂停 stop停止
            var _timer, layer, src, prveLayer = [], that = this, playStatus = 'ready', barid, index = 0, isEnd = false,
                isEndPause = false;

            // var bl = ol.proj.transform([112.243, 26.432], 'EPSG:4326', 'EPSG:3857');
            // var tr = ol.proj.transform([122.373, 36.992], 'EPSG:4326', 'EPSG:3857');

            var bl = ol.proj.transform([112.243, 26.432], 'EPSG:4326', 'EPSG:3857');
            var tr = ol.proj.transform([122.373, 36.992], 'EPSG:4326', 'EPSG:3857');
            var imageExtent = [bl[0], bl[1], tr[0], tr[1]];
            src = fileArray;               //将图片数组进行切割
            _timer = _setInterval(speed);

            // this._timer = _timer;
            function _setInterval(speed) {
                playStatus = 'play';
                intervalExec();
                if (index !== 0) {
                    return setInterval(function () {   //设置interval属性
                        intervalExec();
                    }, speed);
                }
                return setInterval(function () {
                }, 1);
            }

            function intervalExec() {
                if (src[index]) {
                    layer = _setLayer(that, prevPath + src[index], imageExtent);
                    loopCallBack(src[index]);
                }
//                $("#starttimeonmap").text(filetimeArray[index]);
                index++;
                if (index >= src.length) {
                    isEnd = true;
                    index = 0;
                    prveLayer = [];
                    clearInterval(_timer);
                    window.setTimeout(function () {
                        that._map.removeLayer(layer);
                        if (that.isSwitch) {
                            playStatus = 'stop';

                            if (completionCallBack) completionCallBack(isEndPause);
                        }
                        isEnd = false;
                    }, speed);
                }
            }

            function _setLayer(that, src, imageExtent) {
                var _prveLayer;
                if (prveLayer && prveLayer.length) {
                    _prveLayer = prveLayer[prveLayer.length - 1];
                }
                var layer = new ol.layer.Image({
                    source: new ol.source.ImageStatic({
                        url: src,
                        crossOrigin: null,          //跨域
                        projection: "EPSG:3857",    //投影
                        imageExtent: imageExtent,
                        imageLoadFunction: function (image, src) {
                            image.getImage().src = src;
                            image.getImage().onload = function () {
                                if (_prveLayer) that._map.removeLayer(_prveLayer);
                            };
                        }
                    }),
                    visible: true,
                    zIndex: 10,
                    opacity: .8
                });
                // layer.setZIndex(9);                 //设置z-index属性
                that._map.addLayer(layer);           //将其加入值map中
                prveLayer.push(layer);
                return layer;
            }

            return {
                //停止
                stop: function () {
                    index = 0;
                    playStatus = 'stop';
                    clearInterval(_timer);

                    that._map.removeLayer(prveLayer[prveLayer.length - 1]);
                },
                //暂停
                pause: function () {
                    if (isEnd) {
                        isEndPause = true;
                    }
                    playStatus = 'pause';
                    clearInterval(_timer);
                },
                //继续
                resume: function () {
                    //如果暂停则继续
                    playStatus = 'play';
                    _timer = _setInterval(speed);
                },
                //获取播放状态
                getPlayStatus: function () {
                    return playStatus;
                },
                //获得当前Layer
                getCurrent: function () {
                    return layer;
                }
            };
        },
        playPicAnimateRequest: function (prevPath, fileArray, speed, filetimeArray, loopCallBack, completionCallBack) {
            /**
             * prevPath: 路径前缀
             * fileArray: 文件名集合
             * speed:   图片切换速度
             * barId: 进度调元素ID
             * completionCallBack: 播放完成事件后的回调函数
             */
                //playStatus 播放状态 ready准备播放 play播放中 pause暂停 stop停止
            var _timer, layer, src, prveLayer = [], that = this, playStatus = 'ready', barid, index = 0, isEnd = false,
                isEndPause = false;

            // var bl = ol.proj.transform([112.243, 26.432], 'EPSG:4326', 'EPSG:3857');
            // var tr = ol.proj.transform([122.373, 36.992], 'EPSG:4326', 'EPSG:3857');

            // var bl = ol.proj.transform([112.143, 26.752], 'EPSG:4326', 'EPSG:3857');
            // var tr = ol.proj.transform([122.373, 36.992], 'EPSG:4326', 'EPSG:3857');
            var bl = ol.proj.transform([113, 27], 'EPSG:4326', 'EPSG:3857');
            var tr = ol.proj.transform([121, 36], 'EPSG:4326', 'EPSG:3857');

            var imageExtent = [bl[0], bl[1], tr[0], tr[1]];
            src = fileArray;               //将图片数组进行切割
            window.requestAnimFrame = (function () {
                return window.requestAnimationFrame ||
                    window.webkitRequestAnimationFrame ||
                    window.mozRequestAnimationFrame ||
                    window.oRequestAnimationFrame ||
                    window.msRequestAnimationFrame ||
                    function (callback) {
                        window.setTimeout(callback, 1000 / 60);
                    };
            })();
            _setInterval();//执行播放
            var startTime = new Date();

            function _setInterval() {
                playStatus = 'play';
                _timer = window.requestAnimFrame(_setInterval)
                intervalExec();
            }

            function intervalExec() {
                var time = new Date()
                if (time - startTime >= speed || index == 0) {
                    startTime = time;
                    if (src[index]) {
                        layer = _setLayer(that, prevPath + src[index], imageExtent);
                        loopCallBack(src[index]);
                    }
//                $("#starttimeonmap").text(filetimeArray[index]);
                    index++;
                    if (index >= src.length) {
                        isEnd = true;
                        index = 0;
                        prveLayer = [];
                        window.cancelAnimationFrame(_timer);
                        window.setTimeout(function () {
                            that._map.removeLayer(layer);
                            if (that.isSwitch) {
                                playStatus = 'stop';

                                if (completionCallBack) completionCallBack(isEndPause);
                            }
                            isEnd = false;
                        }, speed);
                    }
                }

            }

            function _setLayer(that, src, imageExtent) {
                var _prveLayer;
                if (prveLayer && prveLayer.length) {
                    _prveLayer = prveLayer[prveLayer.length - 1];
                }
                var layer = new ol.layer.Image({
                    source: new ol.source.ImageStatic({
                        url: src,
                        crossOrigin: null,          //跨域
                        projection: "EPSG:3857",    //投影
                        imageExtent: imageExtent,
                        imageLoadFunction: function (image, src) {
                            image.getImage().src = src;
                            image.getImage().onload = function () {
                                if (_prveLayer) that._map.removeLayer(_prveLayer);
                            };
                        }
                    }),
                    visible: true,
                    zIndex: 10,
                    opacity: .8
                });
                // layer.setZIndex(9);                 //设置z-index属性
                that._map.addLayer(layer);           //将其加入值map中
                prveLayer.push(layer);
                return layer;
            }

            return {
                //停止
                stop: function () {
                    index = 0;
                    playStatus = 'stop';
                    cancelAnimationFrame(_timer);

                    that._map.removeLayer(prveLayer[prveLayer.length - 1]);
                },
                //暂停
                pause: function () {
                    if (isEnd) {
                        isEndPause = true;
                    }
                    playStatus = 'pause';
                    cancelAnimationFrame(_timer);
                },
                //继续
                resume: function () {
                    //如果暂停则继续
                    playStatus = 'play';
                    _timer = _setInterval(speed);
                },
                //获取播放状态
                getPlayStatus: function () {
                    return playStatus;
                },
                //获得当前Layer
                getCurrent: function () {
                    return layer;
                }
            };
        },
        playPicAnimateNew: function (prevPath, fileArray, speed, filetimeArray, loopCallBack, completionCallBack) {
            /**
             * prevPath: 路径前缀
             * fileArray: 文件名集合
             * speed:   图片切换速度
             * barId: 进度调元素ID
             * completionCallBack: 播放完成事件后的回调函数
             */
                //playStatus 播放状态 ready准备播放 play播放中 pause暂停 stop停止
            var _timer, layer, src, prveLayer = [], that = this, playStatus = 'ready', barid, index = 0, isEnd = false,
                isEndPause = false;

            var bl = ol.proj.transform([112.243, 26.432], 'EPSG:4326', 'EPSG:3857');
            var tr = ol.proj.transform([122.373, 36.992], 'EPSG:4326', 'EPSG:3857');
            var imageExtent = [bl[0], bl[1], tr[0], tr[1]];
            src = fileArray;               //将图片数组进行切割
            _timer = _setInterval(speed);

            // this._timer = _timer;
            function _setInterval(speed) {
                playStatus = 'play';
                intervalExec();
                if (index !== 0) {
                    return setInterval(function () {   //设置interval属性
                        intervalExec();
                    }, speed);
                }
                return setInterval(function () {
                }, 1);
            }

            function intervalExec() {
                if (src[index]) {
                    layer = _setLayer(that, prevPath + src[index].split(".")[0].split("_")[3].substring(0, 8) + "/" + src[index], imageExtent);
                    loopCallBack(src[index]);
                }
//                $("#starttimeonmap").text(filetimeArray[index]);
                index++;
                if (index >= src.length) {
                    isEnd = true;
                    index = 0;
                    prveLayer = [];
                    clearInterval(_timer);
                    window.setTimeout(function () {
                        that._map.removeLayer(layer);
                        if (that.isSwitch) {
                            playStatus = 'stop';

                            if (completionCallBack) completionCallBack(isEndPause);
                        }
                        isEnd = false;
                    }, speed);
                }
            }

            function _setLayer(that, src, imageExtent) {
                var _prveLayer;
                if (prveLayer && prveLayer.length) {
                    _prveLayer = prveLayer[prveLayer.length - 1];
                }
                var layer = new ol.layer.Image({
                    source: new ol.source.ImageStatic({
                        url: src,
                        crossOrigin: null,          //跨域
                        projection: "EPSG:3857",    //投影
                        imageExtent: imageExtent,
                        imageLoadFunction: function (image, src) {
                            image.getImage().src = src;
                            image.getImage().onload = function () {
                                if (_prveLayer) that._map.removeLayer(_prveLayer);
                            };
                        }
                    }),
                    visible: true,
                    zIndex: 10,
                    opacity: .8
                });
                // layer.setZIndex(9);                 //设置z-index属性
                that._map.addLayer(layer);           //将其加入值map中
                prveLayer.push(layer);
                return layer;
            }

            return {
                //停止
                stop: function () {
                    index = 0;
                    playStatus = 'stop';
                    clearInterval(_timer);

                    that._map.removeLayer(prveLayer[prveLayer.length - 1]);
                },
                //暂停
                pause: function () {
                    if (isEnd) {
                        isEndPause = true;
                    }
                    playStatus = 'pause';
                    clearInterval(_timer);
                },
                //继续
                resume: function () {
                    //如果暂停则继续
                    playStatus = 'play';
                    _timer = _setInterval(speed);
                },
                //获取播放状态
                getPlayStatus: function () {
                    return playStatus;
                },
                //获得当前Layer
                getCurrent: function () {
                    return layer;
                }
            };
        },
        vectorImageByPoint: function () {
            /*var layer = new ol.layer.Image({
                source: new ol.source.ImageStatic({
                    url: src,
                    crossOrigin: null,          //跨域
                    projection: "EPSG:3857",    //投影
                    imageExtent: imageExtent,
                    imageLoadFunction: function(image, src){
                        image.getImage().src = src;
                        image.getImage().onload = function(){
                            if(_prveLayer) that._map.removeLayer(_prveLayer);
                        };
                    }
                }),
                visible: true
            });*/
        },
        vectorImgBuddleByProvinces: function (arr, zoomFlag) {
            var that = this;
            /*arr = [
                {sblx : "102000",ywid:"info_imgss_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "江苏省",sbCount:31,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ],
                    callback:"",
                    callbackpp:"",
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "陕西省",sbCount:35,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "四川省",sbCount:67,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "新疆维吾尔自治区",sbCount:86,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "青海省",sbCount:99,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "黑龙江省",sbCount:191,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "吉林省",sbCount:87,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "辽宁省",sbCount:49,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "内蒙古自治区",sbCount:199,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "山西省",sbCount:69,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "湖南省",sbCount:121,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "浙江省",sbCount:129,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "西藏自治区",sbCount:129,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                },
                {sblx : "102000",ywid:"info_imgpp_jkfx_gl",title:"信息",
                    sbarray : [
                        {
                            sbid :  "甘肃省",sbCount:129,color : "red",
                            btns:[ {id:"zsgl_bd", name:"站所管理"}],
                            params:[{key : "电站名称", value :"安吉站"}, {key : "巡视班组", value : "巡视三组", link : "2"}]
                        },
                    ]
                }
            ];*/

            var city = [];
            for (var i = 0; i < arr.length; i++) {
                for (var j = 0; j < arr[i].sbarray.length; j++) {
                    city.push(arr[i].sbarray[j].sbid);
                }
            }
//            cql_filter:"fname in ('" + city.join(',').replace(/,/g, "','") + "')",
            this.getFeature({
                type: 'post',
                version: '1.0.0',
                typename: 'pollution:jcdl_qg_shengjie_polygon',
                srsname: 'EPSG:4326',
                cql_filter: "fname like '%" + city.join(',').replace(/,/g, "%' or fname like '%") + "%'",
                callback: function (json) {
                    var geojsonFormat = new ol.format.GeoJSON();
                    var fts = geojsonFormat.readFeatures(json, {
                        dataProjection: this._srsNameDataSource,
                        featureProjection: this._srsNameMap
                    });
                    for (var k = 0; k < fts.length; k++) {
                        for (var i = 0; i < arr.length; i++) {
                            for (var j = 0; j < arr[i].sbarray.length; j++) {
                                if (fts[k].get('name') == arr[i].sbarray[j].sbid) {
                                    fts[k].set('professionName', arr[i].ywid);
                                    fts[k].set('sbid', arr[i].sbarray[j].sbid);
                                }
                            }
                        }
                    }
                    that.vectorFeatures_s(arr, fts, zoomFlag);
                }
            });
        },
        vectorImgBuddleByProvincesClose: function (ywidArr) {
            var that = this;
            //数据源数组
            var fts = [];
            //非空验证
            if (that.vectorImgAndBuddleLayer != null) {
                //获取数据源
                var source = that.vectorImgAndBuddleLayer.getSource();
                //数据源遍历
                source.forEachFeature(removeLayer);
                //遍历数组
                for (var i = fts.length - 1; i >= 0; i--) {
                    //遍历后删除
                    source.removeFeature(fts[i]);
                }
                //that.map.removeLayer(that.vectorImgAndBuddleLayer);
                //that.vectorImgAndBuddleLayer = null;
            }

            function removeLayer(feature) {
                //遍历数据源
                for (var i = 0; i < ywidArr.length; i++) {
                    //删除冒泡
                    $("." + ywidArr[i] + "pop").parent($(".ol-overlay-container")).remove();
                    //获取业务ID对应的的对象
                    if (feature.get('professionName') == ywidArr[i]) {
                        //添加都数组里
                        fts.push(feature);
                    }
                }
            }

        },
        getFeature: function (options) {
            $.ajax({
                url: this.mapUrl,
                type: options.type,
                dataType: "json",
                async: false,
                data: {
                    service: 'WFS',
                    version: options.version,
                    request: 'GetFeature',
                    typename: options.typename,
                    srsname: options.srsname,
                    outputFormat: 'application/json',
                    viewparams: options.viewparams,
                    bbox: (options.extent == undefined) ? undefined : options.extent.join(',') + ',' + options.srsname, //bbox和filter不能同时使用
                    cql_filter: options.cql_filter,
                    filter: options.filter
                },
                success: options.callback,
                error: options.error
            });


        },
        vectorFeatures_s: function (arr, fts, zoomFlag) {
            var that = this;
            //样式
            var styleFunction = function (feature, resolution) {
                //业务ID
                var ywid = feature.get('professionName');
                var style;
                var size = 0.6;
                //图标类型
                if (ywid.indexOf('imgpp') >= 0 || ywid.indexOf('img_') >= 0) {
                    //公共路径
                    var src = 'resources/images/map_lable_img/gl.png';
                    if (ywid.indexOf('imgpp_zxjcfx') > -1) {
                        size = 0.8;
                    }
                    //点
                    if (feature.get('geometry').getType() == "Point") {   // 故障研判
                        style = new ol.style.Style({
                            image: new ol.style.Icon({
                                src: src,
                                scale: size
                            }),
                        });
                    }
                    //面或者线
                    else if (feature.get('geometry').getType() == "Polygon" || feature.get('geometry').getType() == "LineString"
                        || feature.get('geometry').getType() == "MultiPolygon") {
                        style = new ol.style.Style({
                            image: new ol.style.Icon({
                                src: src,
                                scale: size
                            }),
                            geometry: function (feature) {
                                var poly = feature.getGeometry();
                                var ext = poly.getExtent();
                                return new ol.geom.Point(ol.extent.getCenter(ext));
                            },
                        });
                    }
                } else {
                    //点
                    if (feature.get('geometry').getType() == "Point") {   // 故障研判
                        style = new ol.style.Style({
                            image: new ol.style.Circle({
                                radius: 20,
                                fill: new ol.style.Fill({
                                    color: 'rgba(255,0,0,0.01)'
                                })
                            }),
                        });
                    }
                    //面或者线
                    else if (feature.get('geometry').getType() == "Polygon" || feature.get('geometry').getType() == "LineString"
                        || feature.get('geometry').getType() == "MultiPolygon") {
                        style = new ol.style.Style({
                            image: new ol.style.Circle({
                                radius: 20,
                                fill: new ol.style.Fill({
                                    color: 'rgba(255,0,0,0.01)'
                                })
                            }),
                            geometry: function (feature) {
                                var poly = feature.getGeometry();
                                var ext = poly.getExtent();
                                return new ol.geom.Point(ol.extent.getCenter(ext));
                            },
                        });
                    }
                }
                return [style];
            };

            //是否为空
            if (this.vectorImgAndBuddleLayer == null) {
                //为空则new新的图层
                this.vectorImgAndBuddleLayer = new ol.layer.Vector({
                    source: new ol.source.Vector({
                        features: fts,
                    }),
                    style: styleFunction
                });
                //添加到map
                this._map.addLayer(this.vectorImgAndBuddleLayer);
                //层级
                this.vectorImgAndBuddleLayer.setZIndex(31);
            } else {
                var source = new ol.source.Vector({
                    features: fts,
                });
            }


            //数据源
            var source = this.vectorImgAndBuddleLayer.getSource();
            var arr_list = [];
            //遍历数据源
            source.forEachFeature(function (feature) {
                for (var i = 0; i < arr.length; i++) {
                    for (var j = 0; j < arr[i].sbarray.length; j++) {
                        //判断对应关系
//                        if (feature.get('fname') == arr[i].sbarray[j].sbid) {
                        var isMasch = feature.get('fname').match(arr[i].sbarray[j].sbid);
                        if (isMasch && isMasch.length) {
                            var sbid = arr[i].sbarray[j].sbid;
                            var sbCount = arr[i].sbarray[j].sbCount;
                            var message = sbid + "：" + sbCount + "串";
                            var div = "<div onmouseover='jhd_move_callback(\"" + arr[i].sbarray[j].sbid + "\"," + arr[i].sbarray[j].sbCount + ",\"" + arr[i].sbarray[j].dmmc + "\",\"" + arr[i].sbarray[j].count + "\")' onMouseOut='jhd_out_callback()'  id='" + arr[i].sbarray[j].sbid + "' class='ol-popup1 " + arr[i].ywid + "pop' style='cursor: pointer;z-index: 23;background-color: #244e7d;position:relative;padding: 3px;left: -23px;border-radius: 5px;text-align: center;'>" + message + "</div>";
                            /* var arr1 =
                                 {
                                     sblx: "102000", ywid:"jcd_zx",title:"",
                                     sbarray: [
                                         {
                                             sbid: "00119007947172", color: "red",
                                             "coordinates": [feature.get('cx'),feature.get('cy')],
                                             btns: [],
                                             params: [],
                                             divhtml:divss
                                         }
                                     ]
                                 };
                             arr_list.push(arr1);*/
                            //页面加载
                            if ($("#" + arr[i].sbarray[j].sbid).length == 0) {
                                $("body").append(div);
                            }
                            //如存在则替换
                            else {
                                $("#" + arr[i].sbarray[j].sbid).parent().remove();
                                $("body").append(div);
                            }

                            //坐标
                            var coor = feature.get('geometry').getCoordinates();

                            //取点坐标
                            if (feature.get('geometry').getType() != 'Point') {
                                var extent = feature.get('geometry').getExtent();
                                //中心点
                                coor = ol.extent.getCenter(extent);
                            }
                            var altcoor = ol.proj.transform(coor, 'EPSG:4326', 'EPSG:3857');
                            //overlay对象
                            var overlay = new ol.Overlay({
                                //绑定div
                                element: document.getElementById(arr[i].sbarray[j].sbid),
                            });
                            //设置坐标点
                            overlay.setPosition(altcoor);
                            //添加到地图
                            that._map.addOverlay(overlay);
                            //return;
                        }
                    }
                }
            });
            //that.vectorPointByCoordinate(arr_list);

        },
        setWqtLayer: function (param) {
            //五区图
            this._map.removeLayer(this.wqLayer);
            this.wqLayer = new ol.layer.Tile({
                source: new ol.source.TileWMS({
                    url: this.mapUrl,
                    params: {
                        VIEWPARAMS: param,
                        FORMAT: 'image/png',
                        LAYERS: 'pollution:querychinawuqu',
                        STYLES: ''
                    },
                    crossOrigin: 'Anonymous',
                    serverType: 'geoserver'
                }),
                visible: true
            });
            this._map.addLayer(this.wqLayer);
            return this;
        },
        setCenterAndZoom: function (coordinate, zoom) {
            this._map.getView().setCenter(ol.proj.transform(coordinate == "" ? [105.11718749999997, 36.5978891330702] : coordinate, 'EPSG:4326', 'EPSG:3857'));
            //缩放
            this._map.getView().setZoom(zoom == "" ? 4.45 : zoom);
        },
        _setWxwpLayer: function () {
            if (!this.wxwpLayer)
                this._map.removeLayer(this.wxwpLayer);
            this.wxwpLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    url: this.satelliteUrl + '{z}/{x}/{y}.jpg'
                }),
                zIndex: 6
            });
            this._map.addLayer(this.wxwpLayer);
            return this;
        },
        romeveWxwpLayer: function () {
            if (this.wxwpLayer) {
                this._map.removeLayer(this.wxwpLayer);
                this.wxwpLayer = void 0;
            }
        },
        /**
         * 添加街道图层
         * @returns {Efgis}
         * @private
         */
        _setJieDaoLayer: function () {
            if (!this.roadLayer)
                this._map.removeLayer(this.roadLayer);
            this.roadLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    url: this.roadUrl + '{z}/{x}/{y}.png'
                }),
                zIndex: 6
            });
            this._map.addLayer(this.roadLayer);
            return this;
        },
        /**
         * 移除街道图层
         */
        removeJieDaoLayer: function () {
            if (this.roadLayer) {
                this._map.removeLayer(this.roadLayer);
                this.roadLayer = void 0;
            }
        },
        /**
         * 添加地形图层
         * @returns {Efgis}
         * @private
         */
        _setDiXingLayer: function () {
            if (!this.terrainLayer)
                this._map.removeLayer(this.terrainLayer);
            this.terrainLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    url: this.terrainUrl + '{z}/{x}/{y}.jpg'
                }),
                zIndex: 6
            });
            this._map.addLayer(this.terrainLayer);
            return this;
        },
        /**
         * 移除地形图层
         */
        removeDiXingLayer: function () {
            if (this.terrainLayer) {
                this._map.removeLayer(this.terrainLayer);
                this.terrainLayer = void 0;
            }
        },
        setCenterAndZoomAnimation: function (coordinate, zoom) {
            var pan = ol.animation.pan({
                duration: 1200,
                source: /** @type {ol.Coordinate} */ (this._map.getView().getCenter())
            });
            this._map.beforeRender(pan);
            this._map.getView().setCenter(coordinate);
            var zoomObj = ol.animation.zoom({
                resolution: this._map.getView().getResolution()
            });
            this._map.beforeRender(zoomObj);
            this._map.getView().setResolution(this._map.getView().getResolution() * 2);
            this._map.getView().setZoom(zoom || defaultZoom);
        },
        /*
        * 添加220kv图层
        * @returns {Efgis}
        * @private
        * */
        _set220kvPointLayer: function () {
            if (this.Layer220kv)
                this._map.removeLayer(this.Layer220kv);
            this.Layer220kv = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    ratio: 1,
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        STYLES: '',
                        LAYERS: 'pollution:220point',
                        tiled: true
                    }
                }),
                zIndex: 9
            });
            this._map.addLayer(this.Layer220kv);
            return this.Layer220kv;
        },
        _set220kvLineLayer: function () {
            if (this.Layer220kvLine)
                this._map.removeLayer(this.Layer220kvLine);
            this.Layer220kvLine = new ol.layer.Image({
                source: new ol.source.ImageWMS({
                    ratio: 1,
                    url: this.mapUrl,
                    params: {
                        FORMAT: 'image/png',
                        STYLES: '',
                        LAYERS: 'pollution:220line',
                        tiled: true
                    }
                }),
                zIndex: 9
            });
            this._map.addLayer(this.Layer220kvLine);
            // console.log("aaa")
            return this.Layer220kvLine;
        },
        /*
        * 移除220kv图层
        * */
        remove220kvLayer: function (line) {
            // this._map.removeLayer(point)
            this._map.removeLayer(line)
            this._map.removeLayer(this.vectorPointLayer220kv)
            this.vectorPointLayer220kv = null;
            // console.log(vectorPointLayer220kv);
        }
    };
    //添加常量
    _setconst();

    Efgis.fn._init.prototype = Efgis.fn;
    return Efgis;
});

/**
 * 点击集合点回调函数
 */
var jhd_click_callback = function (p) {
};

/**
 * 点击集合点回调函数
 */
var jhd_move_callback = function (p, count) {
    // console.log("p")
    //   console.log(p)


    // var Obj = document.getElementById("blockCity");
    // document.getElementById("blockCity").innerHTML = "";
    // for (var i=0;i < onecount; i++){
    //     if (subcat[i][1] == locationid)
    //     {
    //         document.getElementById("blockCity").innerHTML += subcat[i][0]+"<br />";
    //         document.getElementById("blockCity").style.left = event.x;
    //         document.getElementById("blockCity").style.top = event.y + document.documentElement.scrollTop - 40;
    //         document.getElementById("blockCity").style.display = "block";
    //     }
    // }
};
var jhd_out_callback = function () {

    // console.log("p")
    //   console.log(p)

    // var Obj = document.getElementById("blockCity");
    // document.getElementById("blockCity").innerHTML = "";
    // for (var i=0;i < onecount; i++){
    //     if (subcat[i][1] == locationid)
    //     {
    //         document.getElementById("blockCity").innerHTML += subcat[i][0]+"<br />";
    //         document.getElementById("blockCity").style.left = event.x;
    //         document.getElementById("blockCity").style.top = event.y + document.documentElement.scrollTop - 40;
    //         document.getElementById("blockCity").style.display = "block";
    //     }
    // }
};
