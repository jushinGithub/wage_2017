<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>主页</title>
		<style>
		body {
			margin: 0;
			padding: 0;
			font-size: 75%;
			background-image: url('resource/images/log_bg.jpg');
			background-repeat: no-repeat;
		}
		
		#logDiv {
			position: absolute;
			left: 690px;
			top: 200px;
			padding-top: 40px;
		}
		
		.log-div {
			position: relative;
			left: 60px;
		}
		
		.log-inp {
			width: 226px;
			height: 34px;
			font-size: 20px;
		}
		
		#submitDiv {
			position: relative;
			left: 20px;
			top: 100px;
			width: 268px;
			height: 40px;
			cursor: pointer;
		}
		</style>
		<script src="js/jquery-1.8.0.js" type="text/javascript"></script>
		<script type="text/javascript" src="js/jquery.form.js"></script>
	</head>
	<body>
		<div style="margin:20px 0;"></div>
		<div id="logDiv">
			<form id="logForm" method="post" action="loginAction.action">
				<div class="log-div" style="top: 35px;">
					<input class="log-inp" type="text" id="user" name="user"></input>
				</div>
				<div class="log-div" style="top: 66px;">
					<input class="log-inp" style="height: 35px;" type="password"
						id="password" name="password"></input>
				</div>
			</form>
			<div id="submitDiv"></div>
		</div>
		
		<script type="text/javascript">
			$(document).ready(function() {
				$('#submitDiv').click(function() {
					$('#logForm').ajaxSubmit({
						success : function(data) {
							if (data == 'success') {
								window.location.href="main.jsp";
							}else{
								window.location.href="index.jsp";
							}
						},
						error : function() {
							alert('用户名或密码错误！');
						}
					});
				});
			});
		</script>
	</body>
</html>
