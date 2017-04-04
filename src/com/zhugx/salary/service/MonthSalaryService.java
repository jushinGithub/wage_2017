package com.zhugx.salary.service;

import java.util.List;

import com.zhugx.salary.pojo.MonthSalary;
import com.zhugx.salary.pojo.OrgSalary;
import com.zhugx.salary.pojo.Wage;
import com.zhugx.salary.pojo.common.DataGrid;

public interface MonthSalaryService {

	/**
	 * 获取 员工工资
	 * @param month
	 * @return
	 */
	DataGrid getSalary(String month, String isPiece,String orgId);
	
	/**
	 *  获取 计件 计时 工资  按班组orgId汇总
	 * @param month
	 * @param isPiece
	 * @return
	 */
	DataGrid getOrgSumSalary(String month, String shop, String isPiece);
	
	/**
	 * 保存 员工工资
	 */
	void saveOrUpdateSalary(List<MonthSalary> list, String isPiece);
	
	/**
	 * 初始化工资项, 工资页面显示的工资项目
	 * @param isPiece
	 * @return
	 */
	List<Wage> initWage(String isPiece);
	
	/**
	 *  计算本月工资
	 * @param month
	 * @return
	 */
	void calculateSalary(String month);
	
	//=================================工段工工资======================
	/**
	 * 获取 工段  工资
	 * @param month
	 * @return
	 */
	DataGrid getOrgSalary(String shop, String month);
	
	/**
	 * 重新获取基本工资
	 * @param month
	 * @return
	 */
	void calTimeSalary(String shop, String month);
	
	/**
	 * 保存工段  工资
	 */
	void saveOrUpdateOrgSalary(List<OrgSalary> list);
	void saveOrUpdateOrgSalaryCallback(List<OrgSalary> list);
	
}
