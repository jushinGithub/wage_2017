<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%
	String MatAndEqu_path = request.getContextPath();
	String MatAndEqu_basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ MatAndEqu_path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>物资匹配管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<jsp:include page="../../commom/easyui.jsp"></jsp:include>
		<script type="text/javascript" src="<%=MatAndEqu_basePath %>js/commom/base.js"></script>
		<script type="text/javascript" src="<%=MatAndEqu_basePath %>js/commom/json2.js"></script>
		<script type="text/javascript">
		$.extend($.fn.datagrid.methods, {
			editCell: function(jq,param){
				return jq.each(function(){
					var opts = $(this).datagrid('options');
					var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor1 = col.editor;
						if (fields[i] != param.field){
							col.editor = null;
						}
					}
					$(this).datagrid('beginEdit', param.index);
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor = col.editor1;
					}
					var ed = $(this).datagrid('getEditor', param);
					
                    if (ed){
                        if ($(ed.target).hasClass('textbox-f')){
                            $(ed.target).textbox('textbox').focus();
                        } else {
                            $(ed.target).focus();
                        }
                    }
			//放这里
				//begin新增功能
				
					var editors = $('#dg').datagrid('getEditors', param.index);
					var amountEditor = editors[0];
					//end
					//begin回车事件处理,直接换行到指定单元格!也可以设定一个热键来响应数量单元格的数据!
					amountEditor.target
					.on(
						'keydown',
						function(e) {
							if (e.keyCode == 39 ) {
								$('#dg').datagrid('endEdit', param.index);
								for(var i=0; i<fields.length; i++){
									if (fields[i] == param.field&&fields[fields.length-1]!=param.field){
										$('#dg').datagrid('editCell', {index:param.index ,field:fields[i+1]});
									}
								}
								if(fields[fields.length-1] == param.field){
										var rows = $("#dg").datagrid("getRows");
										//判断最大的行号是否已经是最后一行
										if(rows.length >  param.index + 1){
											//编辑下一行
											$('#dg').datagrid('selectRow', param.index + 1).datagrid('editCell', {index:param.index + 1,field:fields[0]});
											$('#dg').datagrid('endEdit',param.index);
											editIndex = param.index+1;
										}else{
											editIndex = 0;
										}
										
									}
								//返回到第一个单元格
								var row = $('#dg').datagrid('getSelected');
								var rowIndex = $("#dg").datagrid('getRowIndex', row);
								var rows = $("#dg").datagrid("getRows");
								
								if(rows.length-1==param.index&&fields[fields.length-1] == param.field){
									$('#dg').datagrid('selectRow',0).datagrid('editCell', {index:0 ,field:fields[0]});
									
								}
							}
							if (e.keyCode == 38) {//上光标
								var selected = $('#dg').datagrid('getSelected');
								var index = $('#dg').datagrid('getRowIndex', selected);
			                    if (selected&&index!=0) {
			                        $('#dg').datagrid('selectRow', index - 1).datagrid('editCell', {index:param.index - 1,field:param.field});
			                        $('#dg').datagrid('endEdit',param.index);
			                        editIndex  = param.index-1;
			                    } else {
			                        var rows = $('#dg').datagrid('getRows');
			                        $('#dg').datagrid('selectRow', rows.length - 1).datagrid('editCell', {index:rows.length - 1,field:param.field});
			                        $('#dg').datagrid('endEdit',param.index);
			                        editIndex = rows.length - 1;
			                    }		
							}
							if (e.keyCode == 37) {//左光标
								    $('#dg').datagrid('endEdit', param.index);
									var row = $('#dg').datagrid('getSelected');
									var rowIndex = $("#dg").datagrid('getRowIndex', row);
									var rows = $("#dg").datagrid("getRows");
									
									for(var i=0; i<fields.length; i++){
										if (fields[i] == param.field&&fields[0]!=param.field){
											$('#dg').datagrid('editCell', {index:param.index ,field:fields[i-1]});
										}
									}
									if(fields[0] == param.field){	
						                 var rows = $("#dg").datagrid("getRows");
										//判断最大的行号是否已经是最后一行
										if(0 < param.index){
											//编辑下一行
											$('#dg').datagrid('selectRow', param.index - 1).datagrid('editCell', {index:param.index - 1,field:fields[fields.length-1]});
											$('#dg').datagrid('endEdit',param.index);
											editIndex = param.index-1;
										}else{
											editIndex = rows.length-1;
										}
									}
									//返回到最后一个单元格
								if(0==param.index&&fields[0] == param.field){
									$('#dg').datagrid('selectRow',rows.length-1).datagrid('editCell', {index:rows.length-1,field:fields[fields.length-1]});
									$('#dg').datagrid('endEdit',param.index);
								}
							}
							if(e.keyCode == 40||e.keyCode==13){
								var selected = $('#dg').datagrid('getSelected');
								var index = $('#dg').datagrid('getRowIndex', selected);
								var rows = $('#dg').datagrid('getRows');
			                    if (selected&&index!=rows.length-1) {
			                        $('#dg').datagrid('selectRow', index + 1).datagrid('editCell', {index:param.index + 1,field:param.field});
			                        $('#dg').datagrid('endEdit',param.index);
			                        editIndex  = param.index+1;  
			                    } else {
			                        $('#dg').datagrid('selectRow', rows.length-1 - index).datagrid('editCell', {index:rows.length -1- index,field:param.field});
			                        $('#dg').datagrid('endEdit',param.index);
			                        editIndex =0;
			                    }	
							}
						});
			//放这里		
				});
				
			}
		});
		
		
		
		
		
		var editIndex = undefined;
		function endEditing(){
			if (editIndex == undefined){return true}
			if ($('#dg').datagrid('validateRow', editIndex)){
				$('#dg').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickCell(index, field){
			if (endEditing()){
				$('#dg').datagrid('selectRow', index)
						.datagrid('editCell', {index:index,field:field});
				editIndex = index;
			}
		}
			$(function(){
				$('#dg').datagrid({    
				    url:'wzppgltable.action',  
				    toolbar:'#tc',
				    fit:true,
				    onClickCell: onClickCell,
				    pagination:true, 
				    singleSelect:true,  
				    columns:[[   
				        {field:'wzbm',title:'物资匹配表编码',width:100,align:'center',editor : {type : 'validatebox'}},    
				        {field:'name',title:'物资匹配表名称',width:100,align:'center',editor : {type : 'validatebox'}},    
				        {field:'wzyt',title:'物资匹配表用途',width:100,align:'center',editor : {type : 'validatebox'}},
				        {field:'sfty',title:'是否启用',width:100,align:'center',editor : {type : 'validatebox'}}     
				    ]],
				    onClickRow:function(rowIndex, rowData){
						$("#whppnr").linkbutton("enable");
				    }
				}); 
				$("#selected_save").linkbutton("disable");
				$("#whppnr").linkbutton("disable");
				
                        
			})
			
			//删除
			function sc(){
				var row = $('#dg').datagrid('getSelected');
				if (row) {
					var rowIndex = $("#dg").datagrid('getRowIndex', row);
					$("#dg").datagrid('deleteRow', rowIndex);
					$.ajax({
			        	type:'post',
			        	url:'sc.action',
			       		dataType:'json',
			        	data :{"id":row.id},
			        	success:function(data){
			        		$.messager.alert('提示','删除成功！','info');
		 				},
		 				error:function(){
			 					$.messager.alert('提示','删除失败！','info');
			 			}
		 			})
				} else {
					$.messager.alert('错误', '请选择要删除的行!', 'error');
				}
			}
			
			//修改
			function xg(){
				$("#selected_save").linkbutton("enable");
				$("#append").linkbutton("disable");
				$("#sc").linkbutton("disable");
				$("#ty").linkbutton("disable");
			}
			//新增
			function append(){
				$("#selected_save").linkbutton("enable");
				$("#xg").linkbutton("disable");
				$("#sc").linkbutton("disable");
				$("#ty").linkbutton("disable");
				if (endEditing()){
					$('#dg').datagrid('appendRow',{
						wzbm: '',
						name: '',
						wzyt: '',
						sfty: ''
					});
	
					
					editIndex = $('#dg').datagrid('getRows').length-1;
					$('#dg').datagrid('selectRow', editIndex);		
				}
			
			}
			//保存
			function selected_save(){
				var editGrid=true;
				var xz=$("#dg").datagrid('getSelections');
				if(xz.length<1){
					$.messager.alert('提示','请选择数据！','info');
					return;
				}
				var index = $('#dg').datagrid('getRowIndex', xz[0]);
				$("#dg").datagrid('endEdit',index);
				if(xz[0].wzbm==null||xz[0].wzbm==""){
					$.messager.alert('提示','请输入物资匹配表编码！','info');
					return;
				}
				if(xz[0].name==null||xz[0].name==""){
					$.messager.alert('提示','请输入物资匹配表名称！','info');
					return;
				}
				if(xz[0].wzyt==null||xz[0].wzyt==""){
					$.messager.alert('提示','请输入物资匹配表用途！','info');
					return;
				}
				if (editGrid) {
					editGrid = false;
					$.ajax({
			        	type:'post',
			        	url:'savepp.action',
			        	dataType:'json',
			        	data :{
							"wzppglBo.id":xz[0].id,
							"wzppglBo.wzbm":xz[0].wzbm,
							"wzppglBo.name":xz[0].name,
							"wzppglBo.wzyt":xz[0].wzyt,
							"wzppglBo.sfty":xz[0].sfty
							},
			        	success:function(data){
		 					$.messager.alert('提示','保存成功！','info');
		 					editGrid = true;
		 				},
		 				error:function(){
		 					$.messager.alert('提示','保存失败！','info');
		 					editGrid = true;
		 				}
		 			})
		 			$('#dg').datagrid('load');
					$("#selected_save").linkbutton("disable");
					$("#xg").linkbutton("enable");
					$("#sc").linkbutton("enable");
					$("#ty").linkbutton("enable");
					$("#append").linkbutton("enable");
			  }
			}
			//维护匹配内容
			function whppnr(){
				var xz=$("#dg").datagrid('getSelections');
				window.location.href="wzppglitems.action?&id="+xz[0].id;
			}
		</script>
	</head>
	<body class="easyui-layout">
		<div data-options="region:'center'" style="padding:5px;background:#eee;" >
			<table id="dg"></table>  
		</div> 
		<div id="tc">
	     	 <a href="#" class="easyui-linkbutton" data-options="plain:true" id="selected_save" onclick="selected_save()">保存</a>
	    	 <a href="#" class="easyui-linkbutton" data-options="plain:true" id="whppnr" onclick="whppnr()">维护匹配内容</a>
    	  	 <a href="#" class="easyui-linkbutton" data-options="plain:true" id="append" onclick="append()">新增</a>
    	 	 <a href="#" class="easyui-linkbutton" data-options="plain:true" id="xg" onclick="xg()">修改</a>
    	 	 <a href="#" class="easyui-linkbutton" data-options="plain:true" id="sc" onclick="sc()">删除</a>
    	  	 <a href="#" class="easyui-linkbutton" data-options="plain:true" id="ty" onclick="ty()">停用</a>
      </div> 
	</body>
</html>
