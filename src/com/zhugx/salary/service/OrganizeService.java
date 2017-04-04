package com.zhugx.salary.service;

import java.util.List;

import com.zhugx.salary.pojo.Organize;

public interface OrganizeService {
	
	
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
	 * 增加计时部门
	 * @param orgName
	 */
	void addTimeOrg(String orgName,String orgId);

}
