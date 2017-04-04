 	$(function () {
	   //设置日期
	    setMonthDate();
	    $("#printBtn").bind('click',{title: '工价表',rowNum:35},printData);
	    $("#exportBtn").bind('click',{title: '工价表',pojo:'ProductPrice'},exportExcel);
	    
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
	
	/** 合计工价 */
	function formatterGj(value,row,index){
		return row.cjgj+row.cggj+row.sggj+row.tggj
	}
	