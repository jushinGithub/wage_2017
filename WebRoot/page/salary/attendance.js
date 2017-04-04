    $(function () {
	   //设置日期
    	//<!--  onClickCell:onClickCell  onClickRow: ondbClickRow --> 
	    setMonthDate();
	    $("#printBtn").bind('click', {title: '考勤表',rowNum:20},printData);  
	    $("#exportBtn").bind('click',{title: '考勤表',pojo:'Attendance'},exportExcel);
	    
	    queryAttendace();
    });
 
     var editIndex = undefined;
	
	/** 查询生产数据*/
	function queryAttendace(){
		var that = this;
		var date = $('#month').datebox('getValue');
		var orgId = $("#orgId").combotree("getValues");
		
//		if(!date){
//			$.messager.show({msg:"请选择月份",title:'提示'});
//			return;
//		}
		
		if(!orgId || orgId.length ==0){
			$.messager.show({msg:"请选择班组",title:'提示'});
			return;
		}
		
		$.ajax({
			url:'getAttendace.action',
			data:'month='+date+'&orgId='+orgId,
			cache:false,
			success:function(result){
				if(result && result.total > 0){
					$('#dg').datagrid('loadData',result);  
				}else{
					$('#dg').datagrid('loadData',{total:0,rows:[]});
					//$.messager.show({msg:"没有员工!",title:'成功'});
				}
			}
		});
	}
	