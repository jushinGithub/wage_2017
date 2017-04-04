package com.zhugx.salary.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.zhugx.salary.dao.EmployeeDAO;
import com.zhugx.salary.dao.common.JdbcBaseDAO;
import com.zhugx.salary.dao.common.SqlConstant;
import com.zhugx.salary.pojo.Attendance;
import com.zhugx.salary.pojo.Configuration;
import com.zhugx.salary.pojo.Employee;
import com.zhugx.salary.pojo.common.DataGrid;


@Repository
@SuppressWarnings("all")
public class EmployeeDAOImpl  implements EmployeeDAO{
	public JdbcBaseDAO jdbcTemplate;
	
	public void setJdbcTemplate(JdbcBaseDAO jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcBaseDAO getJdbcTemplate() {
		return jdbcTemplate;
	} 

	//==============================================================
	// logic method 
	//==============================================================
	@Override
	public List<Employee> getEmployeeByOrgId(String orgId,String containAll) {
		//initData();
		String sql = "SELECT T.*, O.GROUP_NO, O.ORG_NAME FROM EMPLOYEE T, ORGANIZE O  "+
	                 "WHERE T.ORG_ID = O.UUID  AND ORG_ID ='"+orgId+"'";
		if(containAll != null && "true".equals(containAll)){//查询包含离职员工
			sql += "  ORDER BY EM_STATE DESC, EM_NO";
		}else{
			sql += " AND T.em_state = '1' ORDER BY EM_STATE DESC, EM_NO";
		}
		
		List<Employee> result = this.jdbcTemplate.queryForList(sql, Employee.class);
		return result;
	}
	
	@Override
	public List<String> saveOrUpdateEmployee(List<Employee> employees) {
		
		List<Object[]> batchAdd = new ArrayList<Object[]>();
		List<Object[]> batchUpdate = new ArrayList<Object[]>();
		
		for(Employee temp: employees){
			//设置在职
			if(temp.getEmState() == null || "".equals(temp.getEmState())){
				temp.setEmState("1");
			}
			
			//设置组号
			if(temp.getGroupNo()==null || "".equals(temp.getGroupNo())){
				String sqlGroupNo = "SELECT  GROUP_no FROM organize t WHERE uuid = '"+temp.getOrgId()+"'";
				String groupNo =  this.jdbcTemplate.getSimpleJdbcTemplate().queryForObject(sqlGroupNo, String.class);
				temp.setGroupNo(groupNo);
			}
			
			boolean isExitEmno = false;
			if(temp.getEmNo() != null && !"".equals(temp.getEmNo())){
				String emNoExit = "SELECT count(*) FROM employee t WHERE em_state=1 AND org_id = '"+temp.getOrgId()+"' and em_no='"+temp.getEmNo()+"'";
				Integer  emNoExitCount = this.jdbcTemplate.getSimpleJdbcTemplate().queryForObject(emNoExit, Integer.class);
				if(emNoExitCount > 1){
					isExitEmno = true;
				}
			}
			
			//1. 设置工号, 取部门最大值   2.判断工号是否已经存在, 如果存在就最大值+1
			if(isExitEmno || temp.getEmNo() == null || "".equals(temp.getEmNo())){
				String sqlMax = "SELECT IFNULL(max(t.em_no),0) FROM employee t WHERE em_state=1 AND org_id = '"+temp.getOrgId()+"'";
				String maxEmno = this.jdbcTemplate.getSimpleJdbcTemplate().queryForObject(sqlMax, String.class);
				Integer max = Integer.parseInt(maxEmno)+1;
				temp.setEmNo(max+"");
			}
			
			if(temp.getUuid() == null){
				temp.setUuid(Math.random()*1000 + "");
				
				Object[] params = {temp.getUuid(),temp.getOrgId(),temp.getEmNo(),temp.getEmName(),temp.getIsPiece(),
						temp.getBaseSalary(),temp.getEmState(), temp.getJob(),temp.getGrade()};
				batchAdd.add(params);
			}else{
				Object[] params = {temp.getOrgId(),temp.getEmNo(),temp.getEmName(),temp.getIsPiece(),
						temp.getBaseSalary(),temp.getEmState(),temp.getJob(),temp.getGrade(),temp.getUuid()};
				batchUpdate.add(params);
			}
			
		}
		
		if(batchAdd.size()>0){
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_EM,batchAdd);
		}
		if(batchUpdate.size()>0){
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.UPDATE_EM,batchUpdate);
		}
		
		return null;
	}


	@Override
	/**
	 * 不做物理删除 , 设置离职状态
	 */
	public void removeEmployee(List<Employee> employees) {
		for(Employee temp: employees){
			temp.setEmState("0"); //设置离职状态
			temp.setEmNo("");     //把工号清空
			
			Object[] params = {temp.getOrgId(),temp.getEmNo(),temp.getEmName(),temp.getIsPiece(),
					temp.getBaseSalary(),temp.getEmState(),temp.getJob(), temp.getGrade(),temp.getUuid()};
			this.jdbcTemplate.getSimpleJdbcTemplate().update(SqlConstant.UPDATE_EM,params);
			
		}
	}
	
	/** 初始化人员 */
	public void initData(){
		//裁剪
		Employee temp11101 = new Employee();
		temp11101.setUuid(Math.random()*1000 + "");
		temp11101.setEmNo("21101");
		temp11101.setEmName("王裁剪");
		temp11101.setOrgId("211");
		temp11101.setIsPiece("√");
		//车工
		Employee temp221 = new Employee();
		temp221.setUuid(Math.random()*1000 + "");
		temp221.setEmNo("2120101");
		temp221.setEmName("张三车工1号");
		temp221.setOrgId("21201");
		temp221.setIsPiece("√");
		Employee temp222 = new Employee();
		temp222.setUuid(Math.random()*1000 + "");
		temp222.setEmNo("2120102");
		temp222.setEmName("张三车工2号");
		temp222.setOrgId("21201");
		temp222.setIsPiece("√");
		
		Employee temp223 = new Employee();
		temp223.setUuid(Math.random()*1000 + "");
		temp223.setEmNo("2120201");
		temp223.setEmName("李四车工3号");
		temp223.setOrgId("21202");
		temp223.setIsPiece("√");
		Employee temp224 = new Employee();
		temp224.setUuid(Math.random()*1000 + "");
		temp224.setEmNo("2120202");
		temp224.setEmName("李四车工4号");
		temp224.setOrgId("21202");
		temp224.setIsPiece("√");
		
		
		Employee temp225 = new Employee();
		temp225.setUuid(Math.random()*1000 + "");
		temp225.setEmNo("2120301");
		temp225.setEmName("王五车工5号");
		temp225.setOrgId("21203");
		temp225.setIsPiece("√");
		Employee temp226 = new Employee();
		temp226.setUuid(Math.random()*1000 + "");
		temp226.setEmNo("2120302");
		temp226.setEmName("王五车工6号");
		temp226.setOrgId("21203");
		temp226.setIsPiece("√");
		
		//手工
		Employee temp231 = new Employee();
		temp231.setUuid(Math.random()*1000 + "");
		temp231.setEmNo("21301");
		temp231.setEmName("黄手工1号");
		temp231.setOrgId("213");
		temp231.setIsPiece("√");
		Employee temp232 = new Employee();
		temp232.setUuid(Math.random()*1000 + "");
		temp232.setEmNo("21302");
		temp232.setEmName("黄手工2号");
		temp232.setOrgId("213");
		temp232.setIsPiece("√");
		
		//烫工
		Employee temp241 = new Employee();
		temp241.setUuid(Math.random()*1000 + "");
		temp241.setEmNo("21401");
		temp241.setEmName("小烫1号");
		temp241.setOrgId("214");
		temp241.setIsPiece("√");
		Employee temp242 = new Employee();
		temp242.setUuid(Math.random()*1000 + "");
		temp242.setEmNo("21402");
		temp242.setEmName("小烫2号");
		temp242.setOrgId("214");
		temp242.setIsPiece("√");
		
		
		//车间2 
		//裁剪
		Employee temp12101 = new Employee();
		temp12101.setUuid(Math.random()*1000 + "");
		temp12101.setEmNo("22101");
		temp12101.setEmName("王裁剪2");
		temp12101.setOrgId("221");
		temp12101.setIsPiece("√");
		//车工
		Employee temp1220101 = new Employee();
		temp1220101.setUuid(Math.random()*1000 + "");
		temp1220101.setEmNo("2220101");
		temp1220101.setEmName("张三车工1号2");
		temp1220101.setOrgId("22201");
		temp1220101.setIsPiece("√");
		Employee temp1220102 = new Employee();
		temp1220102.setUuid(Math.random()*1000 + "");
		temp1220102.setEmNo("2220102");
		temp1220102.setEmName("张三车工2号2");
		temp1220102.setOrgId("22201");
		temp1220102.setIsPiece("√");
		
		Employee temp1220201 = new Employee();
		temp1220201.setUuid(Math.random()*1000 + "");
		temp1220201.setEmNo("2220201");
		temp1220201.setEmName("李四车工3号2");
		temp1220201.setOrgId("22202");
		temp1220201.setIsPiece("√");
		Employee temp1220202 = new Employee();
		temp1220202.setUuid(Math.random()*1000 + "");
		temp1220202.setEmNo("2220202");
		temp1220202.setEmName("李四车工4号2");
		temp1220202.setOrgId("22202");
		temp1220202.setIsPiece("√");
		
		//手工
		Employee temp1230101 = new Employee();
		temp1230101.setUuid(Math.random()*1000 + "");
		temp1230101.setEmNo("22301");
		temp1230101.setEmName("黄手工1号2");
		temp1230101.setOrgId("223");
		temp1230101.setIsPiece("√");
		Employee temp1230102 = new Employee();
		temp1230102.setUuid(Math.random()*1000 + "");
		temp1230102.setEmNo("22302");
		temp1230102.setEmName("黄手工2号2");
		temp1230102.setOrgId("223");
		temp1230102.setIsPiece("√");
		
		//烫工
		Employee temp1240101 = new Employee();
		temp1240101.setUuid(Math.random()*1000 + "");
		temp1240101.setEmNo("22401");
		temp1240101.setEmName("小烫1号");
		temp1240101.setOrgId("224");
		temp1240101.setIsPiece("√");
		Employee temp1240102 = new Employee();
		temp1240102.setUuid(Math.random()*1000 + "");
		temp1240102.setEmNo("22402");
		temp1240102.setEmName("小烫2号");
		temp1240102.setOrgId("224");
		temp1240102.setIsPiece("√");
		//车间2  end 
		
		//车间行管
		Employee temp251 = new Employee();
		temp251.setUuid(Math.random()*1000 + "");
		temp251.setEmNo("3201");
		temp251.setEmName("车管01号");
		temp251.setOrgId("32");
		temp251.setBaseSalary(40000.0);
		Employee temp252 = new Employee();
		temp252.setUuid(Math.random()*1000 + "");
		temp252.setEmNo("3202");
		temp252.setEmName("车管02号");
		temp252.setOrgId("32");
		temp252.setBaseSalary(3000.0);
		
		//行政行管
		Employee temp261 = new Employee();
		temp261.setUuid(Math.random()*1000 + "");
		temp261.setEmNo("3101");
		temp261.setEmName("行政1号");
		temp261.setOrgId("31");
		temp261.setBaseSalary(40000.0);
		Employee temp262 = new Employee();
		temp262.setUuid(Math.random()*1000 + "");
		temp262.setEmNo("3102");
		temp262.setEmName("行政2号");
		temp262.setOrgId("31");
		temp262.setBaseSalary(4000.0);
		
		List<Employee> list = new ArrayList();
		list.add(temp11101);
		list.add(temp221);list.add(temp222);list.add(temp223);list.add(temp224);
		list.add(temp225);list.add(temp226);
		list.add(temp231);list.add(temp232);
		list.add(temp241);list.add(temp242);
		list.add(temp251);list.add(temp252);
		list.add(temp261);list.add(temp262);
		
		list.add(temp12101);
		list.add(temp1220101);list.add(temp1220102);list.add(temp1220201);list.add(temp1220202);
		list.add(temp1230101);list.add(temp1230102);
		list.add(temp1240101);list.add(temp1240102);
		
		for(Employee ee: list){
			ee.setEmState("1");
			Object[] params = {ee.getUuid(),ee.getOrgId(),ee.getEmNo(),ee.getEmName(),ee.getIsPiece(),
					ee.getBaseSalary(),ee.getEmState(),ee.getJob(),ee.getGrade()};
			this.jdbcTemplate.getSimpleJdbcTemplate().update(SqlConstant.ADD_EM,params);
		}
	}

	@Override
	public void removeHistoryEmployee(List<Employee> employees) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		String month = sdf.format(cal.getTime());
		
		StringBuffer emIds = new StringBuffer();
		emIds.append("(");
		for(Employee em: employees){
			
			emIds.append("'").append(em.getUuid()).append("', ");
			
			/*  应该保留历史数据可以查询, 只删除员工表
			//1.清除 员工工资表
			String sqlMonthSalary = "delete from  month_salary where em_id='"+em.getUuid()+"' and month < '"+month+"'";
			this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlMonthSalary);
			
			//2.清除 工时
			String sqlTime = "delete from product_time where em_id='"+em.getUuid()+"' and month < '"+month+"'";
			this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlTime);
			*/
		}
		emIds.append(")");
		
		if(emIds.length()>2){
			String sql = "delete from  employee where uuid  in "+emIds.deleteCharAt(emIds.lastIndexOf(","));
			this.jdbcTemplate.getSimpleJdbcTemplate().update(sql);
		}
	}

	@Override
	public List<Attendance> getAttendace(String orgId, String month) {
//		String sql = " select t.em_no emNo, t.em_name emName, t.time, t.grade From product_time t  "+
//	                 " where t.org_id =  '"+orgId+"' and month = '"+month+"' ORDER BY EM_NO";
		String sql = " select t.em_no emNo, t.em_name emName  From employee t  "+
                " where t.org_id =  '"+orgId+"'  and t.em_state =1 ORDER BY org_id, em_no";
		
		List<Attendance> result = this.jdbcTemplate.queryForList(sql, Attendance.class);
		return result;
	}
	
}