 $(function () {
	   //设置日期
	    setMonthDate();
	    
	    $("#printBtn").bind('click',{title: '输入工价',rowNum:35},printData);
	    $("#exportBtn").bind('click',{title:'输入工价',pojo:'ProductPrice'},exportExcel);
    });  
    var editIndex = undefined;
	
    /** 车间加载成功 选中第一个 */
	function loadCombox(data){
		$("#shop ").combobox('select',data[0].id);
	}
	
	/** 查询 工价*/
	function queryProductPrice(){
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
			url:'getProductPrice.action',
			data:'month='+date+'&shop='+shop,
			cache:false,
			success:function(result){
				if(result && result.total > 0){
					$('#dg').datagrid('loadData',result);  
				}else{
					$('#dg').datagrid('loadData',{total:0,rows:[]});
					$.messager.show({msg:date+"月份没有工价数据, 请初始化",title:'成功'});
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
			url:'saveOrUpdateProductPrice.action',
			data:params,
			dataType:'json',
			type: "POST",
			success:function(r){
				$.messager.progress('close');
				
				if(r&&r == 'success'){
					$('#dg').datagrid('acceptChanges');
					that.queryProductPrice();
					$.messager.show({msg:'保存成功!',title:'成功'});
					
					//回调更新工资金额
					$.ajax({
						url:'saveOrUpdateProductPriceCallback.action',
						data:params,dataType:'json',type: "POST"});
				}else{
					$('#dg').datagrid('rejectChanges');
				}
				editIndex=undefined;
				$('#dg').datagrid('unselectAll');
				
			}
		});
	}
	
	/**使用上一个月的工价和工艺价*/
	function lastProductPrice(){
		var that = this;
		var date = $('#month').datebox('getValue');
		var shop = $('#shop').datebox('getValue');
		
		if(!shop){
			$.messager.show({msg:"请选择车间",title:'提示'});
			return;
		}
		if(!date){
			$.messager.show({msg:"请选择月份",title:'提示'});
			return;
		}
		
		$.ajax({
			url:'userLastMonthPrice.action',
			data:'month='+date+'&shop='+shop,
			dataType:'json',
			success:function(r){
				if(r&&r == 'success'){
					that.queryProductPrice();
					$.messager.show({msg:'设置成功!',title:'成功'});
				}
			}
		});
	}
	