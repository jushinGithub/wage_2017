/**
 * 取当前日期，格式为yyyy-MM-dd
 * 
 * @returns {String}
 */
function getCurrDate(){
	var now = new Date();
	return transDate2String(now);
}

function transDate2String(mydate){
	var year = mydate.getFullYear();
	var month = mydate.getMonth()+1;
	month = month>9 ? month : ('0'+month);
	var day = mydate.getDate();
	day = day>9 ? day : ('0'+day);
	return year  + '-' + month + '-' + day;	
}
/**
 * 判断变量是否为空
 * 
 * @param o
 * @returns {Boolean}
 */
function isNullOrEmpty(o){
	if(o===undefined || o === null || o===''){
		return true;
	}
	return false;
}
/**
 * 生成新的RowId
 * 
 * @param gridId
 * @returns {Number}
 */
function getNextRowId(gridId){
	var maxid = 0;
	var idList = $("#"+gridId).jqGrid('getDataIDs');
	if(idList && idList.length>0){
		$.each(idList,function(i,v){
    		if(v>maxid){
    			maxid = v;
    		}
    	});
	}
	maxid = parseInt(maxid) +1;
	return maxid;
}
/**
 * 取最后一行的记录
 * 
 * @returns
 */
function getLastRowData(gridId){
	var idList = $("#"+gridId).jqGrid('getDataIDs');
	var idLen = idList.length;
	if(idLen == 0){
		return {};
	}
	
	var lastRowId = idList[idLen-1];
	var lastRow = $("#"+gridId).getRowData(lastRowId);
	return lastRow;
}

/**
 * 
 * @returns 货品号对应货品信息
 */
function getAllHph2Info(){
	var hph2Info = {};
	var hpList = getAllHp();
	$.each(hpList,function(i,v){
		hph2Info[v.goodsNo] = v;
	});
	return hph2Info;
}
/**
 * 根据货品号查询货品信息
 * 
 * @param hph
 * @returns {Array}
 */
function getHpInfoByHph(hph){
	var hpInfo = [];
	var url = "cxhpbyhph.action";
	var params = {hph:hph};
	var successFun = function(resp, textStatus) {
		hpInfo = resp.datas;
	};
	var errorFun = function(XMLHttpRequest, textStatus, errorThrown) {
	};
	$.ajax({
		type : "POST",
		url : url,
		data : params,
		success : successFun,
		error : errorFun,
		async : false
	});
	return hpInfo;
}
/**
 * 根据货品号取关联颜色
 * 
 * @param hp
 * @returns
 */
function findRelColorByHp(hp){
	var result = [];
	var params = {
		"goodNo" : hp
	};
	var result;
	var url = "cxhpglys.action";
	var successFun = function(data, textStatus) {
		result = data.rows;
	};
	var errorFun = function(XMLHttpRequest, textStatus,
			errorThrown) {
		result = [ false, 'error' ];
	};
	$.ajax({
		type : "POST",
		url : url,
		data : params,
		success : successFun,
		error : errorFun,
		async : false
	});
	return result;
}

function formatRelColorCell(rowid,iCol,gridId){
	var grid = $("#"+gridId);
	//hph
	var currRowHph = grid.getRowData(rowid).goodNo;
	var relColor = [];
	if(currRowHph){
		relColor = findRelColorByHp(currRowHph);
	}
	grid.getGridParam( "colModel" )[iCol].editoptions.value =  genYsSelectData(relColor,"color","colorNo","colorName");
}

/**
 * 根据货品号取该货品未新增裁剪记录的颜色
 * 
 * @param hph
 * @param gridId
 * @returns
 */
function getDressTailColor(hph,gridId){
	var relColorList = findRelColorByHp(hph);
	// 取表格中该货品号已对应的颜色
	var idList = $("#"+gridId).jqGrid('getDataIDs');
	if(idList && idList.length>0){
		var colorMap = {};
		$.each(idList,function(i,v){
			var rowData = jQuery("#"+gridId).getRowData(v);
			if(rowData.goodNo==hph){
				colorMap[rowData.colorId] = true;
			}
		});
		var result = [];
		$.each(relColorList,function(i,v){
			if(!colorMap[v.color]){
				result.push(v);
			}
		});
		return result;
	}
	return relColorList;
}

/**
 * 按url获取dataList
 * 
 * @returns {Array}
 */
function getAllData(url){
	var dataList = [];
	var successFun = function(data, textStatus) {
		dataList = data.datas;
	};
	var errorFun = function(XMLHttpRequest, textStatus, errorThrown) {
	}
	$.ajax({
		type : "POST",
		url : url,
		success : successFun,
		error : errorFun,
		async : false
	});
	return dataList;
}
/**
 * 按dataList组装单元格下拉列表
 * 
 * @param dataList
 * @param idField
 * @param nameField
 * @returns {String}
 */
function getSelectData(dataList,idField,nameField){
	var result = "";
	$.each(dataList, function(i, v) {
		if(i>0){
			result += ";"
		}
		result += v[idField] + ":" + v[nameField];
	});
	return result;
}
/**
 * 颜色选择
 * 
 * @param dataList
 * @param idField
 * @param nameField
 * @returns {String}
 */
function genYsSelectData(dataList,idField,noField,nameField){
	var result = "";
	if(dataList && dataList.length>0){
		$.each(dataList, function(i, v) {
			if(i>0){
				result += ";"
			}
			result += v[idField] + ":" + v[noField]+" "+v[nameField];
		});
	}else{
		alert('未找到对应的关联颜色,请先关联颜色后，再刷新该页面');
	}
	
	return result;
}
/**
 * 返回用于格式化的map
 * 
 * @param dataList
 * @param idField
 * @param nameField
 * @returns {___anonymous6142_6143}
 */
function getId2NameFormat(dataList,idField,nameField){
	var result = {};
	$.each(dataList, function(i, v) {
		result[v[idField]] = v[nameField];
	});
	return result;
}

function formatTrueFalse(cellvalue, options, rowObject){
	var map = {'true':'是','false':'否'};
	if(isNullOrEmpty(map[cellvalue])){
		return "";
	}else{
		return map[cellvalue];
	}
}

function unformatTrueFalse(cellvalue, options, cell){
	var map = {'是':'true','否':'false'};
	if(isNullOrEmpty(map[cellvalue])){
		return "";
	}else{
		return map[cellvalue];
	}
}
/**
 * 编辑单元格后事件 1.记录最后一次编辑的单元格的iRow和iCol,便于保存最后一个编辑的单元格及Tab键新增行或换行编辑列 2.将当前单元格的输入框全选
 * 
 * @param lastEditCell
 * @param rowid
 * @param cellname
 * @param value
 * @param iRow
 * @param iCol
 */
function myAfterEditCell(lastEditCell,rowid, cellname, value, iRow, iCol){
	lastEditCell.iRow = iRow;
	lastEditCell.iCol = iCol;
	// 全选输入框
	var currInputer = $('#'+iRow+'_'+cellname);
	//针对自定义列特殊处理
	//如货品号、色码
	var custCols = ['goodNo','colorNo'];
	if(currInputer[0] && currInputer[0].nodeName && $.inArray(cellname, custCols)>=0
			&& currInputer[0].nodeName.toUpperCase() == 'DIV'){
		currInputer = $('#'+iRow+'_'+cellname+'_input');
	}
	window.setTimeout(function () { currInputer.focus();},0);
	currInputer.select();
}
/**
 * Tab键至最后一列时，换行或新增行
 * 
 * @param gridId
 * @param lastEditCell
 * @param isCopyHh
 *            是否复制货号
 * @param e
 */
function myOnLastKeyDown(gridId,lastEditCell,e,isCopyHh,defaultRow){
	if (e.keyCode === 9)  {
		// Tab键
		// 换行或新增行
		var lastIRow = lastEditCell.iRow;
		var lastICol = lastEditCell.iCol;
		var rowLength = $("#"+gridId).getDataIDs().length;
		var nextIRow = lastIRow+1;
		if(lastIRow<rowLength){
			// 换行
		}else{
			// 新增行
			var nextId = getNextRowId(gridId);
			nextIRow = nextId;
			var rowData = defaultRow;
			if(nextId>1 && isCopyHh){
				// 取第一行的货品号、货品名称、单位
				var lastRowData = getLastRowData(gridId);
				rowData.goodNo = lastRowData.goodNo;
				rowData.goodName = lastRowData.goodName;
				rowData.unit = lastRowData.unit;
			}
			$("#"+gridId).jqGrid('addRowData', nextId, rowData );
		}
		
		var colModels = $("#"+gridId).getGridParam('colModel');
		var nCol=false;
		for (i=0; i<colModels.length; i++) {
			if ( colModels[i].editable ===true) {
				nCol = i; 
				break;
			}
		}
		if(nCol !== false) {
			setTimeout("jQuery('#"+gridId+"').editCell(" + nextIRow + ", "+nCol+", true);", 100);
		}
	}
}

/**
 * 数量变化时，总数相应发生变化
 * 
 * @param gridId
 * @param e
 */
function myOnNumBlur(gridId,e,colPrefix){
	if(!colPrefix){
		colPrefix = 'num'
	}
	var rowid = $("#"+gridId).getGridParam( "selrow" );
	var row = $("#"+gridId).getRowData(rowid);
	
	// 当前输入框
	var currInput = $( e.target);
	// 当前输入框的值
	var currColVal = currInput.val();
	// 当前输入框的名称
	var currColName = currInput.attr('name');
	
	var sumCount = 0;
	for(var i=1;i<8;i++){
		var numName = colPrefix+i;
		if(numName == currColName){
			sumCount += currColVal * 1;
		}else{
			sumCount += row[numName] * 1;
		}
	}
	$("#"+gridId).setCell(rowid,colPrefix,sumCount);
	return sumCount;
}
/**
 * 处理数量合计
 * 
 * @param grid
 * @param rowid
 * @param cellname
 * @param val
 * @param colPrefix
 * @param hjCfg
 *            单价，合价列:{djCol:'',hjCol:''}
 */
function myAfterSaveCell(grid,rowid,cellname,val,colPrefix,hjCfg,amountCfg){
	if(!colPrefix){
		colPrefix = 'num';
	}
	// 哪些列变化后需计算合计
	var cols = [];
	for(var i=1;i<8;i++){
		cols.push(colPrefix + i);
	}
	var row = grid.getRowData(rowid);
	var sumCount = 0;
	// 判断是从row里面取数量合计，还是从计算
	// 因设置数量合计列时，再去取时可能获取不到
	var flag = false;
	if($.inArray(cellname,cols)>=0){
		flag = true;
		for(var i=1;i<8;i++){
			var numName = colPrefix+i;
			if(numName == cellname){
				sumCount += val * 1;
			}else{
				sumCount += row[numName] * 1;
			}
		}
		grid.setCell(rowid,colPrefix,sumCount);
	}
	
	if(hjCfg && hjCfg.djCol && (hjCfg.djCol == cellname || $.inArray(cellname,cols)>=0)){
		// 重新计算合价
		var slSum = flag ? sumCount : row[colPrefix];
		var dj = row[hjCfg.djCol];
		grid.setCell(rowid,hjCfg.hjCol,(dj*slSum).toFixed(2));
	}
	if(amountCfg){
		// 处理合计
		calTotal(grid,amountCfg.amountText,amountCfg.amountCols);
	}
}

/**
 * 公共ajax请求方法
 * 
 * @param url
 * @param params
 * @returns
 */
function ajaxRequest(url,params){
	var result;
	var successFun = function(resp, textStatus) {
		result = resp;
	};
	var errorFun = function(XMLHttpRequest, textStatus, errorThrown) {
		alert(XMLHttpRequest.responseText);
	};
	$.ajax({
		type : "POST",
		url : url,
		data : params,
		success : successFun,
		error : errorFun,
		async : false
	});
	return result;
}
/**
 * 取共享对象
 * @param paramName
 * @returns
 */
function getShareObjByName(paramName){
	var result = ajaxRequest("hqggdx",{"shareObjName":paramName});
	return result[paramName];
}
/**
 * 取公司名称
 * @returns
 */
function getCompName(){
	var compInfo = getShareObjByName('compInfo');
	return compInfo.compName;
}

/**
 * 判断数据是否可编辑
 * @param sjlx
 * @param ny
 * @returns
 */
function ckAllowEdit(sjlx,rq){
	var flag = false;
	var ny = '';
	if(!isNullOrEmpty(rq)){
		ny = rq.substr(0,4)+rq.substr(5,2);
	}
	var result = ajaxRequest("jysjkbj",{"sjlx":sjlx,"ny":ny});
	var rsObj = result.rst;
	if(rsObj){
		if(rsObj.success){
			//成功
			flag = true;
		}else{
			alert(rsObj.msg);
		}
		
	}else{
		alert('请求失败。');
	}
	return flag;
}


function calTotal(grid,amountText,amountCols) {// 当表格所有数据都加载完成，处理统计行数据
    var rowNum = grid.jqGrid('getGridParam','records');
    var footRow = {};
    // 合计
	footRow[amountText] = '合计：';
    if(rowNum > 0){
    	$.each(amountCols,function(i,v){
    		footRow[v] = grid.getCol(v,false,"sum"); 
    	});
    }else{
    	$.each(amountCols,function(i,v){
    		footRow[v] = 0; 
    	});
    }
    grid.footerData("set",footRow);
}
/**
 * 金额转大写
 * 
 * @param num
 * @returns {String}
 */
function amounttoU(num) {
    if(isNaN(num))return "无效数值！";
    var strPrefix="";
    if(num<0)strPrefix ="(负)";
    num=Math.abs(num);
    if(num>=1000000000000)return "无效数值！";
    var strOutput = "";
    var strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';
    var strCapDgt='零壹贰叁肆伍陆柒捌玖';
    num += "00";
    var intPos = num.indexOf('.');
    if (intPos >= 0){
        num = num.substring(0, intPos) + num.substr(intPos + 1, 2);
    }
    strUnit = strUnit.substr(strUnit.length - num.length);
    for (var i=0; i < num.length; i++){
        strOutput += strCapDgt.substr(num.substr(i,1),1) + strUnit.substr(i,1);
    }
    return strPrefix+strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元");
};

function getUrlParam(url,name) 
{ 
	var idx = url.indexOf('?');
	if(idx==-1){
		return null;
	}
	var paramStr = url.substr(idx+1);
	
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); 
	var r = paramStr.match(reg); 
	if (r!=null) 
		return unescape(r[2]); 
	return null; 
} 

/**
 * 初始化报表表格
 */
function initReportGrid(gridParam){
	var gridId = gridParam.gridId;
	var url = gridParam.url;
	var colNames = gridParam.colNames;
	var colModel = gridParam.colModel;
	var buildParamFunc = gridParam.buildParamFunc;
	var pagerId = gridParam.pagerId;
	//分组
	var grouping = gridParam.grouping;
	var groupingView = gridParam.groupingView;
	if(groupingView){
		//不显示分组列
		groupingView.groupColumnShow = [false];
		//显示小计
		groupingView.groupSummary = [true];
	}
	
	var tabHeight = gridParam.height ? gridParam.height : 450;
	
	var pager = pagerId ? "#"+pagerId : null;
	var rowNum = pagerId ? 26 : null;
	var rowList = pagerId ? [ 20, 26, 30 ] : null;
	
	var loadComplete = gridParam.loadComplete ? gridParam.loadComplete : null;
		
	$("#"+gridId).jqGrid(
			{
				url : url,
				datatype : "local",
				colNames : colNames,
				colModel : colModel,
				ajaxSelectOptions :{},
				postData : buildParamFunc(),
				pager : pager,
				rowNum : rowNum,
				rowList : rowList,
				height : tabHeight,
				// toolbar:[true,"top"],
				sortname : "upNum",
				sortorder : "desc",
				grouping : grouping,
				groupingView : groupingView,
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
				},
				loadComplete : loadComplete
			});
}

function custSelectCell(opt){
	var dataList = opt.dataList;
	if(!dataList){
		dataList = getAllData(opt.url);
	}
	
	if(dataList && dataList.length>0){
		
		var callbackFunc = opt.callback;
		var defVal = opt.defVal;
		var id = opt.id;
		var $ret = $('<div id="'+id+'"></div>');
	    $ret.flexbox({results:dataList}, {  
	    	displayValue:opt.textField,
	    	hiddenValue:opt.valField,
	    	resultTemplate:'<div class="col1">{'+opt.textField+'}</div>',
	    	initialValue : defVal,
	    	width: opt.width,
	    	onSelect : callbackFunc,
	    	noResultsText : '未找到记录',
	    	paging: {  
		        pageSize: 8,
		        summaryTemplate: '显示 {start}-{end}条,共 {total} 条'
		    }  
		}); 
	    //callbackFunc.apply($ret,[defVal]);
	    return $ret;
	}else{
		alert('没有记录');
	}
}

function custHphComboCell(val,opt,callback){
	return custSelectCell({valField:"goodsNo",textField:"goodsNo",url:"syhp.action",width:120,callback:callback,defVal:val,id:opt.id});
}

function custYsComboCell(val,opt,callback,dataList){
	return custSelectCell({valField:"colorId",textField:"colorNo",dataList:dataList,width:120,callback:callback,defVal:val,id:opt.id});
}

function custFlexVal(p1,p2){
	return  $('#'+p1.attr('id')+'_input').val();
}

function delGridRow(grid,gidField,delUrl,callback,amountCfg){
	var id = grid.jqGrid('getGridParam','selrow');
    if (id)    {
    	if(confirm("确定删除该记录吗？")){
    		var row = grid.getRowData(id);
        	var gid = row[gidField];
        	if(!isNullOrEmpty(gid)){
        		//删除数据库中记录
        		var result = ajaxRequest(delUrl,{"gid":gid});
        		alert(result.msg);
        		callback && callback();
        	}
        	grid.jqGrid('delRowData', id, {} );
        	amountCfg && calTotal(grid,amountCfg.amountText,amountCfg.amountCols);
    	}else{
    		//取消删除
    	}
    } else { alert("请选择一行！");}   
}

/**
 * 是否全为空或0
 * @param numArray
 * @returns {Boolean}
 */
function isAllZero(numArray){
	var flag = true;
	if(numArray && numArray.length > 0){
		$.each(numArray,function(i,v){
			if(v){
				flag = false;
				//其中一个不为空或零，即可结束
				return false;
			}
		});
	}
	return flag;
}

/**
 * 过滤总数量为0的记录
 * @param dataList
 * @param totalField
 * @returns {Array}
 */
function filterZero(dataList,totalField){
	var result = [];
	if(dataList && dataList.length > 0){
		$.each(dataList,function(i,v){
			var val = 0;
			if(v[totalField]){
				val = parseFloat(v[totalField]);
			}
			if(val != 0){
				result.push(v);
			}
		});
	}
	return result;
}

function appendParam2Url(url,paramName,paramVal){
	if(url.indexOf('?')>=0){
		//已有参数
		url += '&';
	}else{
		//无参数
		url += '?';
	}
	url += paramName+'='+paramVal;
}





