 	$(function () {
	   //设置日期
	    setMonthDate();
	    $("#printBtn").bind('click',{title: '月工资分析表',rowNum:20},printData);
	    $("#exportBtn").bind('click',{title: '月工资分析表',pojo:'MonthAnalyze'},exportExcel);
	    
    });
 	
 	/** 车间加载成功 选中第一个 */
	function loadCombox(data){
		$("#shop ").combobox('select',data[0].id);
	}
	
	/** 查询数据*/
	function queryReportFive(){
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
			url:'getMonthAnalyze.action',
			data:'month='+date+'&shop='+shop,
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
	
	/** 小计行 */ 
	function rowStyleFun(index,row){
		if(row.orgName && row.orgName.indexOf("小计")>0){
			return 'background-color:#6293BB;color:#fff;'; 
		}
	}
	