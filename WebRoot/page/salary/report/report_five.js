 	$(function () {
	   //设置日期
	    setMonthDate();
	    $("#printBtn").bind('click',{title: '计件工资测算表',rowNum:35},printData);
	    queryReportFive();
	    
    });
	
	/** 查询数据*/
	function queryReportFive(){
		var that = this;
		var date = $('#month').datebox('getValue');
		var orgId = $("#orgId").combotree("getValue");
		var orgName = $("#orgId").combotree("getText");
		
		if(!date){
			$.messager.show({msg:"请选择月份",title:'提示'});
			return;
		}
		if(!orgId){
			$.messager.show({msg:"请选择班组",title:'提示'});
			return;
		}
      		
		$.ajax({
			url:'getReportFive.action',
			data:'orgId='+orgId+'&orgName='+orgName+'&month='+date+'&isPiece=√',
			cache:false,
			success:function(result){
				if(result && result.total > 0){
					$('#dg').datagrid('loadData',result);  
				}else{
					$('#dg').datagrid('loadData',{total:0,rows:[]});
					$.messager.show({msg:date+"月份没有 数据",title:'成功'});
				}
			}
		});
	}
	