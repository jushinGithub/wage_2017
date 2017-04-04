package com.zhugx.salary.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.zhugx.salary.dao.MonthSalaryDAO;
import com.zhugx.salary.dao.WageDAO;
import com.zhugx.salary.pojo.MonthSalary;
import com.zhugx.salary.pojo.OrgSalary;
import com.zhugx.salary.pojo.Wage;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.service.MonthSalaryService;
import com.zhugx.salary.tool.ZhugxUtils;

public class MonthSalaryServiceImpl implements MonthSalaryService {
	private MonthSalaryDAO monthSalaryDAO;
	private WageDAO wageDAO;

	public MonthSalaryDAO getMonthSalaryDAO() {
		return monthSalaryDAO;
	}
	@Resource
	public void setMonthSalaryDAO(MonthSalaryDAO monthSalaryDAO) {
		this.monthSalaryDAO = monthSalaryDAO;
	}
	
	public WageDAO getWageDAO() {
		return wageDAO;
	}
	@Resource
	public void setWageDAO(WageDAO wageDAO) {
		this.wageDAO = wageDAO;
	}
	
	@Override
	public void saveOrUpdateOrgSalary(List<OrgSalary> list) {
		this.monthSalaryDAO.saveOrUpdateOrgSalary(list);
	}
	public void saveOrUpdateOrgSalaryCallback(List<OrgSalary> list) {
		this.monthSalaryDAO.saveOrUpdateOrgSalaryCallback(list);
	}
	//===================================================================
	// 
	//===================================================================
	
	
	@Override
	public DataGrid getSalary(String month, String isPiece,String orgId) {
		List<MonthSalary> list =  this.monthSalaryDAO.getSalary(month, isPiece,orgId);
		
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(list.size());
		dataGrid.setRows(list);
		
		//增加合计行
		List lisFoot = new ArrayList();
		MonthSalary footer = new MonthSalary();
		footer.setEmName("合计");
		Double baseSalary  = 0.0;
		Double piecePay  = 0.0;
		Double realPay  = 0.0;
		Double time = 0.0;
		Double  grade = 0.0;
		Double  sumGrade = 0.0;
		
		Double f01 = 0.0;
		Double f02 = 0.0;
		Double f03 = 0.0;
		Double f04 = 0.0;
		Double f05 = 0.0;
		Double f06 = 0.0;
		Double f07 = 0.0;
		Double f08 = 0.0;
		Double f09 = 0.0;
		Double f10 = 0.0;
		Double f11 = 0.0;
		Double f12 = 0.0;
		Double f13 = 0.0;
		Double f14 = 0.0;
		Double f15 = 0.0;
		
		Double totalPay = 0.0;
		Double finalPay = 0.0;
		for(MonthSalary pt: list){
			time += pt.getTime()==null?0.0:pt.getTime();
			baseSalary = ZhugxUtils.addDouble(pt.getBaseSalary(), baseSalary);
			piecePay = ZhugxUtils.addDouble(pt.getPiecePay(), piecePay);
			realPay = ZhugxUtils.addDouble(pt.getRealPay(), realPay);
			grade = ZhugxUtils.addDouble(pt.getGrade(), grade);
			totalPay = ZhugxUtils.addDouble(pt.getTotalPay(), totalPay);
			finalPay = ZhugxUtils.addDouble(pt.getFinalPay(), finalPay);
			sumGrade = ZhugxUtils.addDouble(pt.getSumGrade(), sumGrade);
			
			
			f01 = ZhugxUtils.addDouble(pt.getF01(), f01);
			f02 = ZhugxUtils.addDouble(pt.getF02(), f02);
			f03 = ZhugxUtils.addDouble(pt.getF03(), f03);
			f04 = ZhugxUtils.addDouble(pt.getF04(), f04);
			f05 = ZhugxUtils.addDouble(pt.getF05(), f05);
			f06 = ZhugxUtils.addDouble(pt.getF06(), f06);
			f07 = ZhugxUtils.addDouble(pt.getF07(), f07);
			f08 = ZhugxUtils.addDouble(pt.getF08(), f08);
			f09 = ZhugxUtils.addDouble(pt.getF09(), f09);
			f10 = ZhugxUtils.addDouble(pt.getF10(), f10);
			f11 = ZhugxUtils.addDouble(pt.getF11(), f11);
			f12 = ZhugxUtils.addDouble(pt.getF12(), f12);
			f13 = ZhugxUtils.addDouble(pt.getF13(), f13);
			f14 = ZhugxUtils.addDouble(pt.getF14(), f14);
			f15 = ZhugxUtils.addDouble(pt.getF15(), f15);
		}
		footer.setBaseSalary(baseSalary);
		footer.setPiecePay(piecePay);
		footer.setRealPay(realPay);
		footer.setTime(time);
		footer.setGrade(grade);
		footer.setTotalPay(totalPay);
		footer.setFinalPay(finalPay);
		footer.setSumGrade(sumGrade);
		
		footer.setF01(f01);
		footer.setF02(f02);
		footer.setF03(f03);
		footer.setF04(f04);
		footer.setF05(f05);
		footer.setF06(f06);
		footer.setF07(f07);
		footer.setF08(f08);
		footer.setF09(f09);
		footer.setF10(f10);
		footer.setF11(f11);
		footer.setF12(f12);
		footer.setF13(f13);
		footer.setF14(f14);
		footer.setF15(f15);
		
		lisFoot.add(footer);
		dataGrid.setFooter(lisFoot);
		
		return dataGrid;
	}
	
	
	@Override
	public void saveOrUpdateSalary(List<MonthSalary> list, String isPiece) {
		this.monthSalaryDAO.saveOrUpdateSalary(list,isPiece);
	}
	
	
	@Override
	public List<Wage> initWage(String isPiece) {
		return this.wageDAO.initWage(isPiece);
	}
	
	@Override
	public void calculateSalary(String month) {
		this.monthSalaryDAO.calculateSalary(month);
	}
	
	@Override
	public DataGrid getOrgSalary(String shop, String month) {
		List<OrgSalary> list =  this.monthSalaryDAO.getOrgSalary(shop,month);
		
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(list.size());
		dataGrid.setRows(list);
		
		//增加合计行
		List lisFoot = new ArrayList();
		OrgSalary footer = new OrgSalary();
		footer.setOrgName("合计");
		Integer emSum  = 0;
		Double pieceSum  = 0.0;
		Double timeSum  = 0.0;
		Double pieceFinal  = 0.0;
		Double kxk  = 0.0;
		Double kzl  = 0.0;
		Double pyf  = 0.0;
		Double zjeOne  = 0.0;
		Double zjeTwo  = 0.0;
		
		for(OrgSalary pt: list){
			emSum += pt.getEmSum()==null?0:pt.getEmSum();
			
			pieceSum = ZhugxUtils.addDouble(pt.getPieceSum(), pieceSum);
			timeSum = ZhugxUtils.addDouble(pt.getTimeSum(), timeSum);
			pieceFinal = ZhugxUtils.addDouble(pt.getPieceFinal(), pieceFinal);
			kxk = ZhugxUtils.addDouble(pt.getKxk(), kxk);
			kzl = ZhugxUtils.addDouble(pt.getKzl(), kzl);
			pyf = ZhugxUtils.addDouble(pt.getPyf(), pyf);
			zjeOne = ZhugxUtils.addDouble(pt.getZjeOne(), zjeOne);
			zjeTwo = ZhugxUtils.addDouble(pt.getZjeTwo(), zjeTwo);
		}
		footer.setEmSum(emSum);
		footer.setPieceSum(pieceSum);
		footer.setTimeSum(timeSum);
		footer.setPieceFinal(pieceFinal);
		footer.setKxk(kxk);
		footer.setKzl(kzl);
		footer.setPyf(pyf);
		footer.setZjeOne(zjeOne);
		footer.setZjeTwo(zjeTwo);
		lisFoot.add(footer);
		dataGrid.setFooter(lisFoot);
		
		return dataGrid;
	}
	
	@Override
	public DataGrid getOrgSumSalary(String month, String shop, String isPiece) {
		List<MonthSalary> list =  this.monthSalaryDAO.getOrgSumSalary(month, shop, isPiece);
		
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(list.size());
		dataGrid.setRows(list);
		
		//增加合计行
		List lisFoot = new ArrayList();
		MonthSalary footer = new MonthSalary();
		footer.setEmName("合计");
		Double baseSalary  = 0.0;
		Double piecePay  = 0.0;
		Double realPay  = 0.0;
		Double time = 0.0;
		Double  grade = 0.0;
		Double  sumGrade = 0.0;
		
		Double f01 = 0.0;
		Double f02 = 0.0;
		Double f03 = 0.0;
		Double f04 = 0.0;
		Double f05 = 0.0;
		Double f06 = 0.0;
		Double f07 = 0.0;
		Double f08 = 0.0;
		Double f09 = 0.0;
		Double f10 = 0.0;
		Double f11 = 0.0;
		Double f12 = 0.0;
		Double f13 = 0.0;
		Double f14 = 0.0;
		Double f15 = 0.0;
		
		Double totalPay = 0.0;
		Double finalPay = 0.0;
		for(MonthSalary pt: list){
			time += pt.getTime()==null?0.0:pt.getTime();
			baseSalary = ZhugxUtils.addDouble(pt.getBaseSalary(), baseSalary);
			piecePay = ZhugxUtils.addDouble(pt.getPiecePay(), piecePay);
			realPay = ZhugxUtils.addDouble(pt.getRealPay(), realPay);
			grade = ZhugxUtils.addDouble(pt.getGrade(), grade);
			totalPay = ZhugxUtils.addDouble(pt.getTotalPay(), totalPay);
			finalPay = ZhugxUtils.addDouble(pt.getFinalPay(), finalPay); 
			sumGrade = ZhugxUtils.addDouble(pt.getSumGrade(), sumGrade); 
			
			f01 = ZhugxUtils.addDouble(pt.getF01(), f01);
			f02 = ZhugxUtils.addDouble(pt.getF02(), f02);
			f03 = ZhugxUtils.addDouble(pt.getF03(), f03);
			f04 = ZhugxUtils.addDouble(pt.getF04(), f04);
			f05 = ZhugxUtils.addDouble(pt.getF05(), f05);
			f06 = ZhugxUtils.addDouble(pt.getF06(), f06);
			f07 = ZhugxUtils.addDouble(pt.getF07(), f07);
			f08 = ZhugxUtils.addDouble(pt.getF08(), f08);
			f09 = ZhugxUtils.addDouble(pt.getF09(), f09);
			f10 = ZhugxUtils.addDouble(pt.getF10(), f10);
			f11 = ZhugxUtils.addDouble(pt.getF11(), f11);
			f12 = ZhugxUtils.addDouble(pt.getF12(), f12);
			f13 = ZhugxUtils.addDouble(pt.getF13(), f13);
			f14 = ZhugxUtils.addDouble(pt.getF14(), f14);
			f15 = ZhugxUtils.addDouble(pt.getF15(), f15);
		}
		footer.setBaseSalary(baseSalary);
		footer.setPiecePay(piecePay);
		footer.setRealPay(realPay);
		footer.setTime(time);
		footer.setGrade(grade);
		footer.setTotalPay(totalPay);
		footer.setFinalPay(finalPay);
		footer.setSumGrade(sumGrade);
		
		footer.setF01(f01);
		footer.setF02(f02);
		footer.setF03(f03);
		footer.setF04(f04);
		footer.setF05(f05);
		footer.setF06(f06);
		footer.setF07(f07);
		footer.setF08(f08);
		footer.setF09(f09);
		footer.setF10(f10);
		footer.setF11(f11);
		footer.setF12(f12);
		footer.setF13(f13);
		footer.setF14(f14);
		footer.setF15(f15);
		
		lisFoot.add(footer);
		dataGrid.setFooter(lisFoot);
		
		return dataGrid;
	}
	@Override
	public void calTimeSalary(String shop, String month) {
		this.monthSalaryDAO.calTimeSalary(shop, month);
		
	}

}
