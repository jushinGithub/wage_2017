function printBillUtils(opt){
	//标题
	this.title = opt.title;
	//条件行
	this.condRow = opt.condRow;
	//页尾
	this.pageFoot = opt.pageFoot;
	//表格标题
	this.gridHeader = opt.gridHeader;
	//表格模型 
	this.gridModel = opt.gridModel;
	//非打印列
	this.noPrtCols = opt.noPrtCols;
	//表格数据
	this.gridData = opt.gridData;
	//合计行
	this.amountRow = opt.amountRow;
	//尾部
	this.footer = opt.footer;
}

printBillUtils.prototype = {
	print : function(){
		$("#printContainer").remove();
    	$('body').append("<div id='printContainer'></div>");
		//除页头、页尾、表格标题后的高度
		var titleDiv = '<div style="text-align: center;"><span class="mytitle">'+this.title+'</span></div>';
		var gridHeader = this.gridHeader;
		//除标题、条件行、表格标题后的高度
		var leftH = 400;
		//每页容纳数据行数
		var rowCount = parseInt(leftH / 17);
		
		var dataList = this.gridData;
		//数据行数
		var totalRow = dataList.length;
		
		//页数
		var pageNum = parseInt(totalRow/rowCount) + 1;
		
		//页头为标题+条件行
		var pageHeader = titleDiv + this.condRow;
		var content = '';
		var gridModel = this.gridModel;
		var noPrtCols = this.noPrtCols;
		for(var pn=0;pn<pageNum;pn++){
			//打印一页
			//页头
			content += pageHeader;
			//表格
			content += '<table border="1" width="100%" style="border-collapse:collapse;text-align: center;">';
			//表格标题
			content += gridHeader;
			var pStartIdx = pn*rowCount;
			var pEndidx = (pn+1)*rowCount;
			//表格数据
			for(var c=pStartIdx;c<pEndidx&&c<totalRow;c++){
				//当前行数据
				var v = dataList[c];
				var newRow = "<tr>";
				$.each(gridModel,function(j,h){
					if($.inArray(h.name,noPrtCols)==-1 && h.hidden != true){
						newRow+="<td>";
	        			if(v[h.name]){
	        				newRow+=v[h.name];
	        			}
	        			newRow+="</td>";
					}
        		});
				newRow += "</tr>";
				content += newRow;
			}
			//是否加入合计行
			if(pn == pageNum-1 && this.amountRow){
				content += this.amountRow;
			}
			
			content += '</table>';
			//页尾
			if(this.pageFoot){
				content += this.pageFoot;
			}
			if(pn == pageNum-1){
				//最后一页
				//加入尾部
				content += this.footer;
			}
			
			//分页
			if(pn<pageNum-1){
				content += '<div style="page-break-after:always"></div>';
			}
		}
		$("#printContainer").append(content);
		$("#printContainer").css({'font-size':'12px'});
		$("#printContainer table").css({'font-size':'12px'});
    	$("#printContainer").jqprint();
    	$("#printContainer").hide();
	}
};