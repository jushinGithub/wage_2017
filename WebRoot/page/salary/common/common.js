	
	/** 设置取月份日期控件 */
	function setMonthDate(){
		//设置月份 只能选择年和月
	    $('#month').datebox({
	        onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
	            span.trigger('click'); //触发click事件弹出月份层
	            if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
	                tds = p.find('div.calendar-menu-month-inner td');
	                tds.click(function (e) {
	                    e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
	                    var year = /\d{4}/.exec(span.html())[0]//得到年份
	                    , month = parseInt($(this).attr('abbr'), 10); //月份，这里不需要+1
	                    $('#month').datebox('hidePanel')//隐藏日期对象
	                    .datebox('setValue', year + '-' + month); //设置日期的值
	                });
	            }, 0)
	        },
	        parser: function (s) {
	            if (!s) return new Date();
	            var arr = s.split('-');
	            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
	        },
	        formatter: function (d) { 
	        	var monthR = d.getMonth()+1;
	        	if(monthR < 10){
	        		monthR = '0'+monthR;
	            }
	        	return d.getFullYear() + '-' + monthR;/*getMonth返回的是0开始的，忘记了。。已修正*/ 
	        }
	    });
	    var p = $('#month').datebox('panel'), //日期选择对象
	    tds = false, //日期选择对象中月份
	    span = p.find('span.calendar-text'); //显示月份层的触发控件
	    //设置默认值, 默认上一个月
	    var curr_time = new Date();
	    var strDate = curr_time.getFullYear()+"-";
	    strDate += curr_time.getMonth();
	    $("#month").datebox("setValue", strDate); 
	}
	
	/** 结束编辑状态 */
	function endEditing(){
		if (editIndex == undefined){return true}
		
		if ($('#dg').datagrid('validateRow', editIndex)){
			$('#dg').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	
	/** 双击 选中行 */
	function ondbClickRow(index,row){
		endEdit();
		$('#dg').datagrid('selectRow', index).datagrid('beginEdit', index);
	}
	
	/** 撤销 */
	function reject(){
		$('#dg').datagrid('rejectChanges');
		editIndex = undefined;
	}
	
	/** 结束并接受编辑 */
	function endEdit(){
		var length = $('#dg').datagrid('getRows').length;
		for(var i=0; i<length; i++){
			if ($('#dg').datagrid('validateRow', i)){
				$('#dg').datagrid('endEdit', i);
			}
		}
	}
	
	
	/** 回去设置的小数点保留位数 
	 *   indexArray [{index:1,field:'birthday'}]
	 * */
	function getConfig(){
		var decimal = 0;
		//获取小数点包保留的位数
	    $.ajax({type : "get",url : "getMyConfig.action",async : false,
	      success : function(cofig){ decimal = cofig.decimals;}
       });
	   return decimal;
	}
	
	
	/** 打印 */
	function printData(event){
		var date = $('#month').length && $('#month').datebox('getValue');
		var  isAll = false ;
		if(event.data.isAll){
			//查询所有数据
			$.ajax({
				url:'getSalary.action',
				data:'month='+date+'&isPiece='+event.data.isPiece,
				 async:false,
				success:function(result){
					if(result && result.total > 0){
						dataList = result
					}
				}
			});
			isAll = true;
		}else{
			dataList = $('#dg').datagrid('getData');
			isAll = false;
		}
		
		if(!dataList || dataList.length == 0){
			$.messager.show({msg:'没有打印的数据!',title:'提示'});
			return;
		}
		
		//每页显示行数 
		var rowNum = event.data.rowNum;
		
		//1. 查询条件 
		var condition = [];
		var shopName = $("#shop").length && $("#shop").combotree("getText");
		var orgName = $("#orgId").length && $("#orgId").combotree("getText");
		if(orgName){
			condition.push({label:"班组",condVal:orgName});
		}
		if(shopName){
			condition.push({label:"车间",condVal:shopName});
		}
		if(date){
			condition.push({label:"月份",condVal:date});
		}
		
		var optsfreeze = $('#dg').datagrid('getColumnFields',true); //这是获取到所有的FIELD
		var opts = $('#dg').datagrid('getColumnFields');
		var optsAll = [];
		if(optsfreeze.length){
			if($.inArray('ck',optsfreeze) > -1){
				optsfreeze.splice($.inArray('ck',optsfreeze),1);
			}
			optsAll = optsfreeze.concat(opts);
		}else{
			if($.inArray('ck',opts) > -1){
				opts.splice($.inArray('ck',opts),1);
			}
			optsAll = opts;
		}
		
		var repColNames=[]; //表头
		var repColModel=[]; //列配置
		
		for(i=0;i<optsAll.length;i++){
			var col = $('#dg').datagrid( "getColumnOption" , optsAll[i] );
			if(!col.hidden){
				repColNames.push(col.title);
				repColModel.push(col);
			}
		}
		
		var groupHeader = null;
		if(event.data.compHeader){
			repColNames = null;
			groupHeader = event.data.compHeader;
		}
		
		var printer = new printRep({
			title:event.data.title,
			conditions : condition,
			header : repColNames,
			groupHeader: groupHeader,
			colModel: repColModel,
			dataList : dataList.rows,
			amount : dataList.footer,
			isAll: isAll,
			rowNum: rowNum
		});
		
		printer.print();
	}
	
	
	//EasyUI datagrid 动态导出Excel
	function exportExcel(event) {
		//1.到导出的数据
		var  dataList = $('#dg').datagrid('getData');
		if(!dataList || dataList.rows.length == 0){
			$.messager.show({msg:'没有导出的数据!',title:'提示'});
			return;
		}
		
		var  data = null;
		if(dataList.footer){
			data = dataList.rows.concat(dataList.footer);
		}else{
			data = dataList.rows;
		}
		
	    //2. 获取Datagrid的列
	    //var columns = $("#dg").datagrid("options").columns[0];
		var optsfreeze = $('#dg').datagrid('getColumnFields',true); //这是获取到所有的FIELD
		var opts = $('#dg').datagrid('getColumnFields');
		var optsAll = [];
		if(optsfreeze.length){
			if($.inArray('ck',optsfreeze) > -1){
				optsfreeze.splice($.inArray('ck',optsfreeze),1);
			}
			optsAll = optsfreeze.concat(opts);
		}else{
			if($.inArray('ck',opts) > -1){
				opts.splice($.inArray('ck',opts),1);
			}
			optsAll = opts;
		}
		
		var colArray = [];
		
		for(i=0;i<optsAll.length;i++){
			var col = $('#dg').datagrid( "getColumnOption" , optsAll[i] );
			if(!col.hidden){
				var  obj = {};
		    	obj.field = col.field;
		    	obj.title = col.title;
		    	obj.align = col.align;
		    	obj.halign = col.halign;
		    	colArray.push(obj);
			}
		}
	   
	    //3. 导出文件名
	    var title  = event.data.title;
	    var pojo  = event.data.pojo;
	    
	    $.post("saveListData.action",{dataArray:$.toJSON(data),colArray:$.toJSON(colArray)},function(){
	    	
	        window.location.href= "exportExcel.action?title="+title+"&pojo="+pojo;
	    });
	    
	}
	/** 切换车间的时候, 清空grid数据 */
	function clearDataGrid(newValue,oldValue){
		queryProductData();
	}
	

	/**
	 * 上下左右回车事件 
	 */
	function setEditCell(){
		$.extend($.fn.datagrid.methods, {
			editCell: function(jq,param){
				return jq.each(function(){
					
					var opts = $(this).datagrid('options');
					var fieldsAll = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
					
					var fields = [];//有编辑器的列
					for(var i=0; i<fieldsAll.length; i++){
						var col = $(this).datagrid('getColumnOption', fieldsAll[i]);
						if(col.editor){
							fields.push(fieldsAll[i]);
						}
					}
					
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor1 = col.editor;
						if (fields[i] != param.field){
							col.editor = null;
						}
					}
					
					$(this).datagrid('beginEdit', param.index);
					
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor = col.editor1;
					}
					
					var ed = $(this).datagrid('getEditor', param);
					
		            if (ed){
		                if ($(ed.target).hasClass('textbox-f')){
		                    $(ed.target).textbox('textbox').focus();
		                }else{
		                    $(ed.target).focus();
		                }
		            }
				   //begin新增功能
					var editors = $('#dg').datagrid('getEditors', param.index);
					var amountEditor = editors[0];
					var orginVal = $(amountEditor.target).numberbox('getValue');
					
					$(amountEditor.target).numberbox('setValue', null);
					
					//失去焦点
					$('.datagrid-editable .textbox,.datagrid-editable .datagrid-editable-input,.datagrid-editable .textbox-text').bind('blur',function () {
						var nums = $(amountEditor.target).numberbox('getValue');
						if(!nums){
							$(amountEditor.target).numberbox('setValue', orginVal);
						}
					});
					
					
					
					//begin回车事件处理,直接换行到指定单元格!也可以设定一个热键来响应数量单元格的数据!
					//amountEditor.target.on('keydown',
					$('.datagrid-editable .textbox,.datagrid-editable .datagrid-editable-input,.datagrid-editable .textbox-text').bind("keyup",function(e){
						if (e.keyCode == 39 ||e.keyCode==13) {//右光标
								endEdit();
								for(var i=0; i<fields.length; i++){
									if (fields[i] == param.field && fields[fields.length-1]!=param.field){
										$('#dg').datagrid('editCell', {index:param.index ,field:fields[i+1]});
									}
								}
								
								if(fields[fields.length-1] == param.field){
									var rows = $("#dg").datagrid("getRows");
									//判断最大的行号是否已经是最后一行
									if(rows.length >  param.index + 1){
										//编辑下一行
										$('#dg').datagrid('selectRow', param.index + 1).datagrid('editCell', {index:param.index + 1,field:fields[0]});
										$('#dg').datagrid('endEdit',param.index);
										editIndex = param.index+1;
									}else{
										//editIndex = 0;
									}
								}
								//返回到第一个单元格
								var row = $('#dg').datagrid('getSelected');
								var rowIndex = $("#dg").datagrid('getRowIndex', row);
								var rows = $("#dg").datagrid("getRows");
								
								/*
								if(rows.length-1==param.index&&fields[fields.length-1] == param.field){
									$('#dg').datagrid('selectRow',0).datagrid('editCell', {index:0 ,field:fields[0]});
									
								}*/
							}
							if (e.keyCode == 38) {//上光标
								endEdit();
								var selected = $('#dg').datagrid('getSelected');
								var index = $('#dg').datagrid('getRowIndex', selected);
			                    if (selected&&index!=0) {
			                        $('#dg').datagrid('selectRow', index - 1).datagrid('editCell', {index:param.index - 1,field:param.field});
			                        $('#dg').datagrid('endEdit',param.index);
			                        editIndex  = param.index-1;
			                    } 
//			                    else {
//			                        var rows = $('#dg').datagrid('getRows');
//			                        $('#dg').datagrid('selectRow', rows.length - 1).datagrid('editCell', {index:rows.length - 1,field:param.field});
//			                        $('#dg').datagrid('endEdit',param.index);
//			                        editIndex = rows.length - 1;
//			                    }		
							}
							if (e.keyCode == 37) {//左光标
									endEdit();
								    $('#dg').datagrid('endEdit', param.index);
									var row = $('#dg').datagrid('getSelected');
									var rowIndex = $("#dg").datagrid('getRowIndex', row);
									var rows = $("#dg").datagrid("getRows");
									
									for(var i=0; i<fields.length; i++){
										if (fields[i] == param.field&&fields[0]!=param.field){
											$('#dg').datagrid('editCell', {index:param.index ,field:fields[i-1]});
										}
									}
									if(fields[0] == param.field){	
						                 var rows = $("#dg").datagrid("getRows");
										//判断最大的行号是否已经是最后一行
										if(0 < param.index){
											//编辑下一行
											$('#dg').datagrid('selectRow', param.index - 1).datagrid('editCell', {index:param.index - 1,field:fields[fields.length-1]});
											$('#dg').datagrid('endEdit',param.index);
											editIndex = param.index-1;
										}else{
											//editIndex = rows.length-1;
										}
									}
								
								/* //返回到最后一个单元格
								if(0==param.index&&fields[0] == param.field){
									$('#dg').datagrid('selectRow',rows.length-1).datagrid('editCell', {index:rows.length-1,field:fields[fields.length-1]});
									$('#dg').datagrid('endEdit',param.index);
								}*/
							}
							//下光标
							if(e.keyCode == 40){
								endEdit();
								var selected = $('#dg').datagrid('getSelected');
								var index = $('#dg').datagrid('getRowIndex', selected);
								var rows = $('#dg').datagrid('getRows');
								
			                    if (selected&&index!=rows.length-1) {
			                        $('#dg').datagrid('selectRow', index + 1).datagrid('editCell', {index:param.index + 1,field:param.field});
			                        $('#dg').datagrid('endEdit',param.index);
			                        editIndex  = param.index+1;  
			                    } 
//			                    else {
//			                        $('#dg').datagrid('selectRow', rows.length-1 - index).datagrid('editCell', {index:rows.length -1- index,field:param.field});
//			                        $('#dg').datagrid('endEdit',param.index);
//			                        editIndex =0;
//			                    }	
							}
							//---end 
						});
				});
			}
		});
	}
    	
	setEditCell();
	
	//点击cell 		
	function selectSingleCell(index, field,value){
		    endEdit();
			$('#dg').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
	}		
	
	/** 车间加载成功 选中第一个 */
	function loadCombox(data){
		$("#shop").combobox('select',data[0].id);
	}
		
	