package com.zhugx.salary.service;

import java.util.List;

import com.zhugx.salary.pojo.ProductData;
import com.zhugx.salary.pojo.ProductPrice;
import com.zhugx.salary.pojo.ProductTime;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.pojo.common.DataGridProductData;
import com.zhugx.salary.webservice.CheGong;
import com.zhugx.salary.webservice.Others;

public interface ProductService {

	//1. produdct_data
	/**
	 * 初始化 生产数据
	 * @param month
	 */
	void initProductData(String shop, String month,List<CheGong> listCg, List<Others> listQt);
	void initProductDataCallback(String shop, String month,List<CheGong> listCg, List<Others> listQt);
	
	/**
	 * 重新获取 生产数据
	 * @param month
	 */
	void reloadProductData(String shop,String month,List<CheGong> listCg, List<Others> listQt);
	
	/**
	 * 通过月份获取车工生产数据
	 * @param month
	 * @return
	 */
	DataGridProductData getProductDataByMonth(String shop,String month,boolean isCg);
	
	/**
	 * 获取其他工段的生产数据
	 * @param shop
	 * @param month
	 * @return
	 */
	DataGrid getProductQtData(String shop,String month);
	
	/**
	 * 保存生产属数据
	 */
	void saveOrUpdateProductData(List<ProductData> list);
	void saveOrUpdateProductDataCallback(String shop, String month);
	
	/**
	 * 保存生产属数据  裁剪 手工 烫工数量
	 */
	void saveOrUpdateProductDataQt(List<ProductData> list);
	void saveOrUpdateProductDataQtCallback(List<ProductData> list);
	
	//2. produdct_time
	/**
	 * 获取 件员工的公分值 
	 * @param month
	 * @param team
	 * @return
	 */
	DataGrid getProductTime(String month,String team);
	/**
	 * 保存 公分值 
	 */
	void saveOrUpdateProductTime(List<ProductTime> list);
	void saveOrUpdateProductTimeCallback(List<ProductTime> list);
	
	/**
	 * 保存 公分值 
	 */
	void reLoadTime(String month,String orgId);
	
	/**
	 * 删除 公分值 
	 */
	void delTime(List<ProductTime> list);
	
	
	//3. product_price
	/**
	 * 获取工价
	 * @param month
	 * @return
	 */
	DataGrid getProductPrice(String shop, String month);
	/**
	 * 保存 工价
	 */
	void saveOrUpdateProductPrice(List<ProductPrice> list);
	void saveOrUpdateProductPriceCallback(List<ProductPrice> list);
	
	/**
	 * 使用上个月工价
	 * @param month
	 */
	void userLastMonthPrice(String shop, String month);
	
	/**
	 * 获取所有的组名, 用于构造动态表头
	 * @return
	 */
	List<String> getGroupName(String shop,String month);
	
	/**
	 * 获取所有的组名, 用于构造动态表头
	 * @return
	 */
	List<String> getGroupNo(String shop);
	
	/**
	 * 清空本月数据
	 * @param shop
	 * @param month
	 */
	void cleanData(String shop,String month);
	
	
	
	
}
