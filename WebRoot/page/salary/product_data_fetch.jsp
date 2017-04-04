<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>车工领用数</title>
    
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
	<script type="text/javascript" src="js/jqueryEnter.js"></script>
	<script type="text/javascript" src="page/salary/product_data_fetch.js"></script>
	
  </head>
  
  <body>
    <div class="easyui-layout" style="width:100%;height:100%; padding:0px;"  >
    	
		<!--grid  -->
		<div data-options="region:'center'" >
		     <!--  query -->
	    	 <div>
	    	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		                   车间:<input id="shop" class="easyui-combobox" data-options="editable:false,valueField:'id', textField:'text',url:'getShop.action',onLoadSuccess:loadCombox,onChange:setGroupHeader"  style="width:100px;">
	    	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                    月份: <input  id="month" class="easyui-datebox"  style="width:100px">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <a  class="easyui-linkbutton" iconCls="icon-search"  onclick="queryProductData()" >查询</a>  &nbsp;&nbsp;&nbsp;
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <a  class="easyui-linkbutton" iconCls="icon-search"  onclick="reloadProductData()" >导入生产数据</a>
		    </div>
			<div id="tb" style="height:auto">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true"   onclick="accept()">保存</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true"  id='printBtn'>打印</a>
			</div>
			<table id="dg" class="easyui-datagrid" title="车工领用数" style="width:100%;height:90%"
			   data-options="onClickCell:selectSingleCell"></table>
				
		</div><!--grid  -->
		
		<!--  新增货物  -->
		<div id="popWin" class="easyui-window" title="新增货号" data-options="modal:true,closed:true,iconCls:'icon-save'" 
			style="width:750px;height:500px;padding:1px;">
        	<table id="dgPop" class="easyui-datagrid"  style="width:99%;height:92%" 
					data-options="singleSelect: false,checkbox: true"></table>
					
			<div style="text-align:center;padding:5px">
	            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="savePopWin()">保存</a>
	            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="resetPopWin()">取消</a>
	        </div>
    	</div>
    
	</div>
  </body>
  
  
   
  
  
</html>
