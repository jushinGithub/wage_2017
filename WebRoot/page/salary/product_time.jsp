<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>工时分设置</title>
    
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/demo/demo.css">
	
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.json-2.3.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery-migrate-1.1.0.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.jqprint-0.3.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.jqprint-0.3.js"></script>  
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery-migrate-1.1.0.js"></script> 
    <script type="text/javascript" src="js/printRep.js"></script>
	<script type="text/javascript" src="page/salary/common/common.js"></script>
	<script type="text/javascript" src="page/salary/product_time.js"></script>
	
  </head>
  
  <body>
    <div class="easyui-layout" style="width:100%;height:100%;">
		<!--grid  -->
		<div   data-options="region:'center'" >
		     <div  style="padding:2px 5px;">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		                    班组:<input id="orgId" class="easyui-combotree" value="21201" data-options="url:'getOrgTreeNodes.action?pid=1',method:'get',onChange:queryProductTime"  style="width:150px;">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                    月份: <input  id="month" class="easyui-datebox"  style="width:100px">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		      <a class="easyui-linkbutton" iconCls="icon-search" onclick="queryProductTime()">查询</a>
		       &nbsp;&nbsp;&nbsp;
		      <a  class="easyui-linkbutton" iconCls="icon-search"  onclick="reLoadEm()" >重新导入</a> 
		      <!--  
		      &nbsp;&nbsp;&nbsp;
		      <a  class="easyui-linkbutton" iconCls="icon-search"  onclick="delTime()" >删除</a> 
		      -->
		      &nbsp;&nbsp;&nbsp;
		      <a  class="easyui-linkbutton" iconCls="icon-search"  id="exportBtn" >导出</a> 
		    </div>
			<div id="tb" style="height:auto">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true"   onclick="accept()">保存</a>
				<!--  
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true"  id="printBtn">打印</a>
				-->
			</div>
			<table id="dg" class="easyui-datagrid" title="工时分设置" style="width:100%;height:92%" 
					data-options="
						iconCls: 'icon-edit',
						singleSelect: true,
						toolbar: '#tb',
						showFooter:true,
						rownumbers:true,
						onClickCell:selectSingleCell
					">
				<thead>
					<tr>
					    <!--  
						<th data-options="field:'ck',checkbox:true"></th>
						-->
						<th data-options="field:'month',   hidden:true,width:100,halign:'center',align:'left'">月份</th>
						<th data-options="field:'orgName', hidden:true,width:100,halign:'center',align:'left'">工组</th>
						<th data-options="field:'groupNo', width:60,halign:'center',align:'left'">小组</th>
						<th data-options="field:'emNo',    width:100,halign:'center',align:'left'">工号</th>
						<th data-options="field:'emName',  width:100,halign:'center',align:'left'">姓名</th>
						<th data-options="field:'time',    width:80,halign:'center',align:'right', editor:{type:'numberbox',options:{precision:1}}">工时</th>
						<th data-options="field:'grade',   width:80,halign:'center',align:'right', editor:{type:'numberbox',options:{precision:1}}">分值</th>
						<th data-options="field:'sumGrade',width:80,halign:'center',align:'right'">总分值</th>
						<th data-options="field:'uuid',    hidden:true">主键</th>
						<th data-options="field:'emId',    hidden:true">员工主键</th>
						<th data-options="field:'orgId',   hidden:true">班组主键</th>
						<th data-options="field:'shop',    hidden:true">车间</th> 
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div><!--grid  -->
	</div>
  </body>
  
  
</html>
