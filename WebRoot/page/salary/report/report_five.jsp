<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>计件工资测算表(表五)</title>
    
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/demo/demo.css">
	
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.json-2.3.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.jqprint-0.3.js"></script>  
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery-migrate-1.1.0.js"></script> 
    <script type="text/javascript" src="js/printRep.js"></script>
	<script type="text/javascript" src="page/salary/common/common.js"></script>
	<script type="text/javascript" src="page/salary/report/report_five.js"></script>
	
  </head>
  
  <body>
    <div class="easyui-layout" style="width:100%;height:100%;">
		<!--grid  -->
		<div data-options="region:'center'" >
		     <div  style="padding:2px 5px;">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   
		                    班组:<input id="orgId" class="easyui-combotree" value="21201" data-options="url:'getOrgTreeNodes.action?pid=1',method:'get',onChange:queryReportFive" style="width:150px;">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                    月份: <input  id="month" class="easyui-datebox"  style="width:100px">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                   
		      <a class="easyui-linkbutton" iconCls="icon-search" onclick="queryReportFive()">查询</a>
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		      <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true"  id="printBtn">打印</a>
		    </div>
			
			<table id="dg" class="easyui-datagrid" title="计件工资测算表" style="width:100%;height:92%" 
					data-options="
						iconCls: 'icon-edit',
						singleSelect: true,
						showFooter:true,
						rownumbers:true
					">
				<thead>
					<tr>
						<th data-options="field:'itemNo',width:100,halign:'center',align:'left'">货号</th>
						<th data-options="field:'itemName',width:150,halign:'center',align:'left'">品名</th>
						<th data-options="field:'cgsl',    width:80,halign:'center',align:'right'">数量</th>
						<th data-options="field:'cggj',    width:80,halign:'center',align:'right'">工价</th>
						<th data-options="field:'piecePay',width:80,halign:'center',align:'right'">计件工资</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div><!--grid  -->
	</div>
  </body>
</html>
