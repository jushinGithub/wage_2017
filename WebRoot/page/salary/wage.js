	var editIndex = undefined;
	
	//符号格式化
	var symblArray = [{value:1, text:'+'},{value:0, text:'-'}];
	function symFormatter(value, row){
		if(value == 1){
			return '+';
		}else {
			return '-';
		}
	}
	
	/** 结束编辑 */
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
	
	/** 双击编辑 */
	function ondbClickRow(index){
		if (editIndex != index){
			if (endEditing()){
				$('#dg').datagrid('selectRow', index)
						.datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#dg').datagrid('selectRow', editIndex);
			}
		}
	}
	
	/**保存 */
	function accept(){
		$('#dg').datagrid('endEdit', editIndex);
		var rows = $('#dg').datagrid('getChanges');
		
		//var params = 'emArray ='+$.toJSON(rows);
		var params = {emArray:$.toJSON(rows)};
		$.ajax({
			url:'saveWageAction.action',
			data:params,
			dataType:'json',
			success:function(r){
				if(r&&r == 'success'){
					$('#dg').datagrid('acceptChanges');
					$.messager.show({msg:'保存成功!',title:'成功'});
					
				}else{
					$('#dg').datagrid('rejectChanges');
					//$.messager.alert('错误',r.msg,'error');
				}
				editIndex=undefined;
				$('#dg').datagrid('unselectAll');
			}
		});
		
	}
	
	/** 撤销 */
	function reject(){
		$('#dg').datagrid('rejectChanges');
		editIndex = undefined;
	}
	
	/** 获得修改的数据 */
	function getChanges(){
		var rows = $('#dg').datagrid('getChanges');
		alert(rows.length+' rows are changed!');
	}