package com.zhugx.salary.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.zhugx.salary.dao.ProductTimeDAO;
import com.zhugx.salary.dao.common.JdbcBaseDAO;
import com.zhugx.salary.dao.common.SqlConstant;
import com.zhugx.salary.pojo.Employee;
import com.zhugx.salary.pojo.Organize;
import com.zhugx.salary.pojo.ProductTime;

@SuppressWarnings("all")
public class ProductTimeDAOImpl  implements ProductTimeDAO {
	private  JdbcBaseDAO jdbcTemplate;
	
	public JdbcBaseDAO getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcBaseDAO jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//=====================================================================
	// logic method
	//=====================================================================
	
	@Override
	public void initProductTime(String shop, String month) {
		//1.先删除本月工时分
		String sqlDel = " delete from Product_Time where month=? and shop=?";
		this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlDel, new Object[]{month,shop});
		
		//2.获取所有计件的员工
		String sqlEm = "select t.*, o.org_name orgName, o.group_no groupNo from Employee t, organize o "+
		               "where t.org_id=o.uuid and is_Piece='√' and em_state = 1 and o.shop='"+shop+"'";
		List<Employee> ems = this.jdbcTemplate.queryForList(sqlEm, Employee.class);
		
		//3.初始化工时
		List<Object[]> batchAdd = new ArrayList<Object[]>();
		for(Employee em:ems){
			ProductTime p = new ProductTime();
			p.setUuid(Math.random()*1000+"");
			p.setEmId(em.getUuid());
			p.setEmNo(em.getEmNo());
			p.setEmName(em.getEmName());
			p.setShop(shop);
			p.setOrgId(em.getOrgId());
			p.setOrgName(em.getOrgName());
			p.setGroupNo(em.getGroupNo());
			p.setMonth(month);
			p.setGrade(em.getGrade() == null? 0.0:em.getGrade());
			p.setTime(0.0);
			p.setSumGrade(0.0);
			p.setOpDate(new Date());
			
			Object[] param = {p.getUuid(),p.getEmId(),p.getEmNo(),p.getEmName(),p.getGroupNo(),
					p.getTime(),p.getGrade(),p.getSumGrade(),p.getShop(),p.getOrgId(),p.getOrgName(),
					p.getMonth(),p.getOpDate()};
			batchAdd.add(param);
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_TIME, batchAdd);
	}

	@Override
	public List<ProductTime> getProductTime(String month, String team) {
		String sql = "select * From Product_Time where month='"+month+"' and org_Id in ("+team+") order by group_no, em_no";
		return this.jdbcTemplate.queryForList(sql, ProductTime.class);
	}

	@Override
	public void saveOrUpdateProductTime(List<ProductTime> list) {
		List<Object[]> batchAdd = new ArrayList<Object[]>();
		List<Object[]> batchUpdate = new ArrayList<Object[]>();
		
		for(ProductTime p: list){
			p.setSumGrade(p.getTime()*p.getGrade());
			
			if(p.getUuid() == null){
				Object[] param = {p.getUuid(),p.getEmId(),p.getEmNo(),p.getEmName(),p.getGroupNo(),
						p.getTime(),p.getGrade(),p.getSumGrade(),p.getShop(),p.getOrgId(),p.getOrgName(),p.getMonth(),p.getOpDate()};
				batchAdd.add(param);
			}else{
				Object[] param = {p.getEmId(),p.getEmNo(),p.getEmName(),p.getGroupNo(),
						p.getTime(),p.getGrade(),p.getSumGrade(),p.getShop(),p.getOrgId(),p.getOrgName(),
						p.getMonth(),p.getOpDate(),p.getUuid()};
				batchUpdate.add(param);
			}
		}
		
		if(batchAdd.size()>0){
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_TIME, batchAdd);
		}
		if(batchUpdate.size()>0){
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.UPDATE_TIME, batchUpdate);
		}
	}

	@Override
	public String reLoadTime(String month, String orgId) {
		//1.先删除离职的员工
		String sqlDelTime = "delete from  product_time  where org_id in ("+orgId+")  and month='"+month+"'    "+
		                " and em_id  not in  (select uuid from employee where org_id in ("+orgId+") and em_state=1 )  " ;
		this.jdbcTemplate.getJdbcTemplate().execute(sqlDelTime);

		String sqlDelSalary = "delete from  month_salary  where org_id in ("+orgId+")  and month='"+month+"'  AND is_Piece = '√'   "+
		                " and em_id  not in  (select uuid from employee where org_id in ("+orgId+") and em_state=1 )  " ;
		this.jdbcTemplate.getJdbcTemplate().execute(sqlDelSalary);
		
		//2. 加入新增的员工
		String sqlQuery = "select t.*, o.org_name orgName, o.group_no groupNo, o.shop from employee t, organize o "+
		               "where t.org_id=o.uuid and is_Piece='√' and em_state = 1 and t.org_id = '"+orgId + "' "+
		               " and t.uuid not in (select em_id  from product_time where month='"+month+"' and org_id='"+orgId+"')";
		
		
		List<Employee> listAll = this.jdbcTemplate.queryForList(sqlQuery, Employee.class);
		
		if(listAll != null && listAll.size() > 0){
			List<Object[]> batchAdd = new ArrayList<Object[]>();
			List<Object[]> batchTime = new ArrayList<Object[]>();
			for(Employee em: listAll){
				
				Object[] param = new Object[]{Math.random()*1000+"",em.getUuid(),em.getEmNo(),em.getEmName(),em.getGroupNo(),
						0,em.getGrade()==null?0.0:em.getGrade(),0.0,em.getShop(),em.getOrgId(),em.getOrgName(),
						month,new Date()};
				batchAdd.add(param);
				
				Calendar now = Calendar.getInstance();
				String[] p = month.split("-");
				now.set(Calendar.YEAR, Integer.parseInt(p[0]));
				now.set(Calendar.MONTH, Integer.parseInt(p[1])-1);
				now.set(Calendar.DAY_OF_MONTH, 1); 
				
				Object[] paramTime = new Object[]{Math.random()*1000+"",em.getUuid(),em.getEmNo(),em.getEmName(),em.getShop(),em.getOrgId(),em.getOrgNo(),em.getOrgName(),
						em.getGroupNo(),month,em.getIsPiece(),em.getBaseSalary(),0.0,0.0,0.0, 
						0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,
						0.0,now.getActualMaximum(Calendar.DATE),0.0,em.getGrade()==null?0.0:em.getGrade(),0.0,0.0,0.0,new Date(),em.getJob()};
				batchTime.add(paramTime);
			}
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_TIME, batchAdd);
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_MONTHSALARY, batchTime);
		}
		
		//3.更新名字, 有可能是只改了名字, 工时分和计件计时人员名单都要改
		String sqlTime = "update product_time t set t.em_name = (select  e.em_name from employee e where e.uuid=t.em_id) "+
                         " where t.month = '"+month+"' and t.org_id = '"+orgId+"' ";
		String sqlSalay = "update month_salary m set m.em_name = (select  e.em_name from employee e where e.uuid=m.em_id) "+
                          " where m.month = '"+month+"' and m.org_id = '"+orgId+"'";
		this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlTime);
		this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlSalay);
		
		
		//4. 更改了工时分, 需要调用更新, 在service层做, 返回shop id
		String sqlShop = "select max(shop) shop from organize where uuid in ("+orgId+")";
		Organize  shop = (Organize)this.jdbcTemplate.queryForList(sqlShop, Organize.class).get(0); 
		
		return shop.getShop();
	}

	@Override
	/**
	 * 把工时分删除之后, 需要把计件工资表的员工也删除掉, 还要重新计算工段工资
	 */
	public boolean delTime(List<ProductTime> list) {
		boolean result = false;
		String sqlTime   = "delete from product_time where uuid =?";
		String sqlSalary = "delete from month_salary where month=? and shop=? and org_id=? and em_id=?";
		List<Object[]> batchTime = new ArrayList<Object[]>();
		List<Object[]> batchSalary = new ArrayList<Object[]>();
		
		for(ProductTime time: list){
			Object[] paramTime = new Object[]{time.getUuid()};
			Object[] param = new Object[]{time.getMonth(), time.getShop(), time.getOrgId(), time.getEmId()};
			
			batchTime.add(paramTime);
			batchSalary.add(param);
		}
		
		if(batchTime.size() > 0){
			result = true;
			this.jdbcTemplate.getJdbcTemplate().batchUpdate(sqlTime, batchTime);
			this.jdbcTemplate.getJdbcTemplate().batchUpdate(sqlSalary, batchSalary);
		}
		
		return result;
		
	}

}
