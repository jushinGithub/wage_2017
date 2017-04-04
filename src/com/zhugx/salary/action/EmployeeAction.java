package com.zhugx.salary.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.zhugx.salary.pojo.Employee;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.service.EmployeeService;

@SuppressWarnings("all")
@Controller
public class EmployeeAction extends ActionSupport {
	private String emArray;
	private DataGrid dataGrid;
	private String message;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getEmArray() {
		return emArray;
	}
	public void setEmArray(String emArray) {
		this.emArray = emArray;
	}

	@Resource
	private EmployeeService employeeService;
	
	public DataGrid getDataGrid() {
		return dataGrid;
	}
	public void setDataGrid(DataGrid dataGrid) {
		this.dataGrid = dataGrid;
	}
	
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	//=====================================================
	// logic method
	//=====================================================
	
	/**
	 * 通过组织部门id获取对应的员工
	 * @return
	 */
	public String getEmployeeByOrgId() {
		HttpServletRequest  request = ServletActionContext.getRequest();
		String  orgId = request.getParameter("orgId");
		String containAll = request.getParameter("containAll");
		dataGrid = this.employeeService.getEmployeeByOrgId(orgId,containAll);
		return SUCCESS;
	}
	
	/**保存*/
    public String  saveOrUpdateEmployee(){
    	HttpServletRequest  request = ServletActionContext.getRequest();
    	String temp = request.getParameter("emArray");
    	JSONArray ja = JSONArray.fromObject(emArray);
    	List list = JSONArray.toList(ja, Employee.class);
    	this.employeeService.saveOrUpdateEmployee(list);
    	message = SUCCESS;
    	return SUCCESS;
    	
    }
    
    /**删除*/
    public String removeEmployee(){
    	JSONArray ja = JSONArray.fromObject(emArray);
    	List list = JSONArray.toList(ja, Employee.class);
    	this.employeeService.removeEmployee(list);
    	message = SUCCESS;
    	return SUCCESS;
    }
    
    /** 删除历史数据, 物理删除  保留最近两年  所有员工相关的数据都删除*/
    public String removeHistoryEmployee(){
    	JSONArray ja = JSONArray.fromObject(emArray);
    	List list = JSONArray.toList(ja, Employee.class);
    	this.employeeService.removeHistoryEmployee(list);
    	message = SUCCESS;
    	return SUCCESS;
    }
    
    /**
     * 获取员工信息(考勤表)
     * @return
     */
    public String getAttendace(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String orgId = request.getParameter("orgId");
		String month = request.getParameter("month");
		
		dataGrid = this.employeeService.getAttendace(orgId,month);
		return SUCCESS;
	}
	
}
