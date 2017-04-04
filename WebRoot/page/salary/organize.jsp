<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>员工管理</title>
    
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/demo/demo.css">
	
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.json-2.3.js"></script>
	<script type="text/javascript" src="page/salary/common/common.js"></script>
	<script type="text/javascript" src="page/salary/organize.js"></script>
	
  </head>
  
  <body >
    <div class="easyui-layout" style="margin:0px;width:100%;height:100%;">
    	<!-- tree -->
		<div id="p" data-options="region:'west'" title="班组" style="width:20%;height:100%;padding:8px">
			<div  class="easyui-panel" style="height:100%;padding:5px">
				<ul id=orgTree class="easyui-tree" 
					data-options="url:'getOrgTreeNodes.action', lines:true, onClick:onTreeClick ">
				</ul>
			</div>
		</div> <!-- tree -->
		
		<!--grid  -->
		<div data-options="region:'center'" >
		
			<div id="tb" style="height:auto">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"    onclick="append()">新增</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">离职</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true"   onclick="accept()">保存</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"   onclick="addOrg()">增加|修改 部门</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input id='containAll' class="easyui-switchbutton" data-options="width:'90px',onText:'含离职员工', offText:'在职员工', onChange:switchChange" >
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeHistory()">删除员工</a>
			</div>
			
			<table id="dg" class="easyui-datagrid" title="员工信息" style="width:100%;height:97%" ></table>
		</div><!--grid  -->
	</div>
  </body>
  
  
</html>
