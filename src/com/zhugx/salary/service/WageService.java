package com.zhugx.salary.service;

import java.util.List;

import com.zhugx.salary.pojo.Wage;

public interface WageService {

	/**
	 * 获取所有的工资项
	 * @return
	 */
	List<Wage> getWage();
	
	/**
	 * 保存修改的工资项
	 * @param list
	 */
	void saveWage(List<Wage> list);
}
