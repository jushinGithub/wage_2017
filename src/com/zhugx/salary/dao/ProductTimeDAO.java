package com.zhugx.salary.dao;

import java.util.List;

import com.zhugx.salary.pojo.ProductTime;

public interface ProductTimeDAO {
	
	/**
	 * 初始化 计件员工的公分值 
	 * @param month
	 */
	void initProductTime(String shop,String month);
	/**
	 * 获取 件员工的公分值 
	 * @param month
	 * @param team
	 * @return
	 */
	List<ProductTime> getProductTime(String month,String team);
	/**
	 * 保存 公分值 
	 */
	void saveOrUpdateProductTime(List<ProductTime> list);
	
	
	/**
	 * 保存 公分值 
	 */
	String reLoadTime(String month,String orgId);
	
	/**
	 * 删除 公分值 
	 */
	boolean delTime(List<ProductTime> list);
}
