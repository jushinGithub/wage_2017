package com.zhugx.salary.dao;

import java.util.List;

import com.zhugx.salary.pojo.MonthAnalyze;
import com.zhugx.salary.pojo.MonthSalary;
import com.zhugx.salary.pojo.OrgSalary;
import com.zhugx.salary.pojo.common.DataGrid;

public interface MonthSalaryDAO  {
	
	//1.初始化  工段和员工 工资
	/**
	 *  初始化 工段工资
	 * @param month
	 */
	void  initOrgSalary(String shop,String month);
	
	/**
	 * 初始化 员工工资 1.计件工资 前提是要有 工时和工价, 2.计时工资
	 * @param month
	 */
	void initSalary(String shop, String month);
	
	//2.员工工资
	/**
	 * 获取 员工工资
	 * @param month
	 * @return
	 */
	List<MonthSalary> getSalary(String month, String isPiece,String orgId);
	
	/**
	 *  获取 计件 计时 工资  按班组orgId汇总
	 * @param month
	 * @param isPiece
	 * @return
	 */
	List<MonthSalary> getOrgSumSalary(String month, String shop, String isPiece);
	
	/**
	 * 保存 员工工资
	 */
	void saveOrUpdateSalary(List<MonthSalary> list, String isPiece);
	
	/**
	 *  计算本月工资
	 * @param month
	 * @return
	 */
	void calculateSalary(String month);
	
	
	//3.工段工资
	/**
	 * 获取 工段工资
	 * @param month
	 * @return
	 */
	List<OrgSalary> getOrgSalary(String shop, String month);
	
	/**
	 * 获取 具体 某一个部门的  工段工资
	 * @param month
	 * @param orgId
	 * @return
	 */
	OrgSalary  getOrgSalaryDept(String month, String orgId);
	
	/**
	 * 保存 工段工资 
	 */
	void saveOrUpdateOrgSalary(List<OrgSalary> list);
	void saveOrUpdateOrgSalaryCallback(List<OrgSalary> list);
	
	
	//4.前提条件的数据发生更改以后, 需要重新调整工资
	/**
	 * 修改了[生产数据的数量 ]以后, 需要修改 车工段的工资和工时分价
	 * @param month
	 */
	void renewalOrgSalaryPrice(String shop,String month);
	
	/**
	 * 修改了[工时分 ]以后,需要修改 车工段的工资和工时分价
	 * @param month
	 */
	void renewalOrgSalaryTime(String shop,String month);
	
	/**
	 * 修改了[工段工资 ]以后 更新计件工资
	 * @param month
	 */
	void renewalSalary(String shop, String month);
	
	//5.报表  
	/**
	 * 月度工资分析表
	 * @param month
	 * @return
	 */
	List<MonthAnalyze> getMonthAnalyze(String shop,String month);
	
	/**
	 * 重新获取基本工资
	 * @param month
	 * @return
	 */
	void calTimeSalary(String shop, String month);
}
