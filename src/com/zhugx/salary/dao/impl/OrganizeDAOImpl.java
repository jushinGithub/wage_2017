package com.zhugx.salary.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zhugx.salary.dao.OrganizeDAO;
import com.zhugx.salary.dao.common.JdbcBaseDAO;
import com.zhugx.salary.dao.common.SqlConstant;
import com.zhugx.salary.pojo.Organize;


@Repository
@SuppressWarnings("all")
public class OrganizeDAOImpl implements OrganizeDAO  {
	public JdbcBaseDAO jdbcTemplate;
	
	public JdbcBaseDAO getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcBaseDAO jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	/**
	 * 通过上级id主键  获取组织部门
	 * @param parentid
	 * @return
	 */
	public List<Organize> getOrgTreeById(String parentid){
		String hsql = "select * FROM Organize where pid ='"+parentid+"' ORDER BY uuid";
		List<Organize> result = this.jdbcTemplate.queryForList(hsql, Organize.class);
		if("-1".equals(parentid) && (result== null ||  result.size() == 0)){
			initOrganize();
			result = this.jdbcTemplate.queryForList(hsql, Organize.class);
		}
		return result;
	}
	
	/**
	 * 初始化部门信息
	 * UUID  [部门编号(根1 车间2 行政3)]  [车间 21|22    裁剪211    车工212(21201 21202 21203) 手工213 烫工214   公司行管 31 车间行管32]   组号[01]  
	 * 编号: 组号 : 取车工数据
	 */
	public void initOrganize(){
		Organize org1 = new Organize();
		org1.setUuid("1");
		org1.setOrgName("XXX服装公司");
		org1.setPid("-1");
		
		//行政组
		Organize org3 = new Organize();
		org3.setUuid("3");
		org3.setOrgNo("3");
		org3.setOrgName("行政部门");
		org3.setPid("1");
		
		Organize org31 = new Organize();
		org31.setUuid("31");
		org31.setOrgNo("31");
		org31.setOrgName("车间行管");
		org31.setPid("3");
		
		Organize org32 = new Organize();
		org32.setUuid("32");
		org32.setOrgNo("32");
		org32.setOrgName("公司行管");
		org32.setPid("3");
		
		List<Organize> listOrg = new ArrayList();
		listOrg.add(org1);  listOrg.add(org3);
		listOrg.add(org31);listOrg.add(org32);
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(int i=0; i<listOrg.size(); i++){
			Object[] param = {listOrg.get(i).getUuid(),listOrg.get(i).getOrgNo(),listOrg.get(i).getOrgName(),
					listOrg.get(i).getGroupNo(),listOrg.get(i).getShop(),  listOrg.get(i).getPid()};
			batchArgs.add(param);
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_ORG, batchArgs);
	}

	@Override
	public List<Organize> getShop() {
		//String sql = "SELECT * FROM organize   WHERE org_no LIKE '2%' AND pid = '1'";
		String sql = "SELECT org_no uuid, org_name FROM organize WHERE pid=1 and uuid like '2%'";
		return this.jdbcTemplate.queryForList(sql, Organize.class);
	}
	
	@Override
	public List<String> getGroupName(String shop,String month) {
		//String sql = "SELECT org_name FROM organize  WHERE uuid  LIKE '2_2%' and shop = '"+shop+"' ORDER BY org_no";
		String sql  = "SELECT org_name FROM organize  WHERE uuid  LIKE '2_2%' and shop = '"+shop+"'  AND org_name IN "+
				      " (SELECT org_name FROM product_data   "+
				      " WHERE MONTH ='"+month+"' AND shop= '"+shop+"' "+
				      " GROUP BY org_name                    "+
				      " HAVING (sum(fetch_nums) > 0  OR sum(complete_nums) > 0)) "+
				      " ORDER BY org_no ";		
		
		List<String> listGroup = this.jdbcTemplate.getJdbcTemplate().queryForList(sql, String.class);
		return listGroup;
	}
	
	@Override
	public List<String> getGroupNameOrigin(String shop,String month) {
		String sql = "SELECT org_name FROM organize  WHERE uuid  LIKE '2_2%' and shop = '"+shop+"' ORDER BY org_no";
		List<String> listGroup = this.jdbcTemplate.getJdbcTemplate().queryForList(sql, String.class);
		return listGroup;
	}
	

	@Override
	public List<String> getGroupNo(String shop) {
		String sql = "SELECT group_no FROM organize  WHERE uuid  LIKE '2_2%' and shop = '"+shop+"' ORDER BY org_name";
		List<String> listGroup = this.jdbcTemplate.getJdbcTemplate().queryForList(sql, String.class);
		return listGroup;
	}

	@Override
	public Map<String, String> getCgOrgid(String shop) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		String sql = "select *  FROM  organize WHERE shop ='"+shop+"' and LENGTH(group_no) > 0";
		List<Organize> list = this.jdbcTemplate.queryForList(sql, Organize.class);
		for(Organize org: list){
			if(org.getGroupNo() != null && !"".equals(org.getGroupNo())){
				result.put(org.getGroupNo(), org.getUuid());
			}
		}
		return result;
	}

	@Override
	public void addTimeOrg(String orgName,String orgId) {
		String addSql = null;
		
		if(orgId != null && orgId.trim().length() > 1){//更新操作
			addSql = "update  organize_base set  org_name = '"+orgName+"' where uuid ='"+orgId+"'";
		}else{
			//先获取部门最大id
			String sql = "select max(uuid) uuid  from organize_base ";
			String groupNo =  this.jdbcTemplate.getSimpleJdbcTemplate().queryForObject(sql, String.class);
			groupNo = Integer.parseInt(groupNo)+1+"";
			
			addSql = "insert into organize_base(uuid, org_no,org_name,pid) VALUES('"+groupNo+"','"+groupNo+"','"+orgName+"',3)";
		}
		 this.jdbcTemplate.getJdbcTemplate().execute(addSql);
	}
	
}