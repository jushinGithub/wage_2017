package com.zhugx.salary.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.zhugx.salary.dao.WageDAO;
import com.zhugx.salary.pojo.Wage;
import com.zhugx.salary.service.WageService;

public class WageServiceImpl implements WageService {
	private WageDAO wageDAO;

	@Resource
	public void setWageDAO(WageDAO wageDAO) {
		this.wageDAO = wageDAO;
	}

	@Override
	public List<Wage> getWage() {
		return this.wageDAO.getWage();
	}

	@Override
	public void saveWage(List<Wage> list) {
		this.wageDAO.saveWage(list);
	}
	
	
}
