package com.zhugx.salary.service;

import com.zhugx.salary.pojo.Configuration;

public interface ConfigurationService {

	/**
	 * 获取所有的配置项
	 * @return
	 */
	Configuration getConfig();
	
	/**
	 * 保存配置项
	 * @param config
	 */
	void saveConfiguration(Configuration config);
}
