<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>工资项目设置</title>
    
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/demo/demo.css">
	
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.json-2.3.js"></script>
	<script type="text/javascript" src="page/salary/wage.js"></script>
	
  </head>
  
  <body>
    <div class="easyui-layout" style="width:100%;height:100%;">
		<!--grid  -->
		<div data-options="region:'center'" >
			<div id="tb" style="height:auto">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true"   onclick="accept()">保存</a>
			</div>
			
			<table id="dg" class="easyui-datagrid" title="工资项目设置" style="width:100%;height:97%" 
					data-options="
						iconCls: 'icon-edit',
						singleSelect: true,
						toolbar: '#tb',
						url:'getWageAction.action',
						onClickRow: ondbClickRow
					">
				<thead>
					<tr>
						<th data-options="field:'wageNo',   width:60,halign:'center',align:'left'">项目代号</th>
						<th data-options="field:'wageName', width:100,halign:'center',align:'left', editor:'textbox' ">项目</th>
						<th data-options="field:'symbol',   width:60,align:'center', editor:{type:'combobox',options:{data: symblArray, valueField:'value',textField:'text'}}, formatter:symFormatter">符号</th>
						<th data-options="field:'isPiece',  width:60,align:'center', editor:{type:'checkbox',options:{on:'√',off:''}}">计件使用</th>
						<th data-options="field:'isTime',   width:60,align:'center', editor:{type:'checkbox',options:{on:'√',off:''}}">计时使用</th>
						<th data-options="field:'defaulteValue',width:100,halign:'center',align:'right', editor:'textbox'">默认值</th>
						<th data-options="field:'uuid',     width:100,align:'center', hidden:true">主键</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div><!--grid  -->
	</div>
  </body>
  
  
</html>
