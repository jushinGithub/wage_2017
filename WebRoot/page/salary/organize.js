	
	$(function(){
		var saveDecimal = getConfig();
		var colums = [[
						{field:'ck',checkbox:true},
						{field:'groupNo',   title:'组号',    width:60,halign:'center',  align:'left'},
						{field:'emNo',      title:'工号',    width:100,halign:'center', align:'left',   editor:'textbox'},
						{field:'emName',    title:'姓名',    width:100,halign:'center', align:'left',   editor:'textbox'},
				        {field:'isPiece',   title:'是否计件',  width:60, halign:'center', align:'center', editor:{type:'checkbox', options:{on:'√',off:''}}},
				        {field:'baseSalary',title:'基本工资',  width:100,halign:'center', align:'center', editor:{type:'numberbox',options:{precision:2}}},
				        {field:'grade',     title:'分值',     width:100,halign:'center', align:'center', editor:{type:'numberbox',options:{precision:2}}},
	                    {field:'emState',   title:'状态',     width:60, halign:'center', align:'center', formatter:stateFormatter},
				        {field:'job',       title:'岗位',     width:100,halign:'center', align:'left',   editor:'textbox'},
				        {field:'uuid',      title:'主键',     hidden:true},
				        {field:'orgId',     title:'班组主键',   hidden:true}
                     ]];
	        
        $('#dg').datagrid({
		    columns: colums,
		    iconCls: 'icon-edit',
			singleSelect: false,
			rownumbers: true,
			showFooter:true,
			toolbar: '#tb',
			onClickRow: ondbClickRow
		});
	});
	
	var stateArray = [{value:1, text:'在职'},
	                  {value:0, text:'离职'}
	                 ];
	function stateFormatter(value, row){
		if(value == 1){
			return '在职';
		}else if(value == 0){
			return '离职';
		}
	}
	
	var editIndex = undefined;   //编辑的行
	
	/** 点击班组获取员工信息 */
	function onTreeClick(treeNode){
		reject();
		var id = treeNode.id;
		var containAll =  $('#containAll').switchbutton("options").checked;
		
		$("#dg").datagrid({url:'getEmployeeByOrgId.action',queryParams:{ "orgId":id,"containAll":containAll}});
	}
	
	/**新增*/
	function append(){
		//先判断有没有选中 班组
		var treeNode = $('#orgTree').tree('getSelected');
		if(!treeNode){
			$.messager.alert("提示", "请先选择班组！",'warning');  
			return;
		}
		if (endEditing()){
			if(treeNode.id.indexOf('2') != 0){//计时员工
				$('#dg').datagrid('appendRow',{baseSalary:0, emState:1});
			}else{
				$('#dg').datagrid('appendRow',{isPiece:'√', emState:1});
			}
			editIndex = $('#dg').datagrid('getRows').length-1;
			$('#dg').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
		}
	}
	
	/** 删除 */
	function removeit(){
		var rows = $('#dg').datagrid('getChecked'); 
		if(!rows || rows.length == 0){
			 $.messager.alert("提示", "请选择要离职的员工！",'warning');  
			 return;
		}
		
		var params = {emArray:$.toJSON(rows)};
		jQuery.messager.confirm('提示:','确认要设置该员工离职吗?',function(event){   
			if(event){   
				if (editIndex == undefined){
					$.ajax({
						url:'removeEmployee.action',
						data:params,
						dataType:'json',
						success:function(r){
							if(r&&r == 'success'){
								//var orgId = $('#orgTree').tree('getSelected').id;
								//$('#dg').datagrid('load', {orgId: orgId});
								var node = $('#orgTree').tree('getSelected');
								onTreeClick(node);
								
								$.messager.show({msg:'设置离职成功!',title:'成功'});
							}else{
								$('#dg').datagrid('rejectChanges');
							}
							editIndex=undefined;
							$('#dg').datagrid('unselectAll');
						}
					});
				}else{
					$('#dg').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
					editIndex = undefined; 
				}
			}
		});	
	}
	
	/** 删除历史数据, 物理删除 */
	function removeHistory(){
		var rows = $('#dg').datagrid('getChecked'); 
		if(!rows || rows.length == 0){
			 $.messager.alert("提示", "请选择删除的员工！",'warning');  
			 return;
		}
		
		var params = {emArray:$.toJSON(rows)};
		jQuery.messager.confirm('提示:','你确认要删除员工吗?',function(event){   
			if(event){   
				if (editIndex == undefined){
					$.ajax({
						url:'removeHistoryEmployee.action',
						data:params,
						dataType:'json',
						success:function(r){
							if(r&&r == 'success'){
								var orgId = $('#orgTree').tree('getSelected').id;
								$('#dg').datagrid('load', {orgId: orgId});
								$.messager.show({msg:'删除成功!',title:'成功'});
							}else{
								$('#dg').datagrid('rejectChanges');
							}
							editIndex=undefined;
							$('#dg').datagrid('unselectAll');
						}
					});
				}else{
					$('#dg').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
					editIndex = undefined; 
				}
			}
		});	
	}
	/** 保存 */
	function accept(){
		var that = this;
		endEdit(); //结束编辑
		//获取修改的数据
		//$('#dg').datagrid('endEdit', editIndex);
		
		var orgId = $('#orgTree').tree('getSelected').id;
		
		var rows = $('#dg').datagrid('getChanges');
		if(rows && rows.length > 0){
			for(var i=0; i<rows.length; i++){
				rows[i].orgId = orgId;
				
				if(rows[i].hiredate){
					var date = rows[i].hiredate;
					var p = date.split('/');
					rows[i].hiredate = p[2]+"-"+p[0]+"-"+p[1];
				}
			}
		}else{
			$.messager.show({msg:'没有修改的数据!',title:'提示'});
			return;
		}
		
		var params = {emArray:$.toJSON(rows)};
		$.ajax({
			url:'saveOrUpdateEmployee.action',
			data:params,
			dataType:'json',
			type: "POST",
			success:function(r){
				if(r&&r == 'success'){
					$('#dg').datagrid('acceptChanges');
					//重新查询
					var node = $('#orgTree').tree('getSelected');
					that.onTreeClick(node);
					$.messager.show({msg:'保存成功!',title:'成功'});
					
				}else{
					$('#dg').datagrid('rejectChanges');
				}
				editIndex=undefined;
				$('#dg').datagrid('unselectAll');
			}
		});
	}
	
	/** 在职离职员工查询 */
	function switchChange(checked){
		//重新查询
		var node = $('#orgTree').tree('getSelected');
		onTreeClick(node);
	}
	
	/** 增加行政部门 */
	function addOrg(){
		//判断是否选中部门, 选中就是修改名称
		var treeNode = $('#orgTree').tree('getSelected');
		var orgId = '';
		if(treeNode && treeNode.id.indexOf('2') != 0){
			orgId = treeNode.id;
		}
		
		var name=$.messager.prompt("请输入部门名称","",function(orgName){
			$.ajax({
				url:'addTimeOrg.action?orgName='+orgName+'&orgId='+orgId,
				dataType:'json',
				type: "POST",
				success:function(r){
					if(r&&r == 'success'){
						$.messager.show({msg:'新增成功,请刷新页面!',title:'成功'});
					}
				}
			});
			$.messager.show({msg:'新增|修改 成功,请刷新页面!',title:'成功'});
		});
		
		
	}
