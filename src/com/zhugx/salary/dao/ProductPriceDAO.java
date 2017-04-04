package com.zhugx.salary.dao;

import java.util.List;

import com.zhugx.salary.pojo.ProductData;
import com.zhugx.salary.pojo.ProductPrice;

public interface ProductPriceDAO {

	/**
	 * 初始化 工价
	 * @param month
	 */
	void initProductPrice(String shop, String month);
	
	/**
	 * 获取工价
	 * @param month
	 * @return
	 */
	List<ProductPrice> getProductPrice(String shop, String month);
	/**
	 * 保存 工价
	 */
	void saveOrUpdateProductPrice(List<ProductPrice> list);
	
	/**
	 * 获取上一次的做工价的最大月份
	 * @param month
	 * @return
	 */
	String getLastMonth(String month);
	
	/**
	 * 当生产数据发生变化的时候, 更新工价表的数量.
	 * @param month
	 */
	void renewalProductPrice(String shop, String month);
	
	
	/**
	 * (报表)工段计件工资测算表
	 * @param month
	 * @param orgId
	 * @param orgName
	 * @return
	 */
	List<ProductPrice> getReprotFive(String month, String orgId, String orgName);
	
	
	/**
	 * 删除 工价表的 货物
	 */
	void deleteProductPriceItem(List<String> list, String shop,String month);
	
	/**
	 * 新增 工价表的 货物
	 */
	void addProductPrice(List<ProductData> list,String shop, String month);
	
	
	/**
	 * 清空本月数据
	 * @param shop
	 * @param month
	 */
	void cleanData(String shop,String month);
	
}
