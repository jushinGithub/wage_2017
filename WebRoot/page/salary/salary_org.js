    $(function () {
	   //设置日期
	    setMonthDate();
	    
	    $("#printBtn").bind('click', {title: '各项扣款',rowNum:20},printData);
	    $("#exportBtn").bind('click',{title: '各项扣款',pojo:'OrgSalary'},exportExcel);
	    
	    
    });
 
 	var editIndex = undefined;
 	
    /** 车间加载成功 选中第一个 */
	function loadCombox(data){
		$("#shop ").combobox('select',data[0].id);
		queryProductTime();
	}
	
	/** 查询工段工资*/
	function queryProductTime(){
		var that = this;
		var date = $('#month').datebox('getValue');
		var shop = $('#shop').datebox('getValue');
		
		if(!shop){
			$.messager.show({msg:"请选择车间",title:'提示'});
			return;
		}else{
			shop = parseInt(shop);
		}
		if(!date){
			$.messager.show({msg:"请选择月份",title:'提示'});
			return;
		}
		
		$.ajax({
			url:'getOrgSalary.action',
			data:'month='+date+'&shop='+shop,
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
		//var params = 'dataArray ='+$.toJSON(rows);
		
		var params = {dataArray:$.toJSON(rows)};
		$.ajax({
			url:'saveOrUpdateOrgSalary.action',
			data:params,
			dataType:'json',
			type: "POST",
			success:function(r){
				$.messager.progress('close');
				
				if(r&&r == 'success'){
					$('#dg').datagrid('acceptChanges');
					that.queryProductTime();
					$.messager.show({msg:'保存成功!',title:'成功'});
					
					//callback
					$.ajax({
						url:'saveOrUpdateOrgSalaryCallback.action',
						data:params,dataType:'json',type: "POST"});
				}else{
					$('#dg').datagrid('rejectChanges');
				}
				editIndex=undefined;
				$('#dg').datagrid('unselectAll');
				
			}
		});
	}
	
	