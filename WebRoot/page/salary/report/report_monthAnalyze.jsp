<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>月工资分析表</title>
    
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
	<script type="text/javascript" src="page/salary/report/report_monthAnalyze.js"></script>
	
  </head>
  
  <body>
    <div class="easyui-layout" style="width:100%;height:100%;">
		<!--grid  -->
		<div data-options="region:'center'" >
		     <div  style="padding:2px 5px;">
		     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		                   车间:<input id="shop" class="easyui-combobox"  data-options="editable:false,valueField:'id', textField:'text',url:'getShop.action',onLoadSuccess:loadCombox,onChange:queryReportFive"  style="width:100px;">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                    月份: <input  id="month" class="easyui-datebox"  style="width:100px">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                   
		      <a class="easyui-linkbutton" iconCls="icon-search" onclick="queryReportFive()">查询</a>
		       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		      <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true"  id="printBtn">打印</a>
		      &nbsp;&nbsp;&nbsp;
		      <a  class="easyui-linkbutton" iconCls="icon-search"  id="exportBtn" >导出</a> 
		    </div>
			
			<table id="dg" class="easyui-datagrid" title="月工资分析表" style="width:100%;height:92%" 
					data-options="
						iconCls: 'icon-edit',
						singleSelect: true,
						showFooter:true,
						rowStyler: rowStyleFun,
						rownumbers:true
					">
				<thead>
					<tr>
						<th data-options="field:'month',        hidden:true,width:80,halign:'center',align:'left'">月份</th>
						<th data-options="field:'orgName',      width:100,halign:'center',align:'left'">工组</th>
						<th data-options="field:'emSum',        width:60,halign:'center',align:'right'">人数</th>
						<th data-options="field:'sumItem',      width:80,halign:'center',align:'right'">本月产量</th>
						<th data-options="field:'evgPrice',     width:80,halign:'center',align:'right'">平均工价</th>
						<th data-options="field:'pieceSum',     width:100,halign:'center',align:'right'">工资总金额</th>
						<th data-options="field:'koukuanSum',   width:100,halign:'center',align:'right'">扣款合计</th>
						<th data-options="field:'salarySum',    width:100,halign:'center',align:'right'">实发计件额</th>
						
						
						<th data-options="field:'salaryMax',    width:80,halign:'center',align:'right'">最高工资</th>
						<th data-options="field:'evgSalary',    width:80,halign:'center',align:'right'">人均工资</th>
						<th data-options="field:'sumGrade',     width:80,halign:'center',align:'right'">总工时分值</th>
						<th data-options="field:'timeSalary',   width:80,halign:'center',align:'right'">工时分工资</th>
						
						<th data-options="field:'timeSum',      hidden:true, width:80,halign:'center',align:'right'">总工时</th>
						<th data-options="field:'gradeSalary',  hidden:true, width:80,halign:'center',align:'right'">分值工资</th>
						
						
						
						
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div><!--grid  -->
	</div>
  </body>
</html>
