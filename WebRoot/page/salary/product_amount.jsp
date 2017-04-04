<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>生产数量调整(表一)</title>
    
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
	<script type="text/javascript" src="page/salary/product_amount.js"></script>
	
  </head>
  
  <body>
    <div class="easyui-layout" style="width:100%;height:100%;">
		<div data-options="region:'center'" >
			<!--  query -->
	    	 <div >
	    	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		                   车间:<input id="shop" class="easyui-combobox"  data-options="editable:false,valueField:'id', textField:'text',  url:'getShop.action',onLoadSuccess:loadCombox,onChange:queryProductPrice"  style="width:100px;">
	    	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                    月份: <input  id="month" class="easyui-datebox"  style="width:100px"> <!--  data-options="formatter:myformatter,parser:myparser" -->
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <a  class="easyui-linkbutton" iconCls="icon-search"  onclick="queryProductPrice()" >查询</a>  
		      &nbsp;&nbsp;&nbsp;
		      <a  class="easyui-linkbutton" iconCls="icon-search"  id="exportBtn" >导出</a> 
		    </div>
			<div id="tb" style="height:auto">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true"   onclick="accept()">保存</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true"  id="printBtn">打印</a>
			</div>
			<!--grid  -->
			<table id="dg" class="easyui-datagrid" title="生产数量调整" style="width:100%;height:92%" 
					data-options="
						iconCls: 'icon-edit',
						singleSelect: true,
						toolbar: '#tb',
						showFooter:true,
						rownumbers:true,
						onClickCell:selectSingleCell
					">
				<thead data-options="frozen:true">	
			       <tr>
			            <th data-options="field:'ck',checkbox:true,"  ></th>
			    	    <th data-options="field:'itemNo',   width:150,halign:'center', align:'left'" >货号</th>
					    <th data-options="field:'itemName', width:180,halign:'center', align:'left'" >品名</th>
			    	</tr>	
				<thead>
				<thead>
					 <tr>
					    <th data-options="field:'cjsl',     width:100,halign:'center', align:'right', editor:{type:'numberbox'}" >下裁数</th>
					    <th data-options="field:'fetchNums',     width:100,halign:'center', align:'right'">车工领用数</th>
					    <th data-options="field:'completeNums',  width:100,halign:'center', align:'right'">车工完工数</th>
						<th data-options="field:'sgsl',     width:100,halign:'center', align:'right', editor:{type:'numberbox'}" >手工数</th>
						<th data-options="field:'tgsl',     width:100,halign:'center', align:'right', editor:{type:'numberbox'}" >烫工数</th>
						
						<th data-options="field:'uuid',     hidden:true"  >主键</th>
						<th data-options="field:'itemid',   hidden:true"  >货物主键</th>
						<th data-options="field:'shop',     hidden:true"  >车间</th>
						<th data-options="field:'orgId',    hidden:true"  >班组id</th>
						<th data-options="field:'orgNo',    hidden:true"  >班组编号</th>
						<th data-options="field:'orgName',  hidden:true"  >班组名称</th>
						<th data-options="field:'groupNo',  hidden:true"  >小组</th>
						<th data-options="field:'month',    hidden:true"  >月份</th>
		            </tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div><!--grid  -->
	</div>
  </body>
  
  
</html>
