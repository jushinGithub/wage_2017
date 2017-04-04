
  $(function(){
	 //设置日期
	 setMonthDate();
	 $("#exportBtn").bind('click', {title:'各班组计件工资表',pojo:'MonthSalary' },exportExcel);
	 
	  var compHeader = "";
	  var columnsPiece = [];
	  var firstPart = [
	                    {field:'uuid',     title:'主键',  hidden:true, rowspan:2},
				        {field:'emId',     title:'员工主键',hidden:true, rowspan:2},
				        
				        {field:'shop',     hidden:true, rowspan:2},
				        {field:'orgId',    hidden:true, rowspan:2},
				        {field:'orgNo',    hidden:true, rowspan:2},
				        {field:'groupNo',  hidden:true, rowspan:2},
				        {field:'month',    hidden:true, rowspan:2},
				        {field:'isPiece',  hidden:true, rowspan:2},
				        {field:'piecePrice',hidden:true, rowspan:2},  
				        
				        {title:"津贴",      colspan:0}, 
				        {field:'totalPay', title:'应发工资', width:80,halign:'center', align:'right', rowspan:2},
				        {title:"扣款",      colspan:0},
				        {field:'finalPay', title:'实发工资', width:80,halign:'center', align:'right', rowspan:2},
				        {field:'sign',     title:'签名',   width:120,halign:'center', align:'right', rowspan:2}
                       ];
	  	
	  $.ajax({
			url:'initWage.action',
			data:"isPiece=√",
			dataType:'json',
			async: false,
			success:function(data){
				var objArr = data.rows;
				var secondPart = [];
				var add = 0;
				var sub = 0;
				$(objArr).each(function(index,el){
					secondPart.push({field:el['wageNo'].toLowerCase(),title:el['wageName'],width:60,halign:'center',align:'right',editor:{type:'numberbox',options:{precision:0}}});
					if(el['symbol'] == 1){
						add += 1;
					}else if(el['symbol'] == 0){
						sub += 1;
					}
			     });
				firstPart[9].colspan = add;
				firstPart[11].colspan = sub;
			    
				columnsPiece.push(firstPart);
				columnsPiece.push(secondPart);
				  
			  $('#dg').datagrid({
				    columns: columnsPiece,
				    iconCls: 'icon-edit',
					singleSelect: true,
					rownumbers: true,
					showFooter:true,
					toolbar: '#tb',
					frozenColumns:[[{field:'orgName',  title:'组号', hidden:true},
					                {field:'emNo',     title:'工号', width:60,halign:'center', align:'left'},
							        {field:'emName',   title:'姓名', width:60,halign:'center', align:'left'},
							        {field:'time',     title:'工时', width:60,halign:'center', align:'center'},
							        {field:'workDay',  title:'出勤<br>天数',   width:40, halign:'center', align:'right', editor:{type:'numberbox',options:{precision:1}}},
							        
							        {field:'grade',    title:'分值', width:60,halign:'center', align:'center'},
							        {field:'sumGrade', title:'总分值',width:60,halign:'center', align:'center'},
							        {field:'piecePay', title:'计件工资',width:80,halign:'center', align:'right'}
					              ]],
				   onClickCell:selectSingleCell
				});
			  
			  	//组合表头
				compHeader = '<tr><th rowspan="2">工号</th><th rowspan="2">姓名</th><th rowspan="2">工时</th><th rowspan="2" >分值</th><th rowspan="2" >总分值</th><th rowspan="2" >计件工资</th>'
				for(var x=0; x<firstPart.length; x++){
					if(!firstPart[x].hidden){
						var colspan = firstPart[x].colspan?firstPart[x].colspan:1;
						var rowspan = firstPart[x].rowspan?firstPart[x].rowspan:1;
						var width = firstPart[x].width
						compHeader += '<td colspan='+colspan+' rowspan='+rowspan+'  width='+width+' >'+firstPart[x].title+'</td>'
					}
				}
				compHeader += '</tr><tr>';
				
				for(var y=0; y<secondPart.length; y++){
					if(!secondPart[y].hidden){
						var colspan = secondPart[y].colspan?secondPart[y].colspan:1;
						var rowspan = secondPart[y].rowspan?secondPart[y].rowspan:1;
						compHeader += '<td colspan='+colspan+' rowspan='+rowspan+'>'+secondPart[y].title+'</td>'
					}
				}
				compHeader += '</tr>';
				
				$("#printBtn").bind('click',{title: '各班组计件工资表',compHeader:compHeader,rowNum:21},printData);
				//打印工资条
				//$("#printBtnList").bind('click',{title: '各班组计件工资条',compHeader:compHeader,isAll:true,isPiece:'√',rowNum:8},printData);
			}
		});
	  
	  //查询数据
	  querySalary();
  });

   var editIndex = undefined;
  
   /** 计算本月的计件工资 */
   function calculateSalary(){
	   var that = this;
		var date = $('#month').datebox('getValue');
		if(!date){
			$.messager.show({msg:"请选择月份",title:'提示'});
			return;
		}
		
		$.ajax({
			url:'calculateSalary.action',
			data:'month='+date,
			success:function(r){
				if(r&&r == 'success'){
					that.querySalary();
					$.messager.show({msg:'计算成功!',title:'成功'});
				}
			}
		});
   }
	
	/** 查询人员工资*/
	function querySalary(){
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
			cache:false,
			success:function(result){
				if(result && result.total > 0){
					$('#dg').datagrid('loadData',result);  
				}else{
					$('#dg').datagrid('loadData',{total:0,rows:[]});
					$.messager.show({msg:date+"月份没有工资数据, 请初始化",title:'成功'});
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
		
		//var params = 'dataArray ='+$.toJSON(rows)+"&isPiece=√";
		var params = {dataArray:$.toJSON(rows),isPiece:'√'};
		$.ajax({
			url:'saveOrUpdateSalary.action',
			data:params,
			dataType:'json',
			type: "POST",
			success:function(r){
				$.messager.progress('close');
				
				if(r&&r == 'success'){
					$('#dg').datagrid('acceptChanges');
					that.querySalary();
					$.messager.show({msg:'保存成功!',title:'成功'});
				}else{
					$('#dg').datagrid('rejectChanges');
				}
				editIndex=undefined;
				$('#dg').datagrid('unselectAll');
				
			}
		});
	}
	
