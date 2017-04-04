package com.zhugx.salary.service;

import java.util.List;

import com.zhugx.salary.pojo.OrgSalary;
import com.zhugx.salary.pojo.ProductPrice;
import com.zhugx.salary.pojo.common.DataGrid;


public interface ReportService {

	/**
	 * 工段计件工资测算表
	 * @param month
	 * @param orgId
	 * @param orgName
	 * @return
	 */
	DataGrid getReprotFive(String month, String orgId, String orgName);
	
	/**
	 * 获取工段工资  (报表 五)
	 * @param month
	 * @param orgId
	 * @return
	 */
	OrgSalary  getOrgSalaryDept(String month, String orgId);
	
	/**
	 * 获取 月度分析表
	 * @param month
	 * @return
	 */
	DataGrid getMonthAnalyze(String shop, String month);
	
}
