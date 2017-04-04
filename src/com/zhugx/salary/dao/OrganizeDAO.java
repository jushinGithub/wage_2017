package com.zhugx.salary.dao;

import java.util.List;
import java.util.Map;

import com.zhugx.salary.pojo.Organize;

public interface OrganizeDAO {
	
	/**
	 * 通过上级id主键获取组织部门
	 * @param parentid
	 * @return
	 */
	List<Organize> getOrgTreeById(String parentid);
	
	/**
	 * 获取车间
	 * @return
	 */
	List<Organize> getShop();
	
	/**
	 * 获取所有的组名, 用于构造动态表头(本月要是没有数据就不用了)
	 * @return
	 */
	List<String> getGroupNameOrigin(String shop,String month);
	
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
	 * 根据车间号获取 车工组的uuid orgId
	 * @param shop
	 * @return
	 */
	Map<String, String> getCgOrgid(String shop);
	
	
	/**
	 * 增加计时部门
	 * @param orgName
	 */
	void addTimeOrg(String orgName,String orgId);

}
