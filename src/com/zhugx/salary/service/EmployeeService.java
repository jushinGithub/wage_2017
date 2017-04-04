package com.zhugx.salary.service;

import java.util.List;

import com.zhugx.salary.pojo.Employee;
import com.zhugx.salary.pojo.common.DataGrid;

public interface EmployeeService {

	/**
	 * 通过部门id获取员工
	 * @param orgId
	 * @return
	 */
	DataGrid getEmployeeByOrgId(String orgId,String containAll);
	
	/**
	 * 获取考勤员工信息
	 * @param orgId
	 * @return
	 */
	DataGrid getAttendace(String orgId, String month);
	
	/**
	 * 保存
	 * @return
	 */
	List<String> saveOrUpdateEmployee(List<Employee> employees);
	
	/**
	 * 状态设置为 离职, 不直接删除
	 * @return
	 */
	void removeEmployee(List<Employee> employees);
	
	/**
	 * 删除历史数据, 物理删除  保留最近两年  所有员工相关的数据都删除
	 * @return
	 */
	void removeHistoryEmployee(List<Employee> employees);
}
