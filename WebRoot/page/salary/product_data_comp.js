 $(function () {
	 	//设置日期
	    setMonthDate();
	    //绑定 车间修改事件
	    //$("#shop").bind('onChange',setGroupHeader);
        
    });  
 
 	/** 车间加载成功 选中第一个 */
	function loadCombox(data){
		$("#shop ").combobox('select',data[0].id);
		var shop = parseInt($('#shop').combobox('getValue'));
		var date = $('#month').datebox('getValue');
		
		//设置表头
	    //setGroupHeader(shop, date);
	}
	
 	/** 设置表头 */
 	function setGroupHeader(shop, date){
 		//1.设置动态表头, 获取所有的组
        var compHeader = "";
        $.ajax({
			url:'queryGroupName.action',
			data:'shop='+shop +"&date="+date,
			success:function(groupName){
				if(groupName.length > 0){ 
					 var columnsPiece = [];
					//2.设置cols, 第一行和第二行
			        var firstPart = [{field:'emId',    title:'货物主键',hidden:true, rowspan:2}];
			        var secondPart = [];
			        for(var i=0; i<groupName.length; i++){
			        	firstPart.push({title:groupName[i], colspan:1});
			        	
			        	//secondPart.push({field:'fetch'+groupName[i],title:'领用数',hidden:true,width:50,halign:'center',align:'right',editor:{type:'numberbox'}});
			        	secondPart.push({field:'comp'+groupName[i], title:'完工数',width:50,halign:'center',align:'right',editor:{type:'numberbox'}});
			        }
			        
			        //firstPart.push({field:'sumSlFetch',    title:'领用数合计',hidden:true, halign:'center',align:'right', rowspan:2});
			        firstPart.push({field:'sumSlComp',     title:'完工数合计', halign:'center',align:'right', rowspan:2});
			        
			        columnsPiece.push(firstPart);
					columnsPiece.push(secondPart);
			        
					
			        //3.初始化Grid
					 $('#dg').datagrid({
						    columns: columnsPiece,
							singleSelect: true,
							rownumbers: true,
							showFooter:true,
							toolbar: '#tb',
							frozenColumns:[[{field:'itemNo',   title:'货号',   width:100,halign:'center', align:'left'},
									       {field:'itemName',  title:'品名',   width:100,halign:'center', align:'left'}
							              ]]
						});
					 //组合表头
					 compHeader = '<tr><th rowspan="2">货号</th><th rowspan="2" >品名</th>'
						 for(var x=0; x<firstPart.length; x++){
							 if(!firstPart[x].hidden){
								 var colspan = firstPart[x].colspan?firstPart[x].colspan:1;
								 var rowspan = firstPart[x].rowspan?firstPart[x].rowspan:1;
								 compHeader += '<td colspan='+colspan+' rowspan='+rowspan+'>'+firstPart[x].title+'</td>'
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
					 
					 $("#printBtn").bind('click',{title: '车工生产数据',compHeader:compHeader,rowNum:20},printData);
					 
					//重新查询数据
				    //queryProductData();
				}else{
					//$.messager.show({msg:"本月车间没有生产数据!",title:'提示'});
					return;
				}
			}
		});
 		
 	}
	
 	
 	
	/** 查询生产数据*/
	function queryProductData(){
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
		
		//设置表头
	    setGroupHeader(shop, date);
	    setEditCell();
	    
		$.ajax({
			url:'getProductData.action',
			data:'month='+date+'&shop='+shop,
			cache:false,
			success:function(result){
				if(result && result.total > 0){
					$('#dg').datagrid('loadData',result);  
				}else{
					$('#dg').datagrid('loadData',{total:0,rows:[]});
					$.messager.show({msg:date+"月份没有生产数据, 请初始化",title:'成功'});
				}
			}
		});
	}
	
	/** 重新获取 生产数据 */
	function reloadProductData(){
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
		
		$.messager.progress({title:'等待',msg:'正在加载数据...',text:'处理中.......'});
		
		var dataCgArray = [];  //车工数据
		var dataQtArray = [];  //其他工段数据
		$.ajax({
			async: false,
			url:'http://'+window.location.host+'/cloth_erp/cgyb.action?repMonth='
				+date.replace('-','')+'&cj='+shop+'&fromWage=true&bz=&_search=false&nd=1461500648487&rows=&page=1&sidx=groupNo+asc%2C+upNum&sord=desc',
			success:function(result){
				if(result && result.rows && result.rows.length > 0){
					dataCgArray = result.rows;
				}
			}
		});
		
		$.ajax({
			async: false,
			url:'http://'+window.location.host+'/cloth_erp/scjdyb.action?repMonth='
				+date.replace('-','')+'&cj='+shop+'&fromWage=true&_search=false&nd=1461468149485&rows=&page=1&sidx=upNum&sord=desc',
			success:function(result){
				if(result && result.rows && result.rows.length > 0){
					dataQtArray = result.rows;
				}
			}
		});
		
		shop  = parseInt($('#shop').datebox('getValue'));
		if(dataCgArray.length > 0 || dataQtArray.length > 0){
			//获取之前, 需要判断本月是否有, 有则需要提示, 并且覆盖
			var params = {dataCgArray:$.toJSON(dataCgArray),dataQtArray:$.toJSON(dataQtArray)};
			$.ajax({
				url:'getProductData.action',
				data:'month='+date+'&shop='+shop,
				success:function(result){
					if(result && result.total > 0){
						$.messager.show({msg:"本月已有生产数据, 请查询!",title:'提示'});
						$.messager.progress('close');
					}else{
						$.ajax({
							url:'reloadProductData.action?month='+date+'&shop='+shop,
							data:params,
							dataType:'json',
							type: "POST",
							success:function(result){
								if(result){
									$.messager.progress('close');
									
									that.queryProductData();
									$.messager.show({msg:"导入成功, 请查询",title:'成功'});
									//查询数据
									//queryProductData();
									$.ajax({
										url:'reloadProductDataCallback.action?month='+date+'&shop='+shop,
										data:params,dataType:'json',type: "POST",});
								}
							}
						});
					}
				}
			}); 
		}else{
			$.messager.progress('close');
			$.messager.show({msg:'本月没有数据!请确认生产数据是否结转',title:'提示'});
			return;
		}
	}
	
	/** 保存 */
	function accept(){
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
		
		$.messager.progress({title:'等待',msg:'正在保存...',text:'处理中.......'});
		endEdit();
		//获取修改的数据
		var rows = $('#dg').datagrid('getChanges');
		if(!rows || rows.length == 0){
			$.messager.progress('close');
			$.messager.show({msg:'没有修改的数据!',title:'提示'});
			return;
		}
		
		var shop = parseInt($('#shop').datebox('getValue'));
		//需要做一次数据转换   总数应该是组号记录*2   fetch车工01组
		var records = [];
		for(var x=0; x<rows.length; x++){
			for(var y in rows[x]){
				if(y.indexOf('comp') > -1 &&　y.indexOf('complete')== -1　){
					var obj = new Object();
					obj.itemId= rows[x].itemId;
					obj.itemNo= rows[x].itemNo;
					obj.itemName= rows[x].itemName;
					obj.month= rows[x].month;
					obj.shop= shop;
					obj.orgName = y.substring(4);
					obj.completeNums = rows[x][y];
					obj.fetchNums = rows[x]['fetch'+y.substring(4)];
					records.push(obj);
				}
			}
			
		}
		var params = {dataArray:$.toJSON(records)};
		$.ajax({
			url:'saveOrUpdateProductData.action',
			data:params,
			dataType:'json',
			type: "POST",
			success:function(r){
				$.messager.progress('close');
				
				if(r&&r == 'success'){
					$('#dg').datagrid('acceptChanges');
					$.messager.show({msg:'保存成功!',title:'成功'});
					queryProductData();
					//回调计算
					$.ajax({
						url:'saveOrUpdateProductDataCallback.action',
						data:'month='+date+'&shop='+shop});
				}else{
					$('#dg').datagrid('rejectChanges');
				}
				$('#dg').datagrid('unselectAll');
			}
		});
		
	}
	
	/** 重新计算 */
	function reCalculate(){
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
		
		$.messager.progress({title:'等待',msg:'正在保存...',text:'处理中.......'});
		$.ajax({
			url:'saveOrUpdateProductDataCallback.action',
			data:'month='+date+'&shop='+shop,
			success:function(r){
				$.messager.progress('close');
				
				if(r&&r == 'success'){
					$.messager.show({msg:'重新计算成功!',title:'成功'});
				}
			}
		});
		
	}
	
	/** 清空某一月全部数据   */
	function cleanData(){
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
		
		$.messager.progress({title:'等待',msg:'正在保存...',text:'处理中.......'});
		jQuery.messager.confirm('提示:','你确认要把该月的生产, 工时,工资全部删除吗?',function(event){   
			if(event){   
				$.ajax({
					url:'cleanData.action',
					data:'month='+date+'&shop='+shop,
					dataType:'json',
					success:function(r){
						if(r&&r == 'success'){
							$.messager.progress('close');
							$.messager.show({msg:'删除成功!',title:'成功'});
							queryProductData();
						}
					}
				});
			}else{
				$.messager.progress('close');
			}
		});	
		
	}
	
	