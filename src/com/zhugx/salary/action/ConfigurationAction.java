package com.zhugx.salary.action;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.zhugx.salary.pojo.Configuration;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.service.ConfigurationService;

public class ConfigurationAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private JSONObject jsonConfig;
	Configuration configuration;
	
	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	private ConfigurationService configurationService;
	private DataGrid dataGrid;
	
	public DataGrid getDataGrid() {
		return dataGrid;
	}
	public void setDataGrid(DataGrid dataGrid) {
		this.dataGrid = dataGrid;
	}
	public JSONObject getJsonConfig() {
		return jsonConfig;
	}
	public void setJsonConfig(JSONObject jsonConfig) {
		this.jsonConfig = jsonConfig;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}
	@Resource
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	
	//===================================================================
	// logic method
	//===================================================================
	/** 获取配置　*/
	public String getMyConfig(){
		Configuration config = this.configurationService.getConfig();
		jsonConfig = JSONObject.fromObject(config);  
		return SUCCESS;
	}
	
	/**  保存配置 */
	public String saveMyConfig(){
		this.configurationService.saveConfiguration(configuration);
		return SUCCESS;
	}
	
	
}
