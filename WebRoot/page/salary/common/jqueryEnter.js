
/**
 * 上下左右回车事件 
 */
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
			
			$('.datagrid-editable .textbox,.datagrid-editable .datagrid-editable-input,.datagrid-editable .textbox-text').bind('blur',function () {
				var nums = $(amountEditor.target).numberbox('getValue');
				if(!nums){
					$(amountEditor.target).numberbox('setValue', orginVal);
				}
			});
			
			//begin回车事件处理,直接换行到指定单元格!也可以设定一个热键来响应数量单元格的数据!
			//amountEditor.target.on('keydown',
			
			$('.datagrid-editable .textbox,.datagrid-editable .datagrid-editable-input,.datagrid-editable .textbox-text').bind("keyup",function(e){
					if (e.keyCode == 39 ||e.keyCode==13 ) {//右光标
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
									editIndex = 0;
								}
								
							}
						//返回到第一个单元格
						var row = $('#dg').datagrid('getSelected');
						var rowIndex = $("#dg").datagrid('getRowIndex', row);
						var rows = $("#dg").datagrid("getRows");
						
						if(rows.length-1==param.index&&fields[fields.length-1] == param.field){
							$('#dg').datagrid('selectRow',0).datagrid('editCell', {index:0 ,field:fields[0]});
							
						}
					}
					if (e.keyCode == 38) {//上光标
						var selected = $('#dg').datagrid('getSelected');
						var index = $('#dg').datagrid('getRowIndex', selected);
	                    if (selected&&index!=0) {
	                        $('#dg').datagrid('selectRow', index - 1).datagrid('editCell', {index:param.index - 1,field:param.field});
	                        $('#dg').datagrid('endEdit',param.index);
	                        editIndex  = param.index-1;
	                    } else {
	                        var rows = $('#dg').datagrid('getRows');
	                        $('#dg').datagrid('selectRow', rows.length - 1).datagrid('editCell', {index:rows.length - 1,field:param.field});
	                        $('#dg').datagrid('endEdit',param.index);
	                        editIndex = rows.length - 1;
	                    }		
					}
					if (e.keyCode == 37) {//左光标
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
									editIndex = rows.length-1;
								}
							}
							//返回到最后一个单元格
						if(0==param.index&&fields[0] == param.field){
							$('#dg').datagrid('selectRow',rows.length-1).datagrid('editCell', {index:rows.length-1,field:fields[fields.length-1]});
							$('#dg').datagrid('endEdit',param.index);
						}
					}
					//下光标
					if(e.keyCode == 40){
						var selected = $('#dg').datagrid('getSelected');
						var index = $('#dg').datagrid('getRowIndex', selected);
						var rows = $('#dg').datagrid('getRows');
	                    if (selected&&index!=rows.length-1) {
	                        $('#dg').datagrid('selectRow', index + 1).datagrid('editCell', {index:param.index + 1,field:param.field});
	                        $('#dg').datagrid('endEdit',param.index);
	                        editIndex  = param.index+1;  
	                    } else {
	                        $('#dg').datagrid('selectRow', rows.length-1 - index).datagrid('editCell', {index:rows.length -1- index,field:param.field});
	                        $('#dg').datagrid('endEdit',param.index);
	                        editIndex =0;
	                    }	
					}
					//---end 
				});
		});
	}
});
		
//点击cell 		
function onClickCell(index, field,value){
	    endEdit();
		$('#dg').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
}		
	