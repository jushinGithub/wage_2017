  $(function () {
	   //设置日期
	    setMonthDate();
    });  
	
    /** 车间加载成功 选中第一个 */
	function loadCombox(data){
		$("#shop").combobox('select',data[0].id);
		var shop = parseInt($('#shop').combobox('getValue'));
		var date = $('#month').datebox('getValue');
		//设置表头
	    //setGroupHeader(shop, date);
	}
	
	/** 设置表头 */
 	function setGroupHeader(shop, date){
        $.ajax({
			url:'queryGroupName.action',
			data:'shop='+shop+"&date="+date,
			success:function(groupName){
				if(groupName.length > 0){ 
					 var compHeader = "";
					 var columnsPiece = [];
					//2.设置cols, 第一行和第二行
			        var firstPart = [{field:'emId',    title:'货物主键',hidden:true, rowspan:2},
			                         {title:'生产数据', colspan:groupName.length+1},
			                         {title:'计件工资', colspan:groupName.length+1}
			        				 ];
			        
			        var secondPart = [];
			        for(var i=0; i<groupName.length; i++){
			        	secondPart.push({field:'comp'+groupName[i],title:groupName[i],width:80,halign:'center',align:'right'});
			        }
			        secondPart.push({field:'sumSlComp',title:'合计',width:80,halign:'center',align:'right'});
			        
			        for(var j=0; j<groupName.length; j++){
			        	secondPart.push({field:'completeJe'+groupName[j],title:groupName[j],width:80,halign:'center',align:'right'});
			        }
			        secondPart.push({field:'sumJeComp',title:'合计',width:80,halign:'center',align:'right'});
			        
			        columnsPiece.push(firstPart);
					columnsPiece.push(secondPart);
			        
			        //3.初始化Grid
					 $('#dg').datagrid({
						    columns: columnsPiece,
						    iconCls: 'icon-edit',
							singleSelect: true,
							rownumbers: true,
							showFooter:true,
							frozenColumns:[[{field:'itemNo',   title:'货号',   width:100,halign:'center', align:'left'},
									       {field:'itemName',  title:'品名',   width:100,halign:'center', align:'left'},
									       {field:'cggj',      title:'工价',   width:100,halign:'center', align:'left'}
							              ]]
						});
					 
					//组合表头
					compHeader = '<tr><th rowspan="2">货号</th><th rowspan="2" >品名</th><th rowspan="2" >工价</th>'
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
					$("#printBtn").bind('click',{title: '车工计件工资明细表',compHeader:compHeader,rowNum:20},printData);
					
					//查询数据
					queryProductData();
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
		
		$.ajax({
			url:'getProductDataCg.action',
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
	