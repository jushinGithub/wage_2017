package com.zhugx.salary.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.zhugx.salary.dao.MonthSalaryDAO;
import com.zhugx.salary.dao.OrganizeDAO;
import com.zhugx.salary.dao.ProductDataDAO;
import com.zhugx.salary.dao.ProductPriceDAO;
import com.zhugx.salary.dao.ProductTimeDAO;
import com.zhugx.salary.pojo.ProductData;
import com.zhugx.salary.pojo.ProductPrice;
import com.zhugx.salary.pojo.ProductTime;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.pojo.common.DataGridProductData;
import com.zhugx.salary.service.ProductService;
import com.zhugx.salary.tool.ZhugxUtils;
import com.zhugx.salary.webservice.CheGong;
import com.zhugx.salary.webservice.Others;
import com.zhugx.salary.webservice.ShengChanInterface;

public class ProductServiceImpl implements ProductService {
	private ProductDataDAO productDataDAO;
	private ProductPriceDAO productPriceDAO;
	private ProductTimeDAO productTimeDAO;
	private MonthSalaryDAO salaryDAO;
	private OrganizeDAO organizeDAO;
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public OrganizeDAO getOrganizeDAO() {
		return organizeDAO;
	}
	@Resource
	public void setOrganizeDAO(OrganizeDAO organizeDAO) {
		this.organizeDAO = organizeDAO;
	}

	public MonthSalaryDAO getSalaryDAO() {
		return salaryDAO;
	}
	@Resource
	public void setSalaryDAO(MonthSalaryDAO salaryDAO) {
		this.salaryDAO = salaryDAO;
	}

	public ProductDataDAO getProductDataDAO() {
		return productDataDAO;
	}

	@Resource
	public void setProductDataDAO(ProductDataDAO productDataDAO) {
		this.productDataDAO = productDataDAO;
	}

	public ProductPriceDAO getProductPriceDAO() {
		return productPriceDAO;
	}

	@Resource
	public void setProductPriceDAO(ProductPriceDAO productPriceDAO) {
		this.productPriceDAO = productPriceDAO;
	}

	public ProductTimeDAO getProductTimeDAO() {
		return productTimeDAO;
	}

	@Resource
	public void setProductTimeDAO(ProductTimeDAO productTimeDAO) {
		this.productTimeDAO = productTimeDAO;
	}

	// ======================================================================
	// logic method
	// ======================================================================
	//1. 初始化
	@Override
	public void initProductData(String shop, String month,List<CheGong> listCgOld, List<Others> listQtOld) {
		// 1. 调用生产模块接口, 传递过来的数据
		System.out.println("开始  调用生产模块接口, 传递过来的数据------>"+sdf.format(new Date()));
		Map<String, String> orgIds = this.organizeDAO.getCgOrgid(shop);
		List<ProductData>  listCg = ShengChanInterface.getCgData(listCgOld, orgIds,shop, month);
		List<ProductData> listQt = ShengChanInterface.getQtData(listQtOld,orgIds,shop, month);
		
		// 2. 初始化本月的生产数据
		System.out.println(" 开始  初始化本月的生产数据------>"+sdf.format(new Date()));
		this.productDataDAO.initProductData(listCg,listQt,shop, month);
		System.out.println("完成   初始化本月的生产数据------>"+sdf.format(new Date()));
		
	}
	public void initProductDataCallback(String shop, String month,List<CheGong> listCgOld, List<Others> listQtOld) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println("==============初始化车工数据,  开始回调============="+sdf.format(new Date()));
		
		
		// 3.初始化本月工价
		System.out.println("初始化本月工价------>"+sdf.format(new Date()));
		this.productPriceDAO.initProductPrice(shop,month);
		
		
		// 4.初始化本月工时
		System.out.println("初始化本月工时------>"+sdf.format(new Date()));
		this.productTimeDAO.initProductTime(shop, month);
		
		
		//5. 初始化计件工段工资
		System.out.println("初始化计件工段工资------>"+sdf.format(new Date()));
		this.salaryDAO.initOrgSalary(shop,month);
		
		
		// 6.初始化本月工资
		System.out.println("初始化本月工资------>"+sdf.format(new Date()));
		this.salaryDAO.initSalary(shop,month);
		
		System.out.println("==============初始化车工数据,  结束回调============="+sdf.format(new Date()));
	}
	
	
	@Override
	/**
	 * 重新加载, 影响到工价表 ,并不是初始化数据, 而是1.重新加载已经存在的 2.删除不存在的   3. 增加新增的
	 */
	public void reloadProductData(String shop, String month,List<CheGong> listCgOld, List<Others> listQtOld) {
		// 1 调用生产模块接口, 传递过来的数据
		Map<String, String> orgIds = this.organizeDAO.getCgOrgid(shop);
		List<ProductData> listCg = ShengChanInterface.getCgData(listCgOld,orgIds,shop, month);
		List<ProductData> listQt = ShengChanInterface.getQtData(listQtOld,orgIds,shop, month);
		
		//2. 工价表的货物, 1.删除不存在的   2. 增加新增的
		List<String> itemRe = new ArrayList<String>();
		Map<String, ProductData> mapRe = new HashMap<String, ProductData>();
		for(ProductData re: listCg){
			if(!itemRe.contains(re.getItemId())){
				itemRe.add(re.getItemId());
				mapRe.put(re.getItemId(), re);
			}
		}
		for(ProductData re: listQt){
			if(!itemRe.contains(re.getItemId())){
				itemRe.add(re.getItemId());
				mapRe.put(re.getItemId(), re);
			}
		}
		List<String> itemExsit = this.productDataDAO.getExsitItem(shop, month);
		
		//需要删除的货物
		List<String> listDel  = new ArrayList<String>();
		CollectionUtils.addAll(listDel, new Object[itemExsit.size()]);
		Collections.copy(listDel,itemExsit);
		listDel.removeAll(itemRe);
		if(listDel.size()>0){
			this.productPriceDAO.deleteProductPriceItem(listDel, shop,month);
		}
		
		//需要新增的货物
		List<String> listAdd  = new ArrayList<String>();
		CollectionUtils.addAll(listAdd, new Object[itemRe.size()]);
		Collections.copy(listAdd,itemRe);
		listAdd.removeAll(itemExsit);
		if(listAdd.size()>0){
			List<ProductData> listDataAdd = new ArrayList<ProductData>();
			for(String itemid: listAdd){
				listDataAdd.add(mapRe.get(itemid));
			}
			this.productPriceDAO.addProductPrice(listDataAdd, shop,month);
		}
		
		// 3  初始化本月的生产数据
		this.productDataDAO.initProductData(listCg,listQt,shop,month);
		
		//2. 需要更新, product_price 工价表的数量, 
		this.productPriceDAO.renewalProductPrice(shop, month);
	}

	@Override
	public DataGridProductData getProductDataByMonth(String shop, String month,boolean isCg) {
		return this.productDataDAO.getMapData(shop,month,isCg);
	}

	@Override
	public void saveOrUpdateProductData(List<ProductData> list) {
		 System.out.println("~~~~~~~  保存车工数据   开始     "+sdf.format(new Date()));
		 this.productDataDAO.saveOrUpdateProductData(list);
		 System.out.println("~~~~~~~  保存车工数据  结束     "+sdf.format(new Date()));
	}
	public void saveOrUpdateProductDataCallback(String shop, String month) {
		 System.out.println("@@@@@@@  保存车工数据回调开始  @@@@@@@@@@"+sdf.format(new Date()));
		 //2. 需要更新, product_price 工价表的数量
		 this.productPriceDAO.renewalProductPrice(shop, month);
		 System.out.println("@@@@@@@  保存车工数据回调结束  @@@@@@@@@@"+sdf.format(new Date()));
	}
	
	
	@Override
	public void saveOrUpdateProductDataQt(List<ProductData> list) {
		System.out.println("^^^^^^^^^^  保存手工烫工 数据  开始     "+sdf.format(new Date()));
		 this.productDataDAO.saveOrUpdateProductDataQt(list);
		 System.out.println("^^^^^^^^^^  保存手工烫工 数据   结束  "+sdf.format(new Date()));
		
	}
	public void saveOrUpdateProductDataQtCallback(List<ProductData> list) {
		System.out.println("^^^^^^^^^^  保存手工烫工 数据(回调)  开始 ^^^^^^^^^^^^^"+sdf.format(new Date()));
		 //2. 需要更新, product_price 工价表的数量
		 this.productPriceDAO.renewalProductPrice(list.get(0).getShop(),list.get(0).getMonth());
		 System.out.println("^^^^^^^^^^  保存手工烫工 数据(回调)  结束 ^^^^^^^^^^^^^"+sdf.format(new Date()));
	}


	@Override
	public DataGrid getProductTime(String month, String team) {
		List<ProductTime> list = this.productTimeDAO.getProductTime(month, team);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(list.size());
		dataGrid.setRows(list);
		
		//增加合计行
		List lisFoot = new ArrayList();
		ProductTime footer = new ProductTime();
		footer.setEmName("合计");
		Double timeF = 0.0;
		Double gradeF = 0.0;
		Double sumF = 0.0;
		int cpNum = 0;
		for(ProductTime pt: list){
			timeF += pt.getTime();
			gradeF = ZhugxUtils.addDouble(pt.getGrade(), gradeF);
			sumF = ZhugxUtils.addDouble(pt.getSumGrade(), sumF);
		}
		footer.setTime(timeF);
		footer.setGrade(gradeF);
		footer.setSumGrade(sumF);
		lisFoot.add(footer);
		dataGrid.setFooter(lisFoot);
		return dataGrid;
	}

	@Override
	public void saveOrUpdateProductTime(List<ProductTime> list) {
		System.out.println("&&&&&&&&& 保存工时分  开始  "+sdf.format(new Date()));
		this.productTimeDAO.saveOrUpdateProductTime(list);
		System.out.println("&&&&&&&&& 保存工时分  结束 "+sdf.format(new Date()));
	}
	public void saveOrUpdateProductTimeCallback(List<ProductTime> list) {
		System.out.println("&&&&&&&&& 保存工时分  开始回调 &&&&&&&&&"+sdf.format(new Date()));
		//更新  工段计件工资(工时)
		this.salaryDAO.renewalOrgSalaryTime(list.get(0).getShop(),list.get(0).getMonth());
		System.out.println("&&&&&&&&& 保存工时分   结束回调 &&&&&&&&&"+sdf.format(new Date()));
	}

	@Override
	public DataGrid getProductPrice(String shop, String month) {
		List<ProductPrice> list =  this.productPriceDAO.getProductPrice(shop,month);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(list.size());
		dataGrid.setRows(list);
		//增加合计行
		List lisFoot = new ArrayList();
		ProductPrice footer = new ProductPrice();
		footer.setItemName("合计");
		Double gjF = 0.0;
		Double gyjF = 0.0;
		
		Integer cgslF = 0;
		Double  cgjeF = 0.0;
		
		Integer sgslF = 0;
		Double  sgjeF = 0.0;
		
		Integer cjslF = 0;
		Double  cjjeF = 0.0;
		
		Integer tgslF = 0;
		Double  tgjeF = 0.0;
		
		for(ProductPrice pp: list){
			gjF = ZhugxUtils.addDouble(pp.getGj(), gjF);
			gyjF = ZhugxUtils.addDouble(pp.getGyj(), gyjF);
			
			cgslF += pp.getCgsl()==null?0:pp.getCgsl();
			cgjeF = ZhugxUtils.addDouble(pp.getCgje(), cgjeF);
			
			cjslF += pp.getCjsl()==null?0:pp.getCjsl();
			cjjeF = ZhugxUtils.addDouble(pp.getCjje(), cjjeF);
			
			sgslF += pp.getSgsl()==null?0:pp.getSgsl();
			sgjeF = ZhugxUtils.addDouble(pp.getSgje(), sgjeF);
			
			tgslF += pp.getTgsl()==null?0:pp.getTgsl();
			tgjeF = ZhugxUtils.addDouble(pp.getTgje(), tgjeF);
		}
		footer.setGj(gjF);
		footer.setGyj(gyjF);
		
		footer.setCgsl(cgslF);
		footer.setCgje(cgjeF);
		
		footer.setCjsl(cjslF);
		footer.setCjje(cjjeF);
		
		footer.setSgsl(sgslF);
		footer.setSgje(sgjeF);
		
		footer.setTgsl(tgslF);
		footer.setTgje(tgjeF);
		lisFoot.add(footer);
		dataGrid.setFooter(lisFoot);
		return dataGrid;
	}

	@Override
	public void saveOrUpdateProductPrice(List<ProductPrice> list) {
		System.out.println("------ 保存工价    开始   "+sdf.format(new Date()));
		this.productPriceDAO.saveOrUpdateProductPrice(list);
		System.out.println("------ 保存工价   结束  "+sdf.format(new Date()));
	}
	public void saveOrUpdateProductPriceCallback(List<ProductPrice> list) {
		System.out.println("------ 保存工价    开始回调   ---------------"+sdf.format(new Date()));
		//工价
		this.salaryDAO.renewalOrgSalaryPrice(list.get(0).getShop(), list.get(0).getMonth());
		System.out.println("------ 保存工价    结束回调   ---------------"+sdf.format(new Date()));
	}
	

	@Override
	public void userLastMonthPrice(String shop, String month) {
	    String lastMonth = this.productPriceDAO.getLastMonth(month);
	    
	    if(lastMonth != null){
	    	 List<ProductPrice> thisMonthList = this.productPriceDAO.getProductPrice(shop,month);
	 	     List<ProductPrice> lastMonthList = this.productPriceDAO.getProductPrice(shop,lastMonth);
	 	    
	 	     if(thisMonthList != null && thisMonthList.size() >0 && lastMonthList != null && lastMonthList.size() >0){
	 	    	for(ProductPrice pt: thisMonthList){
	 	    		for(int i=0; i<lastMonthList.size(); i++){
	 	    			if(pt.getItemId().equals(lastMonthList.get(i).getItemId())){
	 	    				pt.setGj(lastMonthList.get(i).getGj());
	 	    				pt.setGyj(lastMonthList.get(i).getGyj());
	 	    			}
	 	    		}
	 	    	}
	 	    	this.productPriceDAO.saveOrUpdateProductPrice(thisMonthList);
	 	    }
	    }
	    
	    //改了工价, 需要修改 工段计件工资
	    this.salaryDAO.renewalOrgSalaryPrice(shop,month);
	}

	@Override
	public List<String> getGroupName(String shop,String month) {
		return this.organizeDAO.getGroupName(shop,month);
	}

	@Override
	public List<String> getGroupNo(String shop) {
		return this.organizeDAO.getGroupNo(shop);
	}

	@Override
	public void reLoadTime(String month, String orgId) {
		//1.更新员工人员
		String shop = this.productTimeDAO.reLoadTime(month, orgId);
		
		//2.更新工资, 更新总工时分
		this.salaryDAO.renewalOrgSalaryTime(shop, month);
	}

	@Override
	public void delTime(List<ProductTime> list) {
		//1.删除 product_time  month_salary
		boolean result = this.productTimeDAO.delTime(list);
		if(result){
			//2. 重新计算工段工资
			this.salaryDAO.renewalOrgSalaryTime(list.get(0).getShop(), list.get(0).getMonth());
		}
	}
	@Override
	public DataGrid getProductQtData(String shop, String month) {
		List<ProductData> list  = this.productDataDAO.getProductQtData(shop, month);
		DataGrid dataGrid = new DataGrid();
		dataGrid.setTotal(list.size());
		dataGrid.setRows(list);
		//增加合计行
		List lisFoot = new ArrayList();
		ProductData footer = new ProductData();
		footer.setItemName("合计");
		Integer cjsl = 0;
		Integer cgRec = 0;
		Integer cgFin = 0;
		Integer sgsl = 0;
		Integer tgsl = 0;
		
		for(ProductData pp: list){
			cjsl  += pp.getCjsl();
			cgRec += pp.getFetchNums();
			cgFin += pp.getCompleteNums();
			sgsl  += pp.getSgsl();
			tgsl  += pp.getTgsl();
		}
		footer.setCjsl(cjsl);
		footer.setFetchNums(cgRec);
		footer.setCompleteNums(cgFin);
		footer.setSgsl(sgsl);
		footer.setTgsl(tgsl);
		lisFoot.add(footer);
		dataGrid.setFooter(lisFoot);
		return dataGrid;
	}
	
	
	@Override
	public void cleanData(String shop, String month) {
		this.productPriceDAO.cleanData(shop, month);
	}
}
