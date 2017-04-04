package com.zhugx.salary.dao;

import com.zhugx.salary.pojo.Configuration;

public interface ConfigurationDAO  {

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
