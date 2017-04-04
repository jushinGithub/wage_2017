package com.zhugx.salary.service.impl;

import javax.annotation.Resource;

import com.zhugx.salary.dao.ConfigurationDAO;
import com.zhugx.salary.pojo.Configuration;
import com.zhugx.salary.service.ConfigurationService;

public class ConfigurationServiceImpl implements ConfigurationService {
	private ConfigurationDAO  configurationDAO;

	@Resource
	public void setConfigurationDAO(ConfigurationDAO configurationDAO) {
		this.configurationDAO = configurationDAO;
	}

	@Override
	public Configuration getConfig() {
		return this.configurationDAO.getConfig();
	}

	@Override
	public void saveConfiguration(Configuration config) {
		this.configurationDAO.saveConfiguration(config);
	}
	
	
}
