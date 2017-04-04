package com.zhugx.salary.action;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import com.opensymphony.xwork2.ActionSupport;
import com.zhugx.salary.pojo.Wage;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.service.WageService;

@SuppressWarnings("all")
public class WageAction extends ActionSupport{
	private WageService wageService;
	private DataGrid dataGrid;
	private String message;
	private String emArray;
	
	public String getEmArray() {
		return emArray;
	}
	public void setEmArray(String emArray) {
		this.emArray = emArray;
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
	@Resource
	public void setWageService(WageService wageService) {
		this.wageService = wageService;
	}
	//=========================================================================
	//  logic method
	//=========================================================================
	
	/** 保存修改的工资项 */
	public String saveWage(){
		JSONArray ja = JSONArray.fromObject(emArray);
		List list = JSONArray.toList(ja, Wage.class);
    	this.wageService.saveWage(list);
		message = SUCCESS;
		return SUCCESS;
	}
	
	/**
	 * 获取所有的工资项
	 * @return
	 */
	public String getWage(){
		List<Wage> list = this.wageService.getWage();
		dataGrid = new DataGrid();
		dataGrid.setTotal(list.size());
		dataGrid.setRows(list);
		
		return SUCCESS;
	}
	
}
