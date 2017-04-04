package com.zhugx.salary.dao;

import java.util.List;

import com.zhugx.salary.pojo.ProductData;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.pojo.common.DataGridProductData;

public interface ProductDataDAO {

	/**
	 * 初始化 生产数据
	 * @param month
	 */
	void initProductData(List<ProductData> listCg,List<ProductData> listQt,String shop, String month);
	
	/**
	 * 通过月份获取车工生产数据
	 * @param month
	 * @return
	 */
	List<ProductData> getProductDataByMonth(String month,String shop);
	
	/**
	 * 获取其他工段的生产数据
	 * @param shop
	 * @param month
	 * @return
	 */
	List<ProductData> getProductQtData(String shop,String month);
	
	/**
	 * 获取车工生产数据  把数据转换成   按小组行转列的动态格式
	 * @param listData
	 * @return
	 */
	DataGridProductData getMapData(String shop, String month,boolean isCg);
	
	/**
	 * 保存生产属数据
	 */
	void saveOrUpdateProductData(List<ProductData> list);
	
	/**
	 * 保存生产属数据  裁剪 手工 烫工
	 */
	void saveOrUpdateProductDataQt(List<ProductData> list);
	
	/**
	 * 获取本月所有的货物
	 * @param month
	 * @return
	 */
	List<String> getExsitItem(String shop, String month);
}
