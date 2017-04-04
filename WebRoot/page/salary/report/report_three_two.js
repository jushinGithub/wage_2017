   $(function () {
	   //设置日期
	    setMonthDate();
	    
	    
	    //组合表头
		var compHeader = '<tr><th rowspan="2">货号</th><th rowspan="2" >品名</th><th colspan="3" >裁剪</th><th colspan="3" >手工</th><th colspan="3" >烫工</th></tr>';
	    compHeader +='<tr><td>工价</td><td>数量</td><td>金额</td><td>工价</td><td>数量</td><td>金额</td><td>工价</td><td>数量</td><td>金额</td></tr>';
			
		$("#printBtn").bind('click',{title: '各工段工资测算表',compHeader:compHeader,rowNum:20},printData);
	    
    }); 
   
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
