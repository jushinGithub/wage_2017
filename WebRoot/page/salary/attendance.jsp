<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>考勤表</title>
    
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
	<script type="text/javascript" src="page/salary/attendance.js"></script>
	
  </head>
  
  <body>
    <div class="easyui-layout" style="width:100%;height:100%;">
		<!--grid  -->
		<div   data-options="region:'center'" >
		     <div  style="padding:2px 5px;">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		                    班组:<input id="orgId" class="easyui-combotree" value="21201" data-options="url:'getOrgTreeNodes.action?pid=1',method:'get',onChange:queryAttendace"  style="width:150px;">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                    月份: <input  id="month" class="easyui-datebox"  style="width:100px">
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
		      <a class="easyui-linkbutton" iconCls="icon-search" onclick="queryAttendace()">查询</a>
		      
		    </div>
			<div id="tb" style="height:auto">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true"  id="printBtn">打印</a>
				&nbsp;&nbsp;&nbsp;
		        <a  class="easyui-linkbutton" iconCls="icon-search"  id="exportBtn" >导出</a> 
			</div>
			<table id="dg" class="easyui-datagrid" title="考勤表" style="width:100%;height:92%" 
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
						<th data-options="field:'emNo',    width:70,halign:'center',align:'left'">工号</th>
						<th data-options="field:'emName',  width:50,halign:'center',align:'left'">姓名</th>
						
						<th data-options="field:'day1',  width:20,halign:'center',align:'left'">&nbsp;1</th>
						<th data-options="field:'day2',  width:20,halign:'center',align:'left'">&nbsp;2</th>
						<th data-options="field:'day3',  width:20,halign:'center',align:'left'">&nbsp;3</th>
						<th data-options="field:'day4',  width:20,halign:'center',align:'left'">&nbsp;4</th>
						<th data-options="field:'day5',  width:20,halign:'center',align:'left'">&nbsp;5</th>
						<th data-options="field:'day6',  width:20,halign:'center',align:'left'">&nbsp;6</th>
						<th data-options="field:'day7',  width:20,halign:'center',align:'left'">&nbsp;7</th>
						<th data-options="field:'day8',  width:20,halign:'center',align:'left'">&nbsp;8</th>
						<th data-options="field:'day9',  width:20,halign:'center',align:'left'">&nbsp;9</th>
						<th data-options="field:'day10', width:20,halign:'center',align:'left'">10</th>
						
						<th data-options="field:'day11',  width:20,halign:'center',align:'left'">11</th>
						<th data-options="field:'day12',  width:20,halign:'center',align:'left'">12</th>
						<th data-options="field:'day13',  width:20,halign:'center',align:'left'">13</th>
						<th data-options="field:'day14',  width:20,halign:'center',align:'left'">14</th>
						<th data-options="field:'day15',  width:20,halign:'center',align:'left'">15</th>
						<th data-options="field:'day16',  width:20,halign:'center',align:'left'">16</th>
						<th data-options="field:'day17',  width:20,halign:'center',align:'left'">17</th>
						<th data-options="field:'day18',  width:20,halign:'center',align:'left'">18</th>
						<th data-options="field:'day19',  width:20,halign:'center',align:'left'">19</th>
						<th data-options="field:'day20',  width:20,halign:'center',align:'left'">20</th>
						
						<th data-options="field:'day21',  width:20,halign:'center',align:'left'">21</th>
						<th data-options="field:'day22',  width:20,halign:'center',align:'left'">22</th>
						<th data-options="field:'day23',  width:20,halign:'center',align:'left'">23</th>
						<th data-options="field:'day24',  width:20,halign:'center',align:'left'">24</th>
						<th data-options="field:'day25',  width:20,halign:'center',align:'left'">25</th>
						<th data-options="field:'day26',  width:20,halign:'center',align:'left'">26</th>
						<th data-options="field:'day27',  width:20,halign:'center',align:'left'">27</th>
						<th data-options="field:'day28',  width:20,halign:'center',align:'left'">28</th>
						<th data-options="field:'day29',  width:20,halign:'center',align:'left'">29</th>
						<th data-options="field:'day30',  width:20,halign:'center',align:'left'">30</th>
						<th data-options="field:'day31',  width:20,halign:'center',align:'left'">31</th>
						
						<th data-options="field:'workDay',width:50,halign:'center',align:'left'">天数</th>
						<th data-options="field:'time',   width:50,halign:'center',align:'left'">工时</th>
						<th data-options="field:'grade',  width:50,halign:'center',align:'left'">分值</th>
						<th data-options="field:'bonus',  width:50,halign:'center',align:'left'">全勤奖</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div><!--grid  -->
	</div>
  </body>
  
  
</html>
