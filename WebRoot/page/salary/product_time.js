    $(function () {
	   //设置日期
    	//<!--  onClickCell:onClickCell  onClickRow: ondbClickRow --> 
	    setMonthDate();
	    //$("#printBtn").bind('click',{title: '工时分',rowNum:20},printData);  
	    $("#exportBtn").bind('click',{title: '工时分',pojo:'ProductTime'},exportExcel);
	    
	    queryProductTime();
    });
 
     var editIndex = undefined;
	
	/** 查询生产数据*/
	function queryProductTime(){
		var that = this;
		var date = $('#month').datebox('getValue');
		var orgId = $("#orgId").combotree("getValues");
		
		if(!date){
			$.messager.show({msg:"请选择月份",title:'提示'});
			return;
		}
		if(!orgId || orgId.length ==0){
			$.messager.show({msg:"请选择班组",title:'提示'});
			return;
		}
		
		$.ajax({
			url:'getProductTime.action',
			data:'month='+date+'&orgId='+orgId,
			cache:false,
			success:function(result){
				if(result && result.total > 0){
					$('#dg').datagrid('loadData',result);  
				}else{
					$('#dg').datagrid('loadData',{total:0,rows:[]});
					$.messager.show({msg:date+"月份没有 数据, 请初始化",title:'成功'});
				}
			}
		});
	}
	
	/** 保存 */
	function accept(){
		$.messager.progress({title:'等待',msg:'正在保存...',text:'处理中.......'});
		var that = this;
		endEdit();
		//获取修改的数据
		var rows = $('#dg').datagrid('getChanges');
		if(!rows || rows.length == 0){
			$.messager.progress('close');
			$.messager.show({msg:'没有修改的数据!',title:'提示'});
			return;
		}
		
		var params = {dataArray:$.toJSON(rows)};
		$.ajax({
			url:'saveOrUpdateProductTime.action',
			data:params,
			dataType:'json',
			type: "POST",
			success:function(r){
				$.messager.progress('close');
				
				if(r&&r == 'success'){
					$('#dg').datagrid('acceptChanges');
					that.queryProductTime();
					$.messager.show({msg:'保存成功!',title:'成功'});
					
					//回调更新工资金额
					$.ajax({
						url:'saveOrUpdateProductTimeCallback.action',
						data:params,dataType:'json',type: "POST"});
				}else{
					$('#dg').datagrid('rejectChanges');
				}
				editIndex=undefined;
				$('#dg').datagrid('unselectAll');
				
			}
		});
	}
	
	/** 重新导入员工 */
	function reLoadEm(){
		var that = this;
		var date = $('#month').datebox('getValue');
		var orgId = $("#orgId").combotree("getValues");
		
		if(!date){
			$.messager.show({msg:"请选择月份",title:'提示'});
			return;
		}
		if(!orgId || orgId.length ==0){
			$.messager.show({msg:"请选择班组",title:'提示'});
			return;
		}
		
		$.ajax({
			url:'getSalary.action',
			data:'month='+date+'&isPiece=√&orgId='+orgId,
			success:function(result){
				if(result && result.total > 0){
					jQuery.messager.confirm('提示:','本月有员工的工时分数据, 你是否要重新获取?',function(event){   
						if(event){   
							$.ajax({
								url:'reLoadTime.action',
								data:'month='+date+'&orgId='+orgId,
								success:function(r){
									if(r&&r == 'success'){
										that.queryProductTime();
										that.accept();
										
									}
								}
							});
						}
					});	
				}else{
					$.ajax({
						url:'reLoadTime.action',
						data:'month='+date+'&orgId='+orgId,
						success:function(r){
							if(r&&r == 'success'){
								that.queryProductTime();
								that.accept();
								
							}
						}
					});
				}
			}
		});
	}
	
	/** 删除工时分 */
	function  delTime(){
		var that = this;
		var rows = $('#dg').datagrid('getChecked'); 
		if(!rows || rows.length == 0){
			 $.messager.alert("提示", "请选择删除的员工！",'warning');  
			 return;
		}
		
		var params = {dataArray:$.toJSON(rows)};
		jQuery.messager.confirm('提示:','你确认要删除吗?',function(event){   
			if(event){   
				if (editIndex == undefined){
					$.ajax({
						url:'delTime.action',
						data:params,
						dataType:'json',
						success:function(r){
							if(r&&r == 'success'){
								that.queryProductTime();
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
	
	/** 导出 */
	function exportData(){
		
	}