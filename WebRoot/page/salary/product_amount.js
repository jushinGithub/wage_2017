   $(function () {
	   //设置日期
	    setMonthDate();
	    
	    $("#printBtn").bind('click',{title: '生产数量调整',rowNum:20},printData);
	    $("#exportBtn").bind('click',{title: '生产数量调整',pojo:'ProductData'},exportExcel);
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
			url:'getProductQtData.action',
			data:'month='+date+'&shop='+shop,
			cache:false,
			success:function(result){
				if(result && result.total > 0){
					$('#dg').datagrid('loadData',result);  
				}else{
					$('#dg').datagrid('loadData',{total:0,rows:[]});
					$.messager.show({msg:date+"月份没有数据, 请初始化",title:'成功'});
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
			$.messager.show({msg:'没有修改的数据!',title:'提示'});
			$.messager.progress('close');
			return;
		}
		
		var params = {dataArray:$.toJSON(rows)};
		$.ajax({
			url:'saveOrUpdateProductDataQt.action',
			data:params,
			dataType:'json',
			type: "POST",
			success:function(r){
				$.messager.progress('close');
				
				if(r&&r == 'success'){
					$('#dg').datagrid('acceptChanges');
					that.queryProductPrice();
					$.messager.show({msg:'保存成功!',title:'成功'});
					
					//回调计算
					$.ajax({
						url:'saveOrUpdateProductDataQtCallback.action',
						data:params,dataType:'json',type: "POST"});
				}else{
					$('#dg').datagrid('rejectChanges');
				}
				editIndex=undefined;
				$('#dg').datagrid('unselectAll');
			}
		});
	}
	
	