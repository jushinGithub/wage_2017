package com.zhugx.salary.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhugx.salary.dao.OrganizeDAO;
import com.zhugx.salary.pojo.Organize;
import com.zhugx.salary.service.OrganizeService;

@Service
public class OrganizeServiceImpl implements OrganizeService {
	private OrganizeDAO organizeDAO;

	@Resource
	public void setOrganizeDAO(OrganizeDAO organizeDAO) {
		this.organizeDAO = organizeDAO;
	}
	
	/**
	 * 通过上级id主键获取组织部门
	 * @param parentid
	 * @return
	 */
	public List<Organize> getOrgTreeById(String parentid){
		return this.organizeDAO.getOrgTreeById(parentid);
	}

	@Override
	public List<Organize> getShop() {
		return this.organizeDAO.getShop();
	}

	@Override
	public void addTimeOrg(String orgName,String orgId) {
		this.organizeDAO.addTimeOrg(orgName,orgId);
	}
	

}
