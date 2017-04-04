package com.zhugx.salary.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhugx.salary.dao.EmployeeDAO;
import com.zhugx.salary.pojo.Attendance;
import com.zhugx.salary.pojo.Employee;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Resource
	private EmployeeDAO employeeDAO;

	public void setEmployeeDAO(EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}
	
	@Override
	public DataGrid getEmployeeByOrgId(String orgId,String containAll) {
		List<Employee>  ems =  this.employeeDAO.getEmployeeByOrgId(orgId,containAll);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(ems.size());
		dataGrid.setRows(ems);
		
		return dataGrid;
	}

	@Override
	public List<String> saveOrUpdateEmployee(List<Employee> employees) {
		return this.employeeDAO.saveOrUpdateEmployee(employees);
	}

	@Override
	public void removeEmployee(List<Employee> employees) {
		 this.employeeDAO.removeEmployee(employees);
	}

	@Override
	public void removeHistoryEmployee(List<Employee> employees) {
		 this.employeeDAO.removeHistoryEmployee(employees);
	}

	@Override
	public DataGrid getAttendace(String orgId, String month) {
		List<Attendance>  ems =  this.employeeDAO.getAttendace(orgId, month);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(ems.size());
		dataGrid.setRows(ems);
		
		return dataGrid;
	}
	
}
