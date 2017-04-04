<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
    <META HTTP-EQUIV="expires" CONTENT="0">

    <title>首页</title>
    
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/demo/demo.css">
	
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.json-2.3.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery-migrate-1.1.0.js"></script> 
	<script type="text/javascript" src="main.js"></script>
	<style>
	  article, aside, figure, footer, header, hgroup, 
	  menu, nav, section { display: block; }
	  .west{
	    width:150px;
	    padding:10px;
	  }
	  .north{
	    height:100px;
	  }
	</style>
  </head>
  
  <body class="easyui-layout">
	  <div region="center" >
	    <div class="easyui-tabs" fit="true" border="false" id="tabs">
	      
	    </div>
	  </div>
	  <div region="west" class="west" title="导航菜单">
	    <ul id="tree"></ul>
	  </div>
	  
	  <div id="tabsMenu" class="easyui-menu" style="width:120px;">  
	    <div name="close">关闭</div>  
	    <div name="Other">关闭其他</div>  
	    <div name="All">关闭所有</div>
	  </div>  
	</body>
</html>
