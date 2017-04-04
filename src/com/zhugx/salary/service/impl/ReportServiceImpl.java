package com.zhugx.salary.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.zhugx.salary.dao.MonthSalaryDAO;
import com.zhugx.salary.dao.ProductPriceDAO;
import com.zhugx.salary.pojo.MonthAnalyze;
import com.zhugx.salary.pojo.OrgSalary;
import com.zhugx.salary.pojo.ProductPrice;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.service.ReportService;
import com.zhugx.salary.tool.ZhugxUtils;

public class ReportServiceImpl implements ReportService {
	private ProductPriceDAO productPriceDAO;
	private MonthSalaryDAO monthSalaryDAO;
	
	
	public ProductPriceDAO getProductPriceDAO() {
		return productPriceDAO;
	}
	@Resource
	public void setProductPriceDAO(ProductPriceDAO productPriceDAO) {
		this.productPriceDAO = productPriceDAO;
	}
	public MonthSalaryDAO getMonthSalaryDAO() {
		return monthSalaryDAO;
	}
	@Resource
	public void setMonthSalaryDAO(MonthSalaryDAO monthSalaryDAO) {
		this.monthSalaryDAO = monthSalaryDAO;
	}
	
	//===================================================================
	// logic method
	//===================================================================
	
	
	@Override
	public DataGrid getReprotFive(String month, String orgId,String orgName) {
		DataGrid dataGrid = new DataGrid();
		List<ProductPrice> list = this.productPriceDAO.getReprotFive(month, orgId, orgName);
		
		if(list != null && list.size() > 0){
			dataGrid.setTotal(list.size());
			dataGrid.setRows(list);
			List footer = new ArrayList();
			Integer sl = 0;
			Double  gz = 0.0;
			for(ProductPrice pp: list){
				sl += pp.getCgsl();
				gz = ZhugxUtils.addDouble(pp.getPiecePay(), gz);
			}
			ProductPrice p1 = new ProductPrice();
			p1.setItemName("计件合计");
			p1.setCgsl(sl);
			p1.setPiecePay(gz);
			ProductPrice p2 = new ProductPrice();
			p2.setItemName("合计圆整");
			//p2.setPiecePay(Math.floor(gz));//四舍五入
			p2.setPiecePay((double)Math.round(gz));//四舍五入
			
			OrgSalary orgSalary = this.getOrgSalaryDept(month, orgId);
			
			ProductPrice p4 = new ProductPrice();
			p4.setItemName("扣线款");
			p4.setPiecePay(orgSalary.getKxk());
			
			ProductPrice p5 = new ProductPrice();
			p5.setItemName("扣质量");
			p5.setPiecePay(orgSalary.getKzl());
			
			ProductPrice p6 = new ProductPrice();
			p6.setItemName("赔衣服");
			p6.setPiecePay(orgSalary.getPyf());
			
			ProductPrice p71 = new ProductPrice();
			p71.setItemName("增减额①");
			p71.setPiecePay(orgSalary.getZjeOne());
			ProductPrice p72 = new ProductPrice();
			p72.setItemName("增减额②");
			p72.setPiecePay(orgSalary.getZjeTwo());
			
			ProductPrice p8 = new ProductPrice();
			p8.setItemName("实际工资");
			p8.setPiecePay((double)Math.round(gz-orgSalary.getKxk()-orgSalary.getKzl()-orgSalary.getPyf()+
					orgSalary.getZjeOne()+orgSalary.getZjeTwo()));
			
			footer.add(p1);footer.add(p2);footer.add(p4);
			footer.add(p5);footer.add(p6);footer.add(p71);footer.add(p72);footer.add(p8);
			dataGrid.setFooter(footer);
		}
		return  dataGrid;
	}
	@Override
	public OrgSalary getOrgSalaryDept(String month, String orgId) {
		return this.monthSalaryDAO.getOrgSalaryDept(month, orgId);
	}
	
	
	@Override
	public DataGrid getMonthAnalyze(String shop, String month) {
		List<MonthAnalyze> list = this.monthSalaryDAO.getMonthAnalyze(shop,month);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(list.size());
		dataGrid.setRows(list);
		
		return dataGrid;
		
	}

}
