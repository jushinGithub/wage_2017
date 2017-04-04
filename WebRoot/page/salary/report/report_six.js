   $(function () {
	   //设置日期
	    setMonthDate();
	    $("#printBtn").bind('click',{title: '人员计件工资明细表',rowNum:20},printData);
	    $("#exportBtn").bind('click',{title: '人员计件工资明细表',pojo:'MonthSalary'},exportExcel);
	    
	    queryProductTime();
	    
    });
	
	/** 查询数据*/
	function queryProductTime(){
		var that = this;
		var date = $('#month').datebox('getValue');
		var orgId = $("#orgId").combotree("getValue");
		
		if(!date){
			$.messager.show({msg:"请选择月份",title:'提示'});
			return;
		}
		if(!orgId){
			$.messager.show({msg:"请选择班组",title:'提示'});
			return;
		}
      		
		$.ajax({
			url:'getSalary.action',
			data:'orgId='+orgId+'&month='+date+'&isPiece=√',
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
	