package com.zhugx.salary.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.zhugx.salary.dao.MonthSalaryDAO;
import com.zhugx.salary.dao.ProductPriceDAO;
import com.zhugx.salary.dao.common.JdbcBaseDAO;
import com.zhugx.salary.dao.common.SqlConstant;
import com.zhugx.salary.pojo.Configuration;
import com.zhugx.salary.pojo.ProductData;
import com.zhugx.salary.pojo.ProductPrice;
import com.zhugx.salary.webservice.ShengChanInterface;

@SuppressWarnings("all")
public class ProductPriceDAOImpl  implements ProductPriceDAO {
	private String sqlConfig = "From Configuration";
	static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript"); 
	private  JdbcBaseDAO jdbcTemplate;
	private MonthSalaryDAO monthSalaryDAO;
	
	public JdbcBaseDAO getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcBaseDAO jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public MonthSalaryDAO getMonthSalaryDAO() {
		return monthSalaryDAO;
	}
	public void setMonthSalaryDAO(MonthSalaryDAO monthSalaryDAO) {
		this.monthSalaryDAO = monthSalaryDAO;
	}
	//===============================================================================
	//   logic  method
	//===============================================================================
	
	@Override
	public void initProductPrice(String shop, String month) {
		//1.先清除数据
		String sqlDel = "delete From Product_price where month =? and shop = ?";
		this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlDel, month, shop);
		
		String sqlData = "SELECT  DISTINCT T.ITEM_ID, T.ITEM_NO, T.ITEM_NAME,SUM(IFNULL(FETCH_NUMS,0)) FETCHNUMS, SUM(IFNULL(COMPLETE_NUMS,0)) COMPLETENUMS, "+
		                 " SUM(IFNULL(CJSL,0)) CJSL, SUM(IFNULL(SGSL,0)) SGSL,SUM(IFNULL(TGSL,0)) TGSL "+
				     " FROM Product_Data  t  WHERE MONTH = '"+month+"' AND shop='"+shop+"'  GROUP BY     T.ITEM_ID, T.ITEM_NO, T.ITEM_NAME";
		Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
		
		List<ProductData> list = this.jdbcTemplate.queryForList(sqlData, ProductData.class);
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		if(list != null && list.size()>0){
			for(ProductData p : list){
				ProductPrice price = new ProductPrice();
				price.setUuid(Math.random()*1000+"");
				price.setItemId(p.getItemId());
				price.setItemNo(p.getItemNo());
				price.setItemName(p.getItemName());
				//车工
				if("车工完工数".equals(config.getCgpz())){
					price.setCgsl(p.getCompleteNums());
				}else if("车工领活数".equals(config.getCgpz())){
					price.setCgsl(p.getFetchNums());
				}
				
				//裁剪
				if("车工完工数".equals(config.getCjpz())){
					price.setCjsl(p.getCompleteNums());
				}else if("车工领活数".equals(config.getCjpz())){
					price.setCjsl(p.getFetchNums());
				}else {
					price.setCjsl(p.getCjsl());
				}
				
				//手工
				if("车工完工数".equals(config.getSgpz())){
					price.setSgsl(p.getCompleteNums());
				}else if("车工领活数".equals(config.getSgpz())){
					price.setSgsl(p.getFetchNums());
				}else {
					price.setSgsl(p.getSgsl());
				}
				
				//烫工
				if("车工完工数".equals(config.getTgpz())){
					price.setTgsl(p.getCompleteNums());
				}else if("车工领活数".equals(config.getTgpz())){
					price.setTgsl(p.getFetchNums());
				}else {
					price.setTgsl(p.getTgsl());
				}
				price.setGj(0.0);
				price.setGyj(0.0);
				price.setCggj(0.0);
				price.setCgje(0.0);
				price.setCjgj(0.0);
				price.setCjje(0.0);
				price.setSggj(0.0);
				price.setSgje(0.0);
				price.setTggj(0.0);
				price.setTgje(0.0);
				price.setMonth(month);
				price.setOpDate(new Date());
				
				Object[] param = {price.getUuid(),shop,price.getItemId(),price.getItemNo(),price.getItemName(),price.getGj(),price.getGyj(),
			        	  price.getCjgj(),price.getCjsl(),price.getCjje(), price.getCggj(),price.getCgsl(),price.getCgje(),
			        	  price.getSggj(),price.getSgsl(),price.getSgje(),  price.getTggj(),price.getTgsl(),price.getTgje(),
			        	  price.getMonth(),price.getOpDate()};
				batchArgs.add(param);
			}
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_PRICE, batchArgs);
		}
	}

	@Override
	public List<ProductPrice> getProductPrice(String shop, String month) {
		String sql = "select *,cjgj+cggj+sggj+tggj sumGj From Product_Price where month='"+month+"' and shop='"+shop+"'  order by item_no";
		return this.jdbcTemplate.queryForList(sql, ProductPrice.class);
	}

	@Override
	public void saveOrUpdateProductPrice(List<ProductPrice> list) {
		Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
		
		try{
			List<Object[]> batchAdd = new ArrayList<Object[]>();
			List<Object[]> batchUpdate = new ArrayList<Object[]>();
			for(ProductPrice p: list){
				//修改工价以后, 需要设定 车工 裁剪 清减 手工 烫工的工价 以及金额
				//车工
				String cggjStr = config.getCggj();
				cggjStr = cggjStr.replace("GJ", p.getGj()+"");
				if(cggjStr.indexOf("GYJ")>0){
					cggjStr = cggjStr.replace("GYJ", p.getGyj()+"");
					double cggjDouble = (Double)jse.eval(cggjStr);
					p.setCggj(new BigDecimal(cggjDouble).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				}else{
					p.setCggj(p.getGj());
				}
				//金额需要保留小数位数
				BigDecimal cgje = new BigDecimal(p.getCggj()*p.getCgsl()).setScale(config.getDecimals(), BigDecimal.ROUND_HALF_UP);
				p.setCgje(cgje.doubleValue());
				
				//裁剪
				String cjgjStr = config.getCjgj();
				cjgjStr = cjgjStr.replace("GJ", p.getGj()+"");
				double cjgjDouble =  (Double)jse.eval(cjgjStr);
				p.setCjgj(new BigDecimal(cjgjDouble).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				BigDecimal cjje = new BigDecimal(p.getCjgj()*p.getCjsl()).setScale(config.getDecimals(), BigDecimal.ROUND_HALF_UP);
				p.setCjje(cjje.doubleValue());
				
				//手工
				String sggjStr = config.getSggj();
				sggjStr = sggjStr.replace("GJ", p.getGj()+"");
				double sggjDouble =  (Double)jse.eval(sggjStr);
				p.setSggj(new BigDecimal(sggjDouble).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				BigDecimal sgje = new BigDecimal(p.getSggj()*p.getSgsl()).setScale(config.getDecimals(), BigDecimal.ROUND_HALF_UP);
				p.setSgje(sgje.doubleValue());
				
				//烫工
				String tggjStr = config.getTggj();
				tggjStr = tggjStr.replace("GJ", p.getGj()+"");
				double tggjDouble =  (Double)jse.eval(tggjStr);
				p.setTggj(new BigDecimal(tggjDouble).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
				BigDecimal tgje = new BigDecimal(p.getTggj()*p.getTgsl()).setScale(config.getDecimals(), BigDecimal.ROUND_HALF_UP);
				p.setTgje(tgje.doubleValue());
				
				if(p.getUuid() == null){
					Object[] param = {p.getUuid(),p.getShop(),p.getItemId(),p.getItemNo(),p.getItemName(),p.getGj(),p.getGyj(),
				        	  p.getCjgj(),p.getCjsl(),p.getCjje(), p.getCggj(),p.getCgsl(),p.getCgje(),
				        	  p.getSggj(),p.getSgsl(),p.getSgje(),  p.getTggj(),p.getTgsl(),p.getTgje(),
				        	  p.getMonth(),p.getOpDate()};
					batchAdd.add(param);
				}else{
					Object[] param = {p.getShop(),p.getItemId(),p.getItemNo(),p.getItemName(),p.getGj(),p.getGyj(),
				        	  p.getCjgj(),p.getCjsl(),p.getCjje(), p.getCggj(),p.getCgsl(),p.getCgje(),
				        	  p.getSggj(),p.getSgsl(),p.getSgje(),  p.getTggj(),p.getTgsl(),p.getTgje(),
				        	  p.getMonth(),p.getOpDate(),p.getUuid()};
					batchUpdate.add(param);
				}
			}
			if(batchAdd.size()>0){
				this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_PRICE, batchAdd);
			}
			if(batchUpdate.size()>0){
				this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.UPDATE_PRICE, batchUpdate);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	@Override
	public String getLastMonth(String month) {
		String result = null;
		String  sql = "select max(month)  month  from  product_price where month < '"+month+"'";
		String price = this.jdbcTemplate.getSimpleJdbcTemplate().queryForObject(sql, String.class);
		if(price != null){
			result = price;
		}
		return result;
	}

	@Override
	public void renewalProductPrice(String shop,String month) {
		//1. 修改数量
		String sqlData = "SELECT  DISTINCT T.ITEM_ID, T.ITEM_NO, T.ITEM_NAME,SUM(FETCH_NUMS) FETCHNUMS, SUM(COMPLETE_NUMS) COMPLETENUMS, "+
			    " SUM(T.CJSL) CJSL, SUM(T.SGSL) SGSL,SUM(T.TGSL) TGSL " +
				"FROM Product_Data  t  WHERE MONTH = '"+month+"' and shop ='"+shop+"'  GROUP BY     T.ITEM_ID, T.ITEM_NO, T.ITEM_NAME";
	    Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
	    List<ProductData> list = this.jdbcTemplate.queryForList(sqlData, ProductData.class);
	    
		String sqlUpdateSL = "UPDATE  product_price   t SET t.cjsl = ?,t.cgsl=?,t.sgsl=?,tgsl=? WHERE item_id=? AND MONTH=? and shop=?";
		List<Object[]> parameters = new ArrayList<Object[]>();
		 
		if(list != null && list.size()>0){
			for(int i=0; i<list.size();i++){
				Integer cjsl=0, cgsl=0,sgsl=0,tgsl=0;
				//车工
				if("车工完工数".equals(config.getCgpz())){
					cgsl = list.get(i).getCompleteNums();
				}else if("车工领活数".equals(config.getCgpz())){
					cgsl = list.get(i).getFetchNums();
				}
				
				//裁剪
				if("车工完工数".equals(config.getCjpz())){
					cjsl = list.get(i).getCompleteNums();
				}else if("车工领活数".equals(config.getCjpz())){
					cjsl = list.get(i).getCompleteNums();
				}else{
					cjsl = list.get(i).getCjsl();
				}
				
				//手工
				if("车工完工数".equals(config.getSgpz())){
					sgsl = list.get(i).getCompleteNums();
				}else if("车工领活数".equals(config.getSgpz())){
					sgsl = list.get(i).getCompleteNums();
				}else{
					sgsl = list.get(i).getSgsl();
				}
				
				//烫工
				if("车工完工数".equals(config.getTgpz())){
					tgsl = list.get(i).getCompleteNums();
				}else if("车工领活数".equals(config.getTgpz())){
					tgsl = list.get(i).getCompleteNums();
				}else{
					tgsl = list.get(i).getTgsl();
				}
				
				parameters.add(new Object[] {cjsl, cgsl,sgsl,tgsl,list.get(i).getItemId(), month,shop});  	
			}
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(sqlUpdateSL, parameters);
		
		//2. 修改金额
		String sqlUpdateJE = "UPDATE product_price t SET t.cjje = cjsl*cjgj,t.cgje = cgsl*cggj,t.sgje = sgsl*sggj,t.tgje = tgsl*tggj "+
		                     "WHERE  MONTH= '"+month+"' and shop='"+shop+"'";
		this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlUpdateJE);
		
		//3. 修改工段工资
		this.monthSalaryDAO.renewalOrgSalaryPrice(shop,month);
	}
	@Override
	public List<ProductPrice> getReprotFive(String month, String orgId,String orgName) {
		String sql = null;
		List<ProductPrice> list = null;
		
		if(orgName.indexOf("裁剪") == -1 &&  orgName.indexOf("手工") == -1
				&&  orgName.indexOf("烫工") == -1 ){ //1.车工
			sql = "SELECT t.item_name, t.item_no,t.complete_nums cgsl, t.fetch_nums cjsl, p.cggj, p.cggj*t.complete_nums piecePay "+
				  "FROM product_data  t, product_price p "+
				  "WHERE t.item_id =p.item_id AND t.month = p.month "+
				  "AND t.org_id = '"+orgId+"' AND t.month = '"+month+"'  and (t.complete_nums > 0 or t.fetch_nums  >0 ) order by t.item_no";
			
			list = this.jdbcTemplate.queryForList(sql, ProductPrice.class);
			Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
			if("车工领活数".equals(config.getCgpz())){
				for(ProductPrice p: list){
					p.setCgsl(p.getCjsl());
					p.setPiecePay(p.getCggj()*p.getCjsl());
				}
			}
		}else{
			//获取orgId 对应的 shopid 
			if(orgName.indexOf("裁剪") != -1){ //2.裁剪
				sql = "SELECT t.item_name, t.item_no,t.cjsl cgsl, t.cjgj cggj, round(t.cjsl*t.cjgj) piecePay  "+
					  " FROM product_price t  LEFT JOIN organize o on t.shop = o.shop "+
					  "where t.month = '"+month+"' and  o.uuid='"+orgId+"' and t.cjsl > 0  order by t.item_no";
			}else if(orgName.indexOf("手工") != -1){ //3.手工
				sql = "SELECT t.item_name, t.item_no,t.sgsl cgsl, t.sggj cggj, round(t.sgsl*t.sggj) piecePay  "+
					  " FROM product_price t   LEFT JOIN organize o on t.shop = o.shop "+
					  " where t.month = '"+month+"' and  o.uuid='"+orgId+"' and t.sgsl > 0 order by t.item_no ";
			}else if(orgName.indexOf("烫工") != -1){ //3.烫工
				sql = "SELECT t.item_name, t.item_no,t.tgsl cgsl, t.tggj cggj, round(t.tgsl*t.tggj) piecePay   "+
				      "FROM product_price t  LEFT JOIN organize o on t.shop = o.shop "+
					  "where t.month = '"+month+"' and  o.uuid='"+orgId+"'  and  t.tgsl > 0  order by t.item_no";
			}
			list = this.jdbcTemplate.queryForList(sql, ProductPrice.class);
		} 
		return list;
	}
	
	
	@Override
	public void deleteProductPriceItem(List<String> list, String shop, String month) {
		String sql = "delete  from product_price where month=? and item_id=? and shop = ?";
		List<Object[]> parameters = new ArrayList<Object[]>();
		for(String itemid: list){
			parameters.add(new Object[]{month, itemid, shop});
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(sql, parameters);  
	}
	
	
	@Override
	public void addProductPrice(List<ProductData> list,String shop, String month) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(ProductData p : list){
			ProductPrice price = new ProductPrice();
			price.setUuid(Math.random()*1000+"");
			price.setItemId(p.getItemId());
			price.setItemNo(p.getItemNo());
			price.setItemName(p.getItemName());
			
			price.setShop(shop);
			//车工
			price.setCgsl(0);
			//裁剪
			price.setCjsl(0);
			//手工
			price.setSgsl(0);
			//烫工
			price.setTgsl(0);
			price.setGj(0.0);
			price.setGyj(0.0);
			price.setCggj(0.0);
			price.setCgje(0.0);
			price.setCjgj(0.0);
			price.setCjje(0.0);
			price.setSggj(0.0);
			price.setSgje(0.0);
			price.setTggj(0.0);
			price.setTgje(0.0);
			price.setMonth(month);
			price.setOpDate(new Date());
			Object[] param = {price.getUuid(),price.getShop(),price.getItemId(),price.getItemNo(),price.getItemName(),price.getGj(),price.getGyj(),
				        	  price.getCjgj(),price.getCjsl(),price.getCjje(), price.getCggj(),price.getCgsl(),price.getCgje(),
				        	  price.getSggj(),price.getSgsl(),price.getSgje(),  price.getTggj(),price.getTgsl(),price.getTgje(),
				        	  price.getMonth(),price.getOpDate()};
			batchArgs.add(param);
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_PRICE, batchArgs);
	}
	
	
	@Override
	public void cleanData(String shop, String month) {
		String data = "DELETE  FROM  product_data where shop = ? and month = ? ";
		String time = "DELETE  FROM  product_time where shop = ? and month = ? ";
		String price = "DELETE  FROM  product_price where shop = ? and month = ? ";
		String org = "DELETE  FROM  org_salary where  shop = ? and month = ? ";
		String salary = "DELETE  FROM  month_salary where shop = ? and month = ? ";
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		Object[] param = {shop, month};
		batchArgs.add(param);
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(data, batchArgs);
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(time, batchArgs);
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(price, batchArgs);
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(org, batchArgs);
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(salary, batchArgs);
	}

}





