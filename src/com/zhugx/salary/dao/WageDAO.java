package com.zhugx.salary.dao;

import java.util.List;

import com.zhugx.salary.pojo.Wage;

public interface WageDAO{

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
	
	/**
	 * 初始化工资项表头, 用户计件和计时工资的时候, 工资项的表头
	 * @param isPiece
	 * @return
	 */
	List<Wage> initWage(String isPiece);
}
