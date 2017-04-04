function printRep(opt){
	//标题
	this.title = opt.title;
	//条件
	this.conditions = opt.conditions;
	//合并表头
	this.groupHeader = opt.groupHeader;
	//表格标题
	this.header = opt.header;
	//数据模型
	this.colModel = opt.colModel;
	//数据
	this.dataList = opt.dataList;
	//合计
	this.amount = opt.amount;
	
	this.dataSrc = opt.dataSrc;
	//除表头、条件行、表格标题后的高度
	this.leftH = opt.leftH;
	//行高
	this.eachRowH = opt.eachRowH;
	//是否打印工资条, 每一行都有表头
	this.isAll = opt.isAll;
	//每页打印行数
	this.rowNum = opt.rowNum
}

printRep.prototype = {
	print : function(){
		//获取公司名字
		var company = null;
	    $.ajax({type : "get",url : "getMyConfig.action",async : false,
	      success : function(cofig){ company = cofig.company;}
       });
	    
		$("#printContainer").remove();
    	$('body').append("<div id='printContainer'></div>");
    	
    	//*****************报表头，标题+条件行Start*******************
    	//标题  
    	var titleDiv = "<div style='text-align:center;font-family:SimHei;font-size:20px;'>"+company +"&nbsp;&nbsp;"+this.title+"</div>";
    	
    	//条件
    	var condRow = '';
    	if(this.conditions){
    		var conLength = this.conditions.length;
    		
    		var conGridWidth = '50%';
    		if(conLength<4){
    			conGridWidth = (conLength*12.5)+'%';
    		}
    		
    		condRow+="<table width='100%' style='margin-bottom:10px;'>";
			condRow+="<tr>";
			
			//服装系统一共就只有2个条件, 为了让日期居中(在数组第二个)  一共设置3个单元格
        	$.each(this.conditions,function(i,v){
        		if(i == 1){
        			condRow+="<td width = '35%' style='text-align:center;'>";
        		}else{
        			condRow+="<td width = '35%' style='text-align:left;'>";
        		}
        		condRow+=v.label+': ' + v.condVal;
        		condRow+="</td>";
        	});
        	condRow+="<td width = '30%' style='text-align:right;'></td>";
    		
        	condRow+="</tr></table>";
    	}
    	
    	var pageHeader = "";
    	if(!this.isAll){
    		pageHeader = titleDiv +condRow;
    	}
    	//*****************报表头，标题+条件行End*******************
    	
    	
    	
    	
    	//*****************表格标题 Start**************************
    	var gridHeader = '';
    	//合并表头
    	if(this.groupHeader){
    		gridHeader += this.groupHeader;
    	}
    	
    	//表格标题
    	gridHeader+="<tr>";
    	
    	if(this.header){//没有
    		var header = this.header;
        	$.each(header,function(i,v){
        		gridHeader+="<th>";
        		gridHeader+=v;
        		gridHeader+="</th>";
        	});
        	gridHeader+="</tr>";
    	}
    	
    	//*****************表格标题 End**************************
    	
    	//除标题、条件行、表格标题后的高度
		var leftH = this.leftH ? this.leftH : 530;
		
		
		//每页容纳数据行数
		var rowCount =  this.rowNum && this.rowNum > 0? this.rowNum : parseInt(leftH / eachRowH);
		
		//每行高度
		var eachRowH = 0
		if(this.isAll){//需要加上表头的高度
			eachRowH = 500/rowCount-25
		}else{
			eachRowH = this.eachRowH ? this.eachRowH : leftH/rowCount;
		}
		
		
    	var dataList = [];
    	//是否直接取内容
    	var contentFlag = false;
    	if(this.dataSrc){
    		//直接取内容
    		dataList = $('#'+this.dataSrc+' tr');
    		contentFlag = true;
    	}else{
    		dataList = this.dataList;
    	}
    	//数据行数
		var totalRow = dataList.length;
		//页数
		var pageNum = parseInt(totalRow/rowCount) + 1;
		
		var content = '';
		var colModel = this.colModel;
		
		for(var pn=0;pn<pageNum;pn++){
			//打印一页
			//页头
			content += pageHeader;
			//表格
			content += '<table border="1" width="100%" style="border-collapse:collapse;text-align: center;">';
			//表格标题
			if(!this.isAll){
				content+=gridHeader + "<tr>";
			}
			var pStartIdx = pn*rowCount;
			var pEndidx = (pn+1)*rowCount;
			//表格数据
			for(var c=pStartIdx;c<pEndidx&&c<totalRow;c++){
				//当前行数据
				var v = dataList[c];
				if(contentFlag){
					//判断是否是第一行
					if($(v).hasClass('jqgfirstrow')){
						//第一行跳过
						continue;
					}
					
					//一行
	    			var tds = $('td',v);
	    			if(this.isAll){
	    				content+=gridHeader + "<tr>";
	    			}else{
	    				content+="<tr>";
	    			}
	        		$.each(tds,function(j,h){
	        			var colspan = $(h).attr('colspan');
	        			if(colspan){
	        				content+="<td colspan = "+colspan+" style='padding-left:20px;text-align:left;'>";
	        			}else{
	        				content+="<td>";
	        			}
	            		content+=$(h).text();
	            		content+="</td>";
	        		});
	        		content+="</tr>";
				}else{
					//兼容自定义数据行
	        		if(v.custRow){
	        			content += v.custContent;
	        		}else{
	        			//一行
	        			if(this.isAll){
		    				content+=gridHeader + "<tr height="+eachRowH+"px >";
		    			}else{
		    				content+="<tr height="+eachRowH+"px>";
		    			}
	        			
	            		$.each(colModel,function(j,h){  
	            			content+="<td>";
	            			//兼容 easyuigrid 
	            			if(v[h.name] || v[h.field]){
	            				content+= v[h.name] || v[h.field];
	            			}
	                		content+="</td>";
	            		});
	            		content+="</tr>";
	        		}
				}
			}
			//是否加入合计行
			//合计
	    	var amount = this.amount;
			if(pn == pageNum-1 && amount){
				//有可能会有多个合计行 amount 是数组
				if(amount.length){
					$.each(amount,function(index, value){
						content+="<tr>";
			    		$.each(colModel,function(j,h){
			    			var contenttemp =   amount[index][h.name] || amount[index][h.field];
			    			if(!contenttemp){
			    				contenttemp = "";
			    			}
			    			content+="<td>";
			        		content+=contenttemp
			        		content+="</td>";
			    		});
			    		content+="</tr>";
					})
				}else{
					content+="<tr>";
		    		$.each(colModel,function(j,h){
		    			content+="<td>";
		        		content+= amount[h.name] ||amount[h.field];
		        		content+="</td>";
		    		});
		    		content+="</tr>";
				}
	    		
			}
			
			content += '</table>';
			
			//分页
			if(pn<pageNum-1){
				content += '<div style="page-break-after:always"></div>';
			}
			
		}
    	
    	$("#printContainer").append(content);
    	$("#printContainer").jqprint();
    	$("#printContainer").hide();
	}
}