function getDiZhiYun(urlType)
    {
         var projection = ol.proj.get("EPSG:4326");
         var projectionExtent = projection.getExtent();
         var size = ol.extent.getWidth(projectionExtent) / 256;
         var resolutions = [];

         var url = "http://219.142.81.86/igserver/ogc/kvp/"+ urlType+"/WMTSServer/1.0.0/"+ urlType+"/default/EPSG:4326_"+ urlType+"_dpi96_GB/{TileMatrix}/{TileRow}/{TileCol}.png";
         for (var z = 1; z < 16; ++z) {
             resolutions[z-1] = size / Math.pow(2, z);
         }
         var layersInfo = new ol.layer.Tile({
             title: "layerId=2048",
             visible: true,
             source: new ol.source.WMTS({
                 crossOrigin: 'anonymous',
                 name: "test",
                 tileGrid: new ol.tilegrid.WMTS({
                     //分辨率，每级对应的分辨率，可在arcgis server的mapserver最后面找到
                     resolutions: resolutions,
                     //原点
                     origin: [-180, 90],
                     tileSize: 256,
                     //每级对应的id，与分辨率数组长度一致，必填。可在WMTS描述文档的<ows:Identifier>找到
                     matrixIds: ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "11", "12", "13" ]

                 }),
                 matrixSet: 'default028mm',
                 projection: projection,
                 layer: "AZImages",
                 style: "default",
                 version: "1.0.0",
                 format: "image/jpg",
                 transition: 0,
                 opaque: 0,
                 requestEncoding: "REST",
                 //路径可以在WMTS描述文档里面的<ResourceURL>里找到
                 url: url
             })
         });
         return layersInfo;
    }

