<!DOCTYPE html>
<html>
	<head>
    <meta charset="utf-8">
    <title>layui</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="/alllayui/layui/css/layui.css"  media="all">
    <script  src="/alllayui/layui/layui.js" charset="utf-8"></script>
    <script  src="/system/js/jquery.min.js" charset="utf-8"></script>
	</head>
	<body>
		<div class="layui-container">
			<div class="layui-row" style="margin:20px;text-align: center ;">
				<h1>${orgnews.ntitle} </h1> 
			</div>

			<div class="layui-row layui-word-aux" style="">
				<div class="layui-col-md3 layui-col-md-offset6">
					<i class="layui-icon layui-icon-rss"></i>  ${orgnewsAuthor}  
				</div>
				<div class="layui-col-md3 layui-col-space5">
					<i class="layui-icon layui-icon-date"></i>  ${orgnews.publishTime?string["yyyy-MM-dd,hh:mm a,EEEE"]}   
				</div> 
			</div> 
		</div>
		<hr class="layui-bg-gray">
		<!-- 正文 -->
		<div style="padding-left: 10%;padding-right: 10%;padding-top: 20px;">
			<div> 
             ${orgnews.ncontent}
			</div>
		</div>


		<div id="fileDiv"> 
		<input type="hidden" name='newsId' id="newsId"  value='${orgnews.id}'  >
		</div>
		
		<div id="commentAddDiv"  loadModule="${orgnews.allowComment}"> 
		</div>

		<div id="commentListDiv" loadModule="${orgnews.allowComment}"> 
		</div>
			
		<script>
			layui.use(['jquery'], function() {
				var $ = layui.$;
				$(function() {	    
				    $.ajax({
					    type: "POST", 
					    url: "/validatePermisson",
					    data: { 
				         "keys":['orgcomment/add','orgcomment/list'] 
				         },
					    dataType: 'json',
					    success: function(data) { 
					           var hasAdd = data.data['orgcomment/add'];
						       var hasList = data.data['orgcomment/list'];
						       if(hasAdd==true || hasAdd=='true' ){
						            var loadCommentAddDiv = $("#commentAddDiv").attr("loadModule");
									if(loadCommentAddDiv=='0'){ 
										$("#commentAddDiv").load("/orgcomment/add");
									}
						       }
						       if(hasList == true || hasList == 'true' ){
							        var loadCommentListDiv = $("#commentListDiv").attr("loadModule");
									if(loadCommentListDiv == '0'){
									      $("#commentListDiv").load("/orgcomment/list");
									}  
						       }	
					    }
					});
				 	    
				});
			});
		</script>


	</body>
</html>
