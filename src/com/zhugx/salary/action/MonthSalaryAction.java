package com.zhugx.salary.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.zhugx.salary.pojo.MonthSalary;
import com.zhugx.salary.pojo.OrgSalary;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.service.MonthSalaryService;
import com.zhugx.salary.tool.ZhugxUtils;

@SuppressWarnings("all")
public class MonthSalaryAction extends ActionSupport {
	private String dataArray;
	private String message;
	private DataGrid dataGrid;
	private MonthSalaryService monthSalaryService;
	
	public String getDataArray() {
		return dataArray;
	}
	public void setDataArray(String dataArray) {
		this.dataArray = dataArray;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public DataGrid getDataGrid() {
		return dataGrid;
	}
	public void setDataGrid(DataGrid dataGrid) {
		this.dataGrid = dataGrid;
	}
	
	public MonthSalaryService getMonthSalaryService() {
		return monthSalaryService;
	}
	@Resource
	public void setMonthSalaryService(MonthSalaryService monthSalaryService) {
		this.monthSalaryService = monthSalaryService;
	}
	
	//===========================================================================
	// logic method 
	//===========================================================================
	/** 获取 计件 计时 工资 */
	public String getSalary(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String isPiece = request.getParameter("isPiece");
		String orgId = request.getParameter("orgId");
		
		dataGrid = this.monthSalaryService.getSalary(month, isPiece,orgId);
		return SUCCESS;
	}
	
	/** 重新获取基本工资 */
	public String calTimeSalary(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String orgId = request.getParameter("orgId");
		this.monthSalaryService.calTimeSalary(orgId,month);
		
		message = SUCCESS;
		return SUCCESS;
	}
	
	/** 获取 计件 计时 工资  按班组orgId汇总 */
	public String getOrgSumSalary(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String isPiece = request.getParameter("isPiece");
		String shop = request.getParameter("shop");
		
		dataGrid = this.monthSalaryService.getOrgSumSalary(month, shop,isPiece);
		return SUCCESS;
	}
	
	/** 保存  计件 计时 工资 */
	public String saveOrUpdateSalary(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String isPiece = request.getParameter("isPiece");
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, MonthSalary.class);
    	this.monthSalaryService.saveOrUpdateSalary(list,isPiece);
    	message = SUCCESS;
		return SUCCESS;
	}
	
	/**
	 * 获取工资项目, 加项和减项, 用于计件 计时工资页面的 动态表头
	 * @return
	 */
	public String initWage(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String isPiece = request.getParameter("isPiece");
		List list = this.monthSalaryService.initWage(isPiece);
		dataGrid = new DataGrid();
		dataGrid.setTotal(list.size());
		dataGrid.setRows(list);
		return SUCCESS;
		
	}
	
	/** 计算本月工资 */
	public String calculateSalary(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		this.monthSalaryService.calculateSalary(month);
		message = SUCCESS;
		return SUCCESS;
	}
	
	
	//===============================工段工资=========================
	/**　获取工段工资 */
	public String getOrgSalary(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String shop = request.getParameter("shop");
		
		dataGrid = this.monthSalaryService.getOrgSalary(shop,month);
		return SUCCESS;
	}
	
	
	/** 保存工段工资 */
	public String saveOrUpdateOrgSalary(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, OrgSalary.class);
    	
    	this.monthSalaryService.saveOrUpdateOrgSalary(list);
    	message = SUCCESS;
		return SUCCESS;
	}
	public String saveOrUpdateOrgSalaryCallback(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, OrgSalary.class);
    	
    	this.monthSalaryService.saveOrUpdateOrgSalaryCallback(list);
    	message = SUCCESS;
		return SUCCESS;
	}
	
}
