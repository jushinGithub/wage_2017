<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>各工段工资测算表(表三)</title>
    
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
	<script type="text/javascript" src="page/salary/report/report_three.js"></script>
	
  </head>
  
  <body>
    <div class="easyui-layout" style="width:100%;height:100%;">
		<div data-options="region:'center'" >
			<!--  query -->
	    	 <div >
	    	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		                            车间:<input id="shop" class="easyui-combobox"  data-options="editable:false,valueField:'id', textField:'text',url:'getShop.action',onLoadSuccess:loadCombox,onChange:queryProductPrice"  style="width:100px;">
	    	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                          月份: <input  id="month" class="easyui-datebox"  style="width:100px"> <!--  data-options="formatter:myformatter,parser:myparser" -->
		        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		        <a  class="easyui-linkbutton" iconCls="icon-search"  onclick="queryProductPrice()" >查询</a>  
		        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true"  id="printBtn">打印</a>
		    </div>
		
			<!--grid  -->
			<table id="dg" class="easyui-datagrid" title="各工段工资测算表" style="width:100%;height:92%" 
					data-options="
						iconCls: 'icon-edit',
						singleSelect: true,
						showFooter:true,
						rownumbers:true,
						onClickRow: ondbClickRow
					">
				<thead data-options="frozen:true">	
			       <tr>
			            <th data-options="field:'ck',checkbox:true,"  ></th>
			    	    <th data-options="field:'itemNo',   width:100,halign:'center',align:'left'" >货号</th>
					    <th data-options="field:'itemName', width:100,halign:'center',align:'left'" >品名</th>
			    	</tr>	
				<thead>
				<thead>
					 <tr>
		                <th colspan="3">裁剪</th>
		                <th colspan="3">车工</th>
		                <th colspan="3">手工</th>
		                <th colspan="3">烫工</th>
		            </tr>
					<tr>
						<th data-options="field:'cjgj',     width:80,halign:'center',align:'right'">工价</th>
						<th data-options="field:'cjsl',     width:80,halign:'center',align:'right'">数量</th>
						<th data-options="field:'cjje',     width:80,halign:'center',align:'right'">金额</th>
						
						<th data-options="field:'cggj',     width:80,halign:'center',align:'right'">工价</th>
						<th data-options="field:'cgsl',     width:80,halign:'center',align:'right'">数量</th>
						<th data-options="field:'cgje',     width:80,halign:'center',align:'right'">金额</th>
						
						<th data-options="field:'sggj',     width:80,halign:'center',align:'right'">工价</th>
						<th data-options="field:'sgsl',     width:80,halign:'center',align:'right'">数量</th>
						<th data-options="field:'sgje',     width:80,halign:'center',align:'right'">金额</th>
						
						<th data-options="field:'tggj',     width:80,halign:'center',align:'right'">工价</th>
						<th data-options="field:'tgsl',     width:80,halign:'center',align:'right'">数量</th>
						<th data-options="field:'tgje',     width:80,halign:'center',align:'right'">金额</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div><!--grid  -->
	</div>
  </body>
  
  
</html>
