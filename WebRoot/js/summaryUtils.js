
/**
 * 汇总工具类
 * @param opt
 * @returns {summaryUtils}
 */
function summaryUtils(opt){
	//汇总类型:1.按货号汇总；2.按货号色码汇总
	this.repType = opt.repType;
	//组装查询参数
	this.constructQryParamFunc = opt.constructQryParamFunc;
	//查询url
	this.postUrl = opt.postUrl;
	//标题
	this.repTitle = opt.repTitle;
	//是否销售单
	this.isSell = opt.isSell;
}

summaryUtils.prototype = {
	summary : function(){
		var colNames= [];
		var colModel = [];
		var cstQryParamFunc = this.constructQryParamFunc;
		var postData = cstQryParamFunc ? cstQryParamFunc() : {};
		var repType = this.repType;
		postData.repType = repType;
		if(repType == 1){
			// 按货号汇总
			colNames = ["货品号", "货品名称", "单位","总数量"];
			colModel  = [{
					name : "goodNo",
					width : 100,
					align : "center"
				}, {
					name : "goodName",
					width : 90
				}, {
					name : "goodUnit",
					width : 80,
					align : "right"
				}, {
					name : "num",
					width : 60,
					align : "right"
				}];
		}else if(repType == 2){
			// 按货号、颜色汇总
			colNames = ["货品号", "货品名称", "单位","色码","颜色","S", "M","L","XL","XXL","XXXL","XXXX","总数量"];
			colModel  = [{
					name : "goodNo",
					width : 100,
					align : "center"
				}, {
					name : "goodName",
					width : 90
				}, {
					name : "goodUnit",
					width : 80,
					align : "right"
				}, {
					name : "colorNo",
					width : 80,
					align : "right"
				}, {
					name : "colorName",
					width : 80,
					align : "right"
				}, {
					name : "num1",
					width : 40,
					align : "right"
				}, {
					name : "num2",
					width : 40,
					align : "right"
				}, {
					name : "num3",
					width : 40,
					align : "right"
				}, {
					name : "num4",
					width : 40,
					align : "right"
				}, {
					name : "num5",
					width : 40,
					align : "right"
				}, {
					name : "num6",
					width : 40,
					align : "right"
				}, {
					name : "num7",
					width : 40,
					align : "right"
				}, {
					name : "num",
					width : 60,
					align : "right"
				}];
		}
		
		if(this.isSell){
			//如为销售开票
			colNames.push("总金额");
			colModel.push({
				name : "total",
				width : 60,
				align : "right"
			});
		}
		
		$("#repList").GridUnload();
		
		$("#repList").jqGrid(
		{
			url : this.postUrl,
			datatype : "json",
			postData:postData,
			colNames : colNames,
			colModel : colModel,
			ajaxSelectOptions :{},
			pager : "#repPager",
			rowNum : 27,
			rowList : [ 20, 27, 30 ],
			height : 250,
			// toolbar:[true,"top"],
			sortname : "upNum",
			sortorder : "desc",
			viewrecords : true,
			gridview : true,
			autoencode : true,
			userDataOnFooter : true,
			footerrow : true,
			caption : "查询结果",
			jsonReader : {
				root : "rows", // json中代表实际模型数据的入口
				page : "currPage", // json中代表当前页码的数据
				total : "totalPage", // json中代表页码总数的数据
				records : "totalRecords", // json中代表数据行总数的数据
				repeatitems : false,
				id : "0"
			}
		});
		
		$("#repList").jqGrid("setGridParam",{page:1}).trigger("reloadGrid");
		var title = this.repTitle;
		//加入窗口标题
	 	$('#repWin').window({
	   	 title : title
	   });
	 	//加入表格标题
	 	$("#repGridTitle").remove();
	 	var gridWidth = $("#gbox_repList").width();
	 	$("#gbox_repList").before("<div id='repGridTitle' style='font-size:20px;font-family: SimHei;font-weight: bold;text-align:center;width:"+gridWidth+"px;'>"+title+"</div>");
	 	
	   $('#repWin').window('open');
	}
};