package com.zhugx.salary.dao.impl;

import java.util.List;

import com.zhugx.salary.dao.ConfigurationDAO;
import com.zhugx.salary.dao.common.JdbcBaseDAO;
import com.zhugx.salary.dao.common.SqlConstant;
import com.zhugx.salary.pojo.Configuration;

@SuppressWarnings("all")
public class ConfigurationDAOImpl implements ConfigurationDAO {
	private  JdbcBaseDAO jdbcTemplate;
	
	public JdbcBaseDAO getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcBaseDAO jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//===================================================================
	//logic method
	//===================================================================
	
	@Override
	public Configuration getConfig() {
		List<Configuration> list = this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG,Configuration.class);
		if(list == null || list.size() == 0){
			this.initConfig();
			list =  this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG,Configuration.class);
		}
		return list.get(0); 
	}

	@Override
	public void saveConfiguration(Configuration config) {
		//裁剪, 手工, 烫工, 工价比例  如果没有输入就设置成 GJ*0
		if(config.getCjgj() == null || config.getCjgj().trim().length() < 1){
			config.setCjgj("GJ*0");
		}
		if(config.getSggj() == null || config.getSggj().trim().length() < 1){
			config.setSggj("GJ*0");
		}
		if(config.getTggj() == null || config.getTggj().trim().length() < 1){
			config.setTggj("GJ*0");
		}
		
		//车工
		if(config.getCggj() == null || config.getCggj().trim().length() < 1){
			config.setCggj("GJ");
		}
		
		
		Object[] params = { config.getCgpz(), config.getCjpz(),config.getSgpz(),config.getTgpz(),
							config.getCggj(), config.getCjgj(),config.getSggj(),config.getTggj(),
		                    config.getDecimals(),config.getJssd(),config.getIp(),config.getCompany(), config.getUuid()};
		
		this.jdbcTemplate.getSimpleJdbcTemplate().update(SqlConstant.UPDATE_CONFIG, params);
		//如果修改了公司名, 那么把orgnize_base的公司名 也更改了
		if(!"".equals(config.getCompany()) && config.getCompany()!=null){
			String sqlOrgnize = "update organize_base t set t.org_name = ? where pid=-1";
			Object[] args = {config.getCompany()};
			this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlOrgnize, args);
		}
	}
	
	/** 初始化配置 */
	void initConfig(){
		Configuration config = new Configuration();
		config.setUuid(Math.random()*1000+"");
		config.setCgpz("车工完工数");
		config.setCggj("GJ+GYJ");
		
		config.setCjpz("车工完工数");
		config.setCjgj("GJ*0.105");
		
		config.setSgpz("车工完工数");
		config.setSggj("GJ*0.055");
		
		config.setTgpz("车工完工数");
		config.setTggj("GJ*0.005");
		
		config.setJssd("车工完工数");
		config.setDecimals(2);
		config.setIp("127.0.0.1");
		config.setCompany("xxx服装厂");
		Object[] params = {config.getUuid(), config.getCgpz(), config.getCjpz(),config.getSgpz(),config.getTgpz(),
				           					 config.getCggj(), config.getCjgj(),config.getSggj(),config.getTggj(),
				           config.getDecimals(),config.getJssd(),config.getIp()};
		
		this.jdbcTemplate.getSimpleJdbcTemplate().update(SqlConstant.ADD_CONFIG, params);
		
	}
	
}
