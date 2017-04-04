<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>系统设置</title>
    
    <link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="jquery-easyui-1.4.4/demo/demo.css">
	
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="jquery-easyui-1.4.4/jquery.json-2.3.js"></script>
	<script type="text/javascript" src="page/salary/config.js"></script>
	
  </head>
  
  <body>
    <h2>系统配置设置</h2>
    <p></p>
    
    <div class="easyui-panel" title=" " style="width:600px">
        <div style="padding:10px 10px 10px 10px">
	        <form id="ff" action="saveMyConfig.action" method="post">
	            <table cellpadding="5">
	            	<tr>
	            		<th colspan="2" align="center" >数据计算设定</th> 
	            		<th colspan="2" align="center" >工价比例设定</th>
	            	</tr>
	                <tr>
	                    <td  align="right" style="width: 80px;">车工:</td>
	                    <td>
	                        <select class="easyui-combobox" style="width: 120px" name="configuration.cgpz">
	                          <option value="车工完工数">车工完工数</option>
	                          <option value="车工领活数">车工领活数</option>
	                       </select>
	                    </td>
	                    
	                    <td align="right" style="width: 80px;">车工工价:</td>
	                    <td><input   style="width:120px" class="easyui-textbox" type="text" name="configuration.cggj" ></input></td>
	                </tr>
	                <tr>
	                    <td align="right"style="width: 80px;">裁剪:</td>
	                    <td>
	                        <select class="easyui-combobox" style="width: 120px"  name="configuration.cjpz">
	                          <option value="车工完工数">车工完工数</option>
	                          <option value="裁剪完工数">裁剪完工数</option>
	                          <option value="车工领活数">车工领活数</option>
	                       </select>
	                    </td>
	                    
	                    <td align="right" style="width: 80px;">裁剪工价比例:</td>
	                    <td><input   style="width:120px" class="easyui-textbox" type="text" name="configuration.cjgj" ></input></td>
	                </tr>
	                <tr>
	                    <td align="right" style="width: 80px;">手工:</td>
	                    <td>
	                        <select class="easyui-combobox" style="width: 120px"  name="configuration.sgpz">
	                          <option value="车工完工数">车工完工数</option>
	                          <option value="尾段入库数">尾段入库数</option>
	                          <option value="车工领活数">车工领活数</option>
	                       </select>
	                    </td>
	                    
	                    <td align="right" style="width: 80px;">手工工价比例:</td>
	                    <td><input   style="width:120px" class="easyui-textbox" type="text" name="configuration.sggj" ></input></td>
	                </tr>
	                <tr>
	                    <td align="right" style="width: 80px;">烫工:</td>
	                    <td>
	                        <select class="easyui-combobox" style="width: 120px"  name="configuration.tgpz">
	                          <option value="车工完工数">车工完工数</option>
	                          <option value="尾段入库数">尾段入库数</option>
	                          <option value="车工领活数">车工领活数</option>
	                       </select>
	                     </td>  
	                     <td align="right" style="width: 80px;">烫工工价比例:</td>
	                     <td><input   style="width:120px" class="easyui-textbox" type="text" name="configuration.tggj" ></input></td>
	                    </td>
		             <tr>    
		                <td align="right" style="width: 80px;">服务器IP地址:</td>
	                    <td><input   style="width:120px" class="easyui-textbox" type="text" name="configuration.ip" ></input></td>
	                    
	                    <td align="right" style="width: 80px;">金额保留小数:</td>
	                    <td>
	                      <select class="easyui-combobox" style="width: 120px"  name="configuration.decimals">
	                          <option value="0">0</option>
	                          <option value="1">1</option>
	                          <option value="2">2</option>
	                          <option value="3">3</option>
	                          <option value="4">4</option>
	                          <option value="5">5</option>
	                          <option value="6">6</option>
	                       </select>
	                    </td>
	                </tr>
	                <tr>
	                	 <td align="right" style="width: 80px;">公司名字:</td>
	                     <td colspan="4">
	                     	<input style="width:350px" class="easyui-textbox" type="text" name="configuration.company" ></input>
	                     </td>
	                </tr>
	                <tr style="display: none">
	                	<td><input    class="easyui-textbox" type="text" name="configuration.uuid" ></input></td>
	                </tr>
	                <tfoot>
	                 <tr>
	                 	<th colspan="4" align="left"><font color="red">说明:</font> GJ表示工价, GYJ表示工艺价</th>
	                 </tr>
	                </tfoot>
	            </table>
	        </form>
	        
	        <div style="text-align:center;padding:5px">
	            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">保存</a>
	        </div>
	        
        </div>
    </div>
  </body>
  
  
</html>
