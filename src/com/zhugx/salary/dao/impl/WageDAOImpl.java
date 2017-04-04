package com.zhugx.salary.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.zhugx.salary.dao.WageDAO;
import com.zhugx.salary.dao.common.JdbcBaseDAO;
import com.zhugx.salary.dao.common.SqlConstant;
import com.zhugx.salary.pojo.Wage;

@SuppressWarnings("all")
public class WageDAOImpl implements WageDAO {
	private  JdbcBaseDAO jdbcTemplate;
	
	public JdbcBaseDAO getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcBaseDAO jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Wage> getWage() {
		String sql = "select * From Wage order by wage_No";
		List<Wage> list = this.jdbcTemplate.queryForList(sql,Wage.class);
		if(list == null || list.size() == 0){
			this.initWage();
			list = this.jdbcTemplate.queryForList(sql,Wage.class);
		}
		return list;
	}

	@Override
	public void saveWage(List<Wage> list) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Wage temp: list){
			Object[] param = {temp.getWageNo(),temp.getWageName(),temp.getSymbol(),temp.getIsPiece(),temp.getIsTime(),temp.getDefaulteValue(),temp.getUuid()};
			batchArgs.add(param);
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.UPDATE_WAGE, batchArgs);
	}

	@Override
	public List<Wage> initWage(String isPiece) {
		String hql = null ; 
		if("√".equals(isPiece)){
			hql = "select * From Wage where is_Piece = '√' ORDER BY symbol DESC";
		}else{
			hql = "select * From Wage where is_Time = '√'  ORDER BY symbol DESC";
		}
		return this.jdbcTemplate.queryForList(hql, Wage.class);
	}
	
	/**
	 * 初始化工资项
	 */
	public void initWage(){
		Wage wage1 = new Wage();
		wage1.setUuid("1");
		wage1.setWageNo("F01");
		wage1.setWageName("车贴");
		wage1.setSymbol("1");
		wage1.setIsTime("√");
		
		Wage wage2 = new Wage();
		wage2.setUuid("2");
		wage2.setWageNo("F02");
		wage2.setWageName("餐补");
		wage2.setSymbol("1");
		wage2.setIsTime("√");
		
		Wage wage3 = new Wage();
		wage3.setUuid("3");
		wage3.setWageNo("F03");
		wage3.setWageName("组长津贴");
		wage3.setSymbol("1");
		wage3.setIsPiece("√");

		Wage wage4 = new Wage();
		wage4.setUuid("4");
		wage4.setWageNo("F04");
		wage4.setWageName("全勤奖");
		wage4.setSymbol("1");
		wage4.setIsTime("√");
		
		Wage wage5 = new Wage();
		wage5.setUuid("5");
		wage5.setWageNo("F05");
		wage5.setWageName("加班费");
		wage5.setSymbol("1");
		wage4.setIsTime("√");
		
		Wage wage6 = new Wage();
		wage6.setUuid("6");
		wage6.setWageNo("F06");
		wage6.setWageName("押金");
		wage6.setSymbol("1");
		wage6.setIsPiece("√");
		
		Wage wage7 = new Wage();
		wage7.setUuid("7");
		wage7.setWageNo("F07");
		wage7.setWageName("补工资");
		wage7.setSymbol("1");
		wage7.setIsPiece("√");
		
		Wage wage8 = new Wage();
		wage8.setUuid("8");
		wage8.setWageNo("F08");
		wage8.setWageName("其他补贴");
		wage8.setSymbol("0");
		wage8.setIsPiece("√");
		
		Wage wage9 = new Wage();
		wage9.setUuid("9");
		wage9.setWageNo("F09");
		wage9.setWageName("补贴");
		wage9.setSymbol("1");
		wage9.setIsPiece("√");
		
		Wage wage10 = new Wage();
		wage10.setUuid("10");
		wage10.setWageNo("F10");
		wage10.setWageName("其他");
		wage10.setSymbol("1");
		wage10.setIsPiece("√");
		
		Wage wage11 = new Wage();
		wage11.setUuid("11");
		wage11.setWageNo("F11");
		wage11.setWageName("缺勤");
		wage11.setSymbol("0");
		
		Wage wage12 = new Wage();
		wage12.setUuid("12");
		wage12.setWageNo("F12");
		wage12.setWageName("违纪");
		wage12.setSymbol("0");
		wage12.setIsPiece("√");
		wage12.setIsTime("√");
		
		Wage wage13 = new Wage();
		wage13.setUuid("13");
		wage13.setWageNo("F13");
		wage13.setWageName("质量");
		wage13.setSymbol("0");
		wage13.setIsPiece("√");
		
		Wage wage14 = new Wage();
		wage14.setUuid("14");
		wage14.setWageNo("F14");
		wage14.setWageName("迟到");
		wage14.setSymbol("0");
		
		Wage wage15 = new Wage();
		wage15.setUuid("15");
		wage15.setWageNo("F15");
		wage15.setWageName("奖金");
		wage15.setSymbol("1");
		wage15.setIsPiece("√");
		
		Wage wage16 = new Wage();
		wage16.setUuid("16");
		wage16.setWageNo("F16");
		wage16.setWageName("职责");
		wage16.setSymbol("0");
		
		List<Wage> list = new ArrayList();
		list.add(wage1);list.add(wage2);list.add(wage3);list.add(wage4);
		list.add(wage5);list.add(wage6);list.add(wage7);list.add(wage8);
		list.add(wage9);list.add(wage10);list.add(wage11);list.add(wage12);
		list.add(wage13);list.add(wage14);list.add(wage15);list.add(wage16);
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(int j=0; j<list.size(); j++){
			Wage temp = list.get(j);
			Object[] param = {temp.getUuid(),temp.getWageNo(),temp.getWageName(),temp.getSymbol(),temp.getIsPiece(),temp.getIsTime(),temp.getDefaulteValue()};
			batchArgs.add(param);
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_WAGE, batchArgs);
	}

}
