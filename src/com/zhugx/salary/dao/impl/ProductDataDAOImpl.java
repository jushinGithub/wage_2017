package com.zhugx.salary.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhugx.salary.dao.OrganizeDAO;
import com.zhugx.salary.dao.ProductDataDAO;
import com.zhugx.salary.dao.common.JdbcBaseDAO;
import com.zhugx.salary.pojo.Configuration;
import com.zhugx.salary.pojo.Organize;
import com.zhugx.salary.pojo.ProductData;
import com.zhugx.salary.pojo.common.DataGridProductData;

@SuppressWarnings("all")
public class ProductDataDAOImpl implements ProductDataDAO {
	public JdbcBaseDAO jdbcTemplate;
	private OrganizeDAO organizeDAO;
	
	public JdbcBaseDAO getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcBaseDAO jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public OrganizeDAO getOrganizeDAO() {
		return organizeDAO;
	}

	public void setOrganizeDAO(OrganizeDAO organizeDAO) {
		this.organizeDAO = organizeDAO;
	}

	private String sqlAdd = "INSERT INTO PRODUCT_DATA(UUID,ITEM_ID, ITEM_NO, ITEM_NAME,FETCH_NUMS,COMPLETE_NUMS,cjsl,sgsl, tgsl, "+
		                     " shop,ORG_ID,org_no, GROUP_NO, ORG_NAME, MONTH,op_date) VALUES(?,?,?,?,?, ?,?,?, ?,?,?,?,?,  ?,?,now())";
	private String sqlUpdate = "UPDATE PRODUCT_DATA SET FETCH_NUMS=?, COMPLETE_NUMS=? WHERE ITEM_ID = ? AND org_name=?  AND MONTH=? and shop=?";
	
	private String sqlUpdateQt = "UPDATE PRODUCT_DATA SET cjsl=?, sgsl=?, tgsl=? WHERE ITEM_ID = ? AND MONTH=? and shop=? and org_id is null";
	//====================================================================
	// logic method
	//====================================================================
	
	@Override
	public void initProductData(List<ProductData> dataCg,List<ProductData> dataQt, String shop, String month) {
		//1.先删除之前的数据
		/*
		List<ProductData> old = this.getProductDataByMonth(month,shop);
		if(old!= null && old.size()>0){
			List<Object[]> parameters = new ArrayList<Object[]>();
			String sqlDelete = "DELETE  FROM PRODUCT_DATA WHERE UUID = ?";
			for(ProductData p: old){
				parameters.add(new Object[] {p.getUuid()});  
			}
			 this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(sqlDelete, parameters);  
		}*/
		
		//2.(车工)初始化本月数据, 获取所有的物品, 给每个小组都分配, 如果没有数量就是为0 
		if(dataCg != null && dataCg.size() > 0){
			//1.获取所有的货物
			Map<String, ProductData> itemMap = new HashMap<String, ProductData>();
			List<String> items = new ArrayList<String>();
			for(ProductData ptemp: dataCg){
				if(!items.contains(ptemp.getItemId())){
					items.add(ptemp.getItemId());
					itemMap.put(ptemp.getItemId(), ptemp);
				}
			}
			//2.获取所有的组号
			String sqlOrg = "select * from Organize where uuid like '2_2%' and shop='"+shop+"'";
			List<Organize> orgs = this.jdbcTemplate.queryForList(sqlOrg, Organize.class);
			
			//3.初始化本月车工生产数据
			List<Object[]> parameters = new ArrayList<Object[]>();
			for(String itemid: items){
				ProductData itemP = itemMap.get(itemid);
				for(Organize orgP: orgs){
					parameters.add(new Object[] {Math.random()*1000 + "", itemP.getItemId(),itemP.getItemNo(), itemP.getItemName(), 
							0, 0, 0,0,0,shop,orgP.getUuid(),orgP.getOrgNo(),orgP.getGroupNo(),orgP.getOrgName(),itemP.getMonth()}); 
				}
			}
			//3. 初始化其他工段数据
			for(ProductData pd : dataQt){
				parameters.add(new Object[] {Math.random()*1000 + "", pd.getItemId(),pd.getItemNo(), pd.getItemName(), 
						0, 0, pd.getCjsl(),pd.getSgsl(),pd.getTgsl(), shop,null,null,null,null,month}); 
			}
			
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(sqlAdd, parameters);
			
			//4.然后更新车工数量
			List<Object[]> paramUpdate = new ArrayList<Object[]>();
			for(ProductData p : dataCg){
				paramUpdate.add(new Object[]{p.getFetchNums(), p.getCompleteNums(),p.getItemId(),p.getOrgName(),p.getMonth(), shop});  
			}
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(sqlUpdate, paramUpdate);
		}
	}
	
	/**
	 * 通过月份获取车工生产数据
	 * @param month
	 * @return
	 */
	public List<ProductData> getProductDataByMonth(String month,String shop){
		String sql = "select * From Product_Data  where month = '"+month+"' and  shop = '"+shop+"'";
		
		List<ProductData> result  = this.jdbcTemplate.queryForList(sql, ProductData.class);
		return result;
	}

	
	@Override 
	public void saveOrUpdateProductData(List<ProductData> list) {
		List<Object[]> paramUpdate = new ArrayList<Object[]>();
		
		//行转列以后, 只有更新
		for(ProductData p: list){
			paramUpdate.add(new Object[]{p.getFetchNums(), p.getCompleteNums(),p.getItemId(),p.getOrgName(),p.getMonth(), p.getShop()}); 
		}
		
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(sqlUpdate, paramUpdate);
	}
	
	@Override 
	public void saveOrUpdateProductDataQt(List<ProductData> list) {
		List<Object[]> paramUpdate = new ArrayList<Object[]>();
		//行转列以后, 只有更新
		for(ProductData p: list){
			paramUpdate.add(new Object[]{p.getCjsl(), p.getSgsl(),p.getTgsl(),p.getItemId(),p.getMonth(), p.getShop()}); 
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(sqlUpdateQt, paramUpdate);
	}
	
	@Override
	public DataGridProductData getMapData(String shop, String month,boolean isCg) {
		String sqlCg = "select * FROM Configuration"; 
		Configuration config = (Configuration)this.jdbcTemplate.queryForList(sqlCg, Configuration.class).get(0);
		
		//1. 获取所有的小组号
		List<String> listGroup = this.organizeDAO.getGroupNameOrigin(shop,month);
		if(listGroup == null || listGroup.size() == 0){
			return null;
		}else{
			//2.获取行转列数据
			StringBuffer sb = new StringBuffer();
			StringBuffer sumSlFetch = new StringBuffer();
			StringBuffer sumSlComp  = new StringBuffer();
			StringBuffer sumJeFetch = new StringBuffer();
			StringBuffer sumJeComp  = new StringBuffer();
			sb.append("SELECT d.item_id itemId, d.item_no itemNo, d.item_name itemName, p.cggj, ");
			for(int i=0; i<listGroup.size();i++){
				sb.append("max(IF(org_name = '"+listGroup.get(i)+"',fetch_nums,0))           as fetch"+listGroup.get(i)+", ");
				sb.append("max(IF(org_name = '"+listGroup.get(i)+"',fetch_nums,0))*p.cggj    as fetchJe"+listGroup.get(i)+", ");
				sumSlFetch.append("max(IF(org_name = '"+listGroup.get(i)+"',fetch_nums,0)) ").append(" + ");
				sumJeFetch.append("max(IF(org_name = '"+listGroup.get(i)+"',fetch_nums,0))*p.cggj  ").append(" + ");
				
				//如果是查询 车工计件工资明细表
				if(isCg && "车工领活数".equals(config.getCgpz())){
					sb.append("max(IF(org_name = '"+listGroup.get(i)+"',fetch_nums,0)) as comp"+listGroup.get(i)+", ");
					sb.append("max(IF(org_name = '"+listGroup.get(i)+"',fetch_nums,0))*p.cggj as completeJe"+listGroup.get(i)+", ");
					sumSlComp.append("max(IF(org_name = '"+listGroup.get(i)+"',fetch_nums,0)) ").append(" + ");
					sumJeComp.append("max(IF(org_name = '"+listGroup.get(i)+"',fetch_nums,0))*p.cggj  ").append(" + ");
				}else{
					sb.append("max(IF(org_name = '"+listGroup.get(i)+"',complete_nums,0)) as        comp"+listGroup.get(i)+", ");
					sb.append("max(IF(org_name = '"+listGroup.get(i)+"',complete_nums,0))*p.cggj as completeJe"+listGroup.get(i)+", ");
					sumSlComp.append("max(IF(org_name = '"+listGroup.get(i)+"',complete_nums,0)) ").append(" + ");
					sumJeComp.append("max(IF(org_name = '"+listGroup.get(i)+"',complete_nums,0))*p.cggj ").append(" + ");
					
				}
			}
			sumSlFetch.deleteCharAt(sumSlFetch.lastIndexOf("+"));
			sumSlComp.deleteCharAt(sumSlComp.lastIndexOf("+"));
			sumJeFetch.deleteCharAt(sumJeFetch.lastIndexOf("+"));
			sumJeComp.deleteCharAt(sumJeComp.lastIndexOf("+"));
			
			sb.append(sumSlFetch).append(" AS sumSlFetch, ").append(sumSlComp).append(" AS sumSlComp, ");
			sb.append(sumJeFetch).append(" AS sumJeFetch, ").append(sumJeComp).append(" AS sumJeComp, ");
			
			sb.append(" '"+ month+"' as month  FROM  product_data d, product_price p ");
			
			
			sb.append("WHERE d.MONTH = '"+month+"' and d.shop='"+shop+"'  AND d.month=p.month and d.shop=p.shop ");
			sb.append(" AND d.item_id = p.item_id    and (fetch_nums > 0 or complete_nums >0) group by d.item_id order by d.item_no ");
			List list = this.jdbcTemplate.getSimpleJdbcTemplate().queryForList(sb.toString());
			
					
			//3.设置返回结果
			String sqlFoot = "select org_name org_name, sum(fetch_nums) fetchNums, sum(complete_nums) completeNums," +
					         "sum(d.fetch_nums*p.cggj) fetchJe,  sum(d.complete_Nums*p.cggj) completeJe " +
							 "from product_data d, product_price p " +
							 "WHERE d.MONTH = '"+month+"' AND d.shop='"+shop+"'  AND d.month=p.month and d.shop=p.shop   " +
							 " and d.item_id=p.item_id GROUP BY org_name ORDER BY org_name";
			List<ProductData> listFoot = this.jdbcTemplate.queryForList(sqlFoot, ProductData.class);
			
			//每一行合计
			
			
			DataGridProductData result = new DataGridProductData();
			result.setTotal(list.size());
			result.setRows(list);
			List footer = new ArrayList();
			HashMap foot = new HashMap();
			foot.put("itemName", "合计");
			Integer fetchAll = 0;
			Integer compAll = 0;
			for(int k=0; k<listFoot.size(); k++){
				fetchAll += listFoot.get(k).getFetchNums();
				compAll  += listFoot.get(k).getCompleteNums();
				
				foot.put("fetch"+listFoot.get(k).getOrgName(), listFoot.get(k).getFetchNums());
				foot.put("comp"+listFoot.get(k).getOrgName(), listFoot.get(k).getCompleteNums());
				foot.put("fetchJe"+listFoot.get(k).getOrgName(), listFoot.get(k).getFetchJe());
				foot.put("completeJe"+listFoot.get(k).getOrgName(), listFoot.get(k).getCompleteJe());
			}
			foot.put("sumSlFetch", fetchAll);
			foot.put("sumSlComp", compAll);
			
			footer.add(foot);
			result.setFooter(footer);
			return result;
		}
		
		
	}

	@Override
	public List<String> getExsitItem(String shop, String month) {
		List<String> result = new ArrayList<String>();
		String sql = "select distinct item_id from product_data where month ='"+month+"' and shop='"+shop+"'";
		List<ProductData> listData =  this.jdbcTemplate.queryForList(sql, ProductData.class);
		for(ProductData data: listData){
			result.add(data.getItemId());
		}
		return result;
	}

	@Override
	public List<ProductData> getProductQtData(String shop, String month) {
		String sql = "SELECT t.month, t.shop, t.item_id, t.item_no, t.item_name, sum(t.cjsl) cjsl, sum(t.fetch_nums)  fetch_nums, sum(t.complete_nums)  complete_nums, "+
					 "	     sum(t.sgsl) sgsl, sum(t.tgsl) tgsl "+
					 "	FROM  product_data   t "+
					 " where MONTH='"+month+"' AND shop='"+shop+"' " +
					 "	GROUP BY t.item_id, t.MONTH, t.shop "+
					 "	ORDER BY item_no ";
				    //HAVING   sum(t.cjsl)>0 OR  sum(t.sgsl)>0 OR  sum(t.tgsl)>0
		return this.jdbcTemplate.queryForList(sql, ProductData.class);
	}


	

}
