package com.zhugx.salary.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.zhugx.salary.pojo.MonthAnalyze;
import com.zhugx.salary.pojo.OrgSalary;
import com.zhugx.salary.pojo.ProductPrice;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.service.ReportService;
import com.zhugx.salary.tool.ZhugxUtils;

@SuppressWarnings("all")
public class ReportAction extends ActionSupport {
	private String dataArray;
	private String message;
	private DataGrid dataGrid;
	private ReportService reportService;
	
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
	public ReportService getReportService() {
		return reportService;
	}
	@Resource
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	//===========================================================================
	// logic method 
	//===========================================================================
	/** 获取  月工资分析表 */
	public String getMonthAnalyze(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String shop = request.getParameter("shop");
		
		dataGrid = this.reportService.getMonthAnalyze(shop,month);
		return SUCCESS;
	}
	
	
	/** 报表五  工段计件工资测算表 */
	public String getReportFive(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String orgId = request.getParameter("orgId");
		String orgName = request.getParameter("orgName");
		dataGrid = this.reportService.getReprotFive(month, orgId, orgName);
		return SUCCESS;
	}
	
}
