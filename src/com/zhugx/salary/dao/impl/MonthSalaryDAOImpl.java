package com.zhugx.salary.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhugx.salary.dao.MonthSalaryDAO;
import com.zhugx.salary.dao.common.JdbcBaseDAO;
import com.zhugx.salary.dao.common.SqlConstant;
import com.zhugx.salary.pojo.Configuration;
import com.zhugx.salary.pojo.Employee;
import com.zhugx.salary.pojo.MonthAnalyze;
import com.zhugx.salary.pojo.MonthSalary;
import com.zhugx.salary.pojo.OrgSalary;
import com.zhugx.salary.pojo.Organize;
import com.zhugx.salary.pojo.ProductData;
import com.zhugx.salary.pojo.ProductPrice;
import com.zhugx.salary.pojo.ProductTime;
import com.zhugx.salary.pojo.Wage;
import com.zhugx.salary.tool.ZhugxUtils;

@SuppressWarnings("all")
public class MonthSalaryDAOImpl  implements MonthSalaryDAO {
	private  JdbcBaseDAO jdbcTemplate;
	
	public JdbcBaseDAO getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcBaseDAO jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	//=============================================================
	// logic method 
	//=============================================================
	
	//1.初始化  工段和员工 工资
	@Override
	public void initOrgSalary(String shop, String month) {
		//1. 先删除本月数据
		String sqlDel = "delete from org_salary where month='"+month+"' and shop='"+shop+"'";
		this.jdbcTemplate.getJdbcTemplate().execute(sqlDel);
		
		//2.获取所有的小组
		String sqlTeam = "select o.*  from organize o where  o.shop='"+shop+"' and pid !=1  order by org_No";
		List<Organize> teams = this.jdbcTemplate.queryForList(sqlTeam, Organize.class);
		
		//3. 获取每个小组的人数
		String sqlEm = "SELECT org_id orgId, count(uuid) sumEm FROM  employee e WHERE e.em_state =1  GROUP BY org_id";
		List<Map<String, Object>> maps = this.jdbcTemplate.getSimpleJdbcTemplate().queryForList(sqlEm);
		HashMap<String, Integer> sumEm = new HashMap<String, Integer>();
		for(Map temp: maps){
			sumEm.put(temp.get("orgId").toString(), Integer.parseInt(temp.get("sumEm").toString()));
		}
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Organize org: teams){
			OrgSalary salary = new OrgSalary();
			salary.setUuid(Math.random()*1000+"");
			salary.setShop(shop);
			salary.setOrgId(org.getUuid());
			salary.setOrgNo(org.getOrgNo());
			salary.setOrgName(org.getOrgName());
			salary.setGroupNo(org.getGroupNo());
			salary.setMonth(month);
			salary.setOpDate(new Date());
			Integer emSum = sumEm.get(org.getUuid()) == null?0:sumEm.get(org.getUuid());
			salary.setEmSum(emSum);
			salary.setKxk(0.0);
			salary.setKzl(0.0);
			salary.setPyf(0.0);
			salary.setZjeOne(0.0);
			salary.setZjeTwo(0.0);
			
			salary.setPieceSum(0.0);
			salary.setPiecePrice(0.0);
			salary.setTimeSum(0.0);
			salary.setPieceFinal(0.0);
			
			Object[] param = {salary.getUuid(),salary.getShop(),salary.getOrgId(),salary.getOrgNo(),salary.getOrgName(),salary.getGroupNo(),
					salary.getEmSum(),salary.getPieceSum(),salary.getTimeSum(),salary.getPiecePrice(),salary.getPieceFinal(),salary.getKxk(),salary.getKzl(),salary.getPyf(),
					salary.getZjeOne(),salary.getZjeTwo(),salary.getMonth(),salary.getOpDate()};
			batchArgs.add(param);
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_ORGSALARY, batchArgs);
	}
	
	@Override
	public void initSalary(String shop, String month) {
		//1.先删除本月工资
		String sqlDel = " delete from Month_Salary where month=? and shop = ?";
		this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlDel, new Object[]{month, shop});
		
		//2. 获取所有员工基本信息
		String sqlEm = "select t.*, o.org_name orgName,o.org_no orgNo,o.group_no groupNo, o.shop "+
		               "From Employee t, organize o where t.org_id=o.uuid and t.em_state=1 and (o.shop='"+shop+"' or shop is null )";
		List<Employee> ems = this.jdbcTemplate.queryForList(sqlEm, Employee.class);
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Employee em:ems){
			//如果是计时员工, 需要先判断是否存在, 因为会有多个车间初始化触发)
			if(em.getShop() == null){
				String sqlEmExsit = "select * from Month_Salary where month='"+month+"' and em_id ='"+em.getUuid()+"'";
				List<MonthSalary> listExsit = this.jdbcTemplate.queryForList(sqlEmExsit, MonthSalary.class);
				if(listExsit!= null && listExsit.size()>0){
					continue;
				}
			}
			MonthSalary salary = new MonthSalary();
			salary.setUuid(Math.random()*1000+"");
			salary.setShop(shop);
			salary.setOrgId(em.getOrgId());
			salary.setOrgName(em.getOrgName());
			salary.setGroupNo(em.getGroupNo());
			salary.setOrgNo(em.getOrgNo());
			salary.setEmId(em.getUuid());
			salary.setEmNo(em.getEmNo());
			salary.setEmName(em.getEmName());
			salary.setJob(em.getJob());
			salary.setIsPiece(em.getIsPiece());
			salary.setBaseSalary(em.getBaseSalary());
			//工龄
			Calendar now = Calendar.getInstance();
			String[] p = month.split("-");
			now.set(Calendar.YEAR, Integer.parseInt(p[0]));
			now.set(Calendar.MONTH, Integer.parseInt(p[1])-1);
			now.set(Calendar.DAY_OF_MONTH, 1); 
			//出勤天数 本月最大天数
			salary.setWorkDay(now.getActualMaximum(Calendar.DATE)+0.0);
			//初始化的时候, 先计算计时间员工的工资
			if(!"√".equals(em.getIsPiece())){
				salary.setTotalPay(em.getBaseSalary());
				salary.setFinalPay(em.getBaseSalary());
				salary.setRealPay(em.getBaseSalary());
			}else{
				salary.setTotalPay(0.0);
				salary.setFinalPay(0.0);
				salary.setRealPay(0.0);
			}
			salary.setTime(0.0);
			salary.setGrade(em.getGrade() == null?0.0:em.getGrade());
			salary.setSumGrade(0.0);
			salary.setPiecePay(0.0);
			salary.setPiecePrice(0.0);
			//设置 默认值  加减项目
			salary.setF01(0.0);
			salary.setF02(0.0);
			salary.setF03(0.0);
			salary.setF04(0.0);
			salary.setF05(0.0);
			salary.setF06(0.0);
			salary.setF07(0.0);
			salary.setF08(0.0);
			salary.setF09(0.0);
			salary.setF10(0.0);
			salary.setF11(0.0);
			salary.setF12(0.0);
			salary.setF13(0.0);
			salary.setF14(0.0);
			salary.setF15(0.0);
			salary.setMonth(month);
			salary.setOpTime(new Date());
			
			Object[] param = {salary.getUuid(),salary.getEmId(),salary.getEmNo(),salary.getEmName(),salary.getShop(),salary.getOrgId(),salary.getOrgNo(),salary.getOrgName(),
				salary.getGroupNo(),salary.getMonth(),salary.getIsPiece(),salary.getBaseSalary(),salary.getPiecePay(),salary.getTotalPay(),salary.getFinalPay(),salary.getF01(),salary.getF02(),salary.getF03(),
				salary.getF04(),salary.getF05(),salary.getF06(),salary.getF07(),salary.getF08(),salary.getF09(),salary.getF10(),salary.getF11(),salary.getF12(),salary.getF13(),
				salary.getF14(),salary.getF15(),salary.getWorkingAge(),salary.getWorkDay(),salary.getTime(),salary.getGrade(),salary.getSumGrade(),salary.getRealPay(),salary.getPiecePrice(),salary.getOpTime(),salary.getJob()};
			batchArgs.add(param);
		}
		
		
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_MONTHSALARY, batchArgs);
	}

	//2. 员工工资
	@Override
	public List<MonthSalary> getSalary(String month, String isPiece,String orgId) {
		String sql = "";
		if("all".equals(isPiece)){
			sql = "select * From Month_Salary  where month ='"+month+"' ";
		}else if("√".equals(isPiece)){
			sql = "select * From Month_Salary  where month ='"+month+"' AND is_Piece = '√' ";
		}else{
			sql = "select * From Month_Salary  where month ='"+month+"' AND (is_Piece != '√' or is_Piece is null )";
		}
		
		if(orgId != null){
			sql += " And  org_id in ("+orgId+")  order by em_no";
		}else{
			sql += " order by em_no";
		}
		
		return this.jdbcTemplate.queryForList(sql, MonthSalary.class);
	}
	
	@Override
	public List<MonthSalary> getOrgSumSalary(String month,String shop, String isPiece) {
		String field =  " sum(base_salary) base_salary, "+
						" sum(real_pay) real_pay, "+
						" sum(piece_pay) piece_pay, "+
						" sum(total_pay) total_pay, "+
						" sum(final_pay) final_pay, "+
						" sum(F01) F01, "+
						" sum(F02) F02, "+
						" sum(F03) F03, "+
						" sum(F04) F04, "+
						" sum(F05) F05, "+
						" sum(F06) F06, "+
						" sum(F07) F07, "+
						" sum(F08) F08, "+
						" sum(F09) F09, "+
						" sum(F10) F10, "+
						" sum(F11) F11, "+
						" sum(F12) F12, "+
						" sum(F13) F13, "+
						" sum(F14) F14, "+
						" sum(F15) F15, "+
						" sum(piece_price) piece_price ";
		String sql = null;
		if(isPiece != null){
			sql = "select org_name, "+field+" From Month_Salary  where shop='"+shop+"' and month ='"+month+"' AND is_Piece = '√'  group by org_name order by org_id";
		}else{
			sql = "select org_name, "+field+" From Month_Salary  where  month ='"+month+"' AND (is_Piece != '√' or is_Piece is null ) group by org_name order by org_id";
		}
		
		return this.jdbcTemplate.queryForList(sql, MonthSalary.class);
	}

	@Override
	public void saveOrUpdateSalary(List<MonthSalary> list, String isPiece) {
		String hql = null;
		if(isPiece != null){
			hql = "select * From Wage where is_Piece = '√'";
		}else{
			hql = "select * From Wage where is_Time = '√'";
		}
		
		Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
		int decimals = config.getDecimals();
		List<Wage> wages = this.jdbcTemplate.queryForList(hql, Wage.class);
		List<String> addMethod = new ArrayList<String>();
		List<String> subMethod = new ArrayList<String>();
		List<String> percentMethod = new ArrayList<String>();
		
		for(Wage wg: wages){
			String getMethod = new StringBuilder().append("get")
					.append(Character.toUpperCase(wg.getWageNo().charAt(0))).append(wg.getWageNo().substring(1)).toString();
			if("1".equals(wg.getSymbol())){
				addMethod.add(getMethod);
			}else if("0".equals(wg.getSymbol())){
				subMethod.add(getMethod);
			}
			
			String setMethod = new StringBuilder().append("set")
					.append(Character.toUpperCase(wg.getWageNo().charAt(0))).append(wg.getWageNo().substring(1)).toString();
			//班组津贴 是总金额的百分比
			if("组长津贴".equals(wg.getWageName().trim()) && wg.getDefaulteValue() != null){
				percentMethod.add(setMethod+"-"+wg.getDefaulteValue()) ;
			}
			
		}
		if(list.size()>0){
			List<Object[]> batchAdd = new ArrayList<Object[]>();
			List<Object[]> batchUpdate = new ArrayList<Object[]>();
			
			for(MonthSalary salary: list){
				//设置更新时间
				salary.setOpTime(new Date());
				//设置实发工资
				Double addSum = 0.0;
				Double subSum = 0.0;
				try {
					//1.先计算set方法
					for(String set: percentMethod){
						if(salary.getOrgId().startsWith("212")){
							//获取最小工号
							String minEmnoSql = "select min(em_no) emNo from employee  where em_state =1 "+
				                    " and em_no is not null and org_id = "+salary.getOrgId();
							String minEmno = ((Employee)(this.jdbcTemplate.queryForList(minEmnoSql, Employee.class).get(0))).getEmNo();
							
							if(salary.getEmNo().equals(minEmno)){
								//获取该组工资的总额
								String orgSalarySql = "select piece_final pieceFinal from org_salary where shop = "+salary.getShop()+" and org_id = '"
								                  +salary.getOrgId()+"' and month= '"+salary.getMonth()+"'";
								Double orgSalary = ((OrgSalary)this.jdbcTemplate.queryForList(orgSalarySql, OrgSalary.class).get(0)).getPieceFinal();
								String[] p = set.split("-");
								orgSalary = orgSalary*(Double.parseDouble(p[1].replace("%", "")))/100;
								
								salary.getClass().getMethod(p[0], new Class[]{Double.class}).invoke(salary, new Object[]{orgSalary});
							}
						}
					}
					
					//2.再计算get方法
					for(String add: addMethod){
						Object obj = salary.getClass().getMethod(add, new Class[]{}).invoke(salary, new Object[]{});
						addSum += obj==null?0.0:(Double)obj;
					}
					for(String sub: subMethod){
						Object obj = salary.getClass().getMethod(sub, new Class[]{}).invoke(salary, new Object[]{});
						subSum += obj==null?0.0:(Double)obj;
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				if("√".equals(salary.getIsPiece())){
					salary.setTotalPay(ZhugxUtils.addDouble(salary.getPiecePay(), addSum,config.getDecimals(), BigDecimal.ROUND_HALF_DOWN)); 
					salary.setFinalPay(ZhugxUtils.subDouble(salary.getTotalPay(), subSum,config.getDecimals(), BigDecimal.ROUND_HALF_DOWN));
				}else{
					//设置 计时工资的 实际工资
					Calendar now = Calendar.getInstance();
					String[] p = salary.getMonth().split("-");
					now.set(Integer.parseInt(p[0]),Integer.parseInt(p[1])-1,1);
					int maxDay = now.getActualMaximum(Calendar.DATE);
					
					//出勤天数 本月最大天数
					Double baseSalary = salary.getBaseSalary()==null?0:salary.getBaseSalary();
					BigDecimal reaPay = new BigDecimal(salary.getWorkDay()*baseSalary).divide(new BigDecimal(maxDay),config.getDecimals(),BigDecimal.ROUND_HALF_DOWN);
					salary.setRealPay(ZhugxUtils.divDouble(salary.getWorkDay()*baseSalary, (double)maxDay, 0, BigDecimal.ROUND_HALF_DOWN));
					salary.setTotalPay(ZhugxUtils.addDouble(salary.getRealPay(), addSum, config.getDecimals(), BigDecimal.ROUND_HALF_DOWN));
					salary.setFinalPay(ZhugxUtils.subDouble(salary.getTotalPay(), subSum, config.getDecimals(), BigDecimal.ROUND_HALF_DOWN));
				}
				
				List<Object[]> batchArgs = new ArrayList<Object[]>();
				if(salary.getUuid() == null){
					Object[] param = {salary.getUuid(),salary.getEmId(),salary.getEmNo(),salary.getEmName(),salary.getShop(),salary.getOrgId(),salary.getOrgNo(),salary.getOrgName(),
							salary.getGroupNo(),salary.getMonth(),salary.getIsPiece(),salary.getBaseSalary(),salary.getPiecePay(),salary.getTotalPay(),salary.getFinalPay(),salary.getF01(),salary.getF02(),salary.getF03(),
							salary.getF04(),salary.getF05(),salary.getF06(),salary.getF07(),salary.getF08(),salary.getF09(),salary.getF10(),salary.getF11(),salary.getF12(),salary.getF13(),
							salary.getF14(),salary.getF15(),salary.getWorkingAge(),salary.getWorkDay(),salary.getTime(),salary.getGrade(),salary.getSumGrade(),salary.getRealPay(),salary.getPiecePrice(),salary.getOpTime(),salary.getJob()};
					batchAdd.add(param);
				}else{
					Object[] param = {salary.getEmId(),salary.getEmNo(),salary.getEmName(),salary.getShop(),salary.getOrgId(),salary.getOrgNo(),salary.getOrgName(),
							salary.getGroupNo(),salary.getMonth(),salary.getIsPiece(),salary.getBaseSalary(),salary.getPiecePay(),salary.getTotalPay(),salary.getFinalPay(),salary.getF01(),salary.getF02(),salary.getF03(),
							salary.getF04(),salary.getF05(),salary.getF06(),salary.getF07(),salary.getF08(),salary.getF09(),salary.getF10(),salary.getF11(),salary.getF12(),salary.getF13(),
							salary.getF14(),salary.getF15(),salary.getWorkingAge(),salary.getWorkDay(),salary.getTime(),salary.getGrade(),salary.getSumGrade(),salary.getRealPay(),salary.getPiecePrice(),salary.getOpTime(),salary.getJob(),salary.getUuid()};
					batchUpdate.add(param);
				}
			}
			if(batchAdd.size()>0){
				this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_MONTHSALARY, batchAdd);
			}
			if(batchUpdate.size()>0){
				this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.UPDATE_MONTHSALARY, batchUpdate);
			}
			
		}
	}
	
	/**
	 * 计算工资
	 * 当 生产数据, 工时分, 工价 发生更改的时候, 计件工资需要重新计算;
	 * @param month
	 */
	public void calculateSalary(String month){
		Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
		//1. 获取所有 计件员工 工资基本信息
		String sqlEm = "select * From Month_Salary where is_Piece='√' AND month='"+month+"'";
		
		List<MonthSalary> ems = this.jdbcTemplate.queryForList(sqlEm, MonthSalary.class);
				
		//2.获取所有计件员工, 工时, 分值, 总分值
		String hqlTime = "select * From Product_Time where month='"+month+"'";
		
		List<ProductTime> times = this.jdbcTemplate.queryForList(hqlTime, ProductTime.class);
		HashMap<String, ProductTime> mapTimes = new HashMap<String, ProductTime>();
		for(ProductTime temp:times){
			mapTimes.put(temp.getEmId(), temp);
		}
		
		//3.获取工时分值
		HashMap<String, Double> timePrice = this.getTimePrice(month);
		
		//4.计算工资
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		List<String> listShop = new ArrayList();
		//每个组的实际总额工资
		Map<String, Double> sumOrgPiecePay = new HashMap<String, Double>();
		for(MonthSalary salary:ems){
			if(!listShop.contains(salary.getShop())){
				listShop.add(salary.getShop());
			}
			ProductTime timeEm = mapTimes.get(salary.getEmId());
			salary.setTime(timeEm.getTime());
			salary.setGrade(timeEm.getGrade());
			salary.setSumGrade(timeEm.getSumGrade());
			//设置计件工资
			Double timePriceR = timePrice.get(salary.getOrgId()) == null?0.0:timePrice.get(salary.getOrgId());
			BigDecimal piecePay = new BigDecimal(timeEm.getSumGrade()*timePriceR).setScale(config.getDecimals(), BigDecimal.ROUND_HALF_DOWN);
			
			salary.setPiecePay(piecePay.doubleValue());
			salary.setRealPay(piecePay.doubleValue());
			
			Object[] param = {salary.getEmId(),salary.getEmNo(),salary.getEmName(),salary.getShop(),salary.getOrgId(),salary.getOrgNo(),salary.getOrgName(),
					salary.getGroupNo(),salary.getMonth(),salary.getIsPiece(),salary.getBaseSalary(),salary.getPiecePay(),salary.getTotalPay(),salary.getFinalPay(),salary.getF01(),salary.getF02(),salary.getF03(),
					salary.getF04(),salary.getF05(),salary.getF06(),salary.getF07(),salary.getF08(),salary.getF09(),salary.getF10(),salary.getF11(),salary.getF12(),salary.getF13(),
					salary.getF14(),salary.getF15(),salary.getWorkingAge(),salary.getWorkDay(),salary.getTime(),salary.getGrade(),salary.getSumGrade(),salary.getRealPay(),salary.getPiecePrice(),salary.getOpTime(),salary.getJob(),salary.getUuid()};
			batchArgs.add(param);
			Double orgPiecePay = sumOrgPiecePay.get(salary.getOrgId())==null? 0:sumOrgPiecePay.get(salary.getOrgId());
			sumOrgPiecePay.put(salary.getOrgId(), orgPiecePay+piecePay.doubleValue());
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.UPDATE_MONTHSALARY, batchArgs);
		
		//计件工资相加不等于的总工资的时候, 把多余的值给组长
		//更新组长计件工资, 所有员工的工资之和  与 工段计件工资的差值  设置给组长的计件工资
		for(String shop: listShop){
			this.updateTeamer(month, shop, sumOrgPiecePay);
		}
	}
	 
	/**
	 * 获取这个月 该组的 工时分值   即: 1工时分有多少值
	 * @param team
	 * @param month
	 * @return
	 */
	public HashMap<String, Double> getTimePrice(String month){
		//裁剪 车工 手工 清减 烫工  通过 组名 判断
		//1.每个小组的总分值
		String sqlTeamGrade = "SELECT ORG_NAME orgName, ORG_ID orgId,SUM(T.SUM_GRADE) SUMGRADE "+
		                      "FROM PRODUCT_TIME T WHERE  T.MONTH = '"+month+"' GROUP BY ORG_ID" ;
		List<ProductTime> list = this.jdbcTemplate.queryForList(sqlTeamGrade, ProductTime.class);
		
		//2. 获取每个小组的总金额(车工组除外)
		String sqlTeamMoney = "SELECT SUM(T.CJJE) CJJE,SUM(T.SGJE) SGJE,SUM(T.QJJE) QJJE,SUM(T.TGJE) TGJE " + 
				              "FROM   PRODUCT_PRICE T WHERE T.MONTH = '"+month+"'" ;
		ProductPrice price = (ProductPrice)this.jdbcTemplate.queryForList(sqlTeamMoney, ProductPrice.class).get(0);
		
		//3.求车工的每个小组的金额
		String priceCJF = "SELECT orgId, sum(fetchNums) cggj  "+ 
						 "FROM (select t.org_id orgId, t.item_id, fetch_Nums*p.cggj fetchNums   "+
						 "from product_data t, product_price p  "+
						 "where t.item_id = p.item_id AND t.month=p.month   AND  t.month = '"+month+"') m  "+
						 "GROUP BY   orgId";
		
		String priceCJC = "SELECT orgId, sum(completeNums) cggj  "+ 
				 "FROM (select t.org_id orgId, t.item_id,  complete_Nums*p.cggj completeNums  "+
				 "from product_data t, product_price p  "+
				 "where t.item_id = p.item_id AND t.month=p.month   AND  t.month = '"+month+"') m  "+
				 "GROUP BY   orgId";
		Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
		int decimals = config.getDecimals();
		List<ProductPrice> listPCj = null;
		if("车工完工数".equals(config.getCgpz())){
			listPCj = this.jdbcTemplate.queryForList(priceCJC, ProductPrice.class);
		}else{
			listPCj = this.jdbcTemplate.queryForList(priceCJF, ProductPrice.class);
		}
		
		HashMap<String, Double> result = new HashMap();
		
		//除车工以外的小组
		for(ProductTime pt: list){
			if(pt.getSumGrade() == 0 || pt.getSumGrade() == null ){
				continue;
			}
			if(pt.getOrgName().indexOf("裁剪") > -1 ){
				result.put(pt.getOrgId(), ZhugxUtils.divDouble(price.getCjje(), pt.getSumGrade(), decimals, BigDecimal.ROUND_HALF_DOWN));
			}
			if(pt.getOrgName().indexOf("手工") > -1){
				result.put(pt.getOrgId(), ZhugxUtils.divDouble(price.getSgje(), pt.getSumGrade(), decimals, BigDecimal.ROUND_HALF_DOWN));
			}
			if(pt.getOrgName().indexOf("烫工") > -1){
				result.put(pt.getOrgId(), ZhugxUtils.divDouble(price.getTgje(), pt.getSumGrade(), decimals, BigDecimal.ROUND_HALF_DOWN));
			}
		}
		//车工小组
		for(ProductPrice pd: listPCj){
			for(ProductTime pt2: list){
				if(pd.getOrgId().equals(pt2.getOrgId())){
					result.put(pd.getOrgId(),ZhugxUtils.divDouble(price.getCggj(), pt2.getSumGrade(), decimals, BigDecimal.ROUND_HALF_DOWN));
				}
			}
		}
		return result;
	}

	//3.工段工资   AND  piece_sum  > 0
	@Override
	public List<OrgSalary> getOrgSalary(String shop, String month){
		String sql = "select * from Org_Salary where month='"+month+
				"' and shop = '"+shop+"' and em_sum > 0   order by org_id";
		return this.jdbcTemplate.queryForList(sql, OrgSalary.class);
	}
	
	@Override
	public OrgSalary getOrgSalaryDept(String month, String orgId) {
		OrgSalary orgSalary = null; 
		String sql = "select * From Org_Salary where org_Id='"+orgId+"' AND month='"+month+"'";
		List<OrgSalary> list =  this.jdbcTemplate.queryForList(sql, OrgSalary.class);
		if(list.size()>0){
			orgSalary = list.get(0);
		}
		return orgSalary;
	}
	
	@Override
	public void saveOrUpdateOrgSalary(List<OrgSalary> list) {
		Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
		
		List<Object[]> batchAdd = new ArrayList<Object[]>();
		List<Object[]> batchUpdate = new ArrayList<Object[]>();
		for(OrgSalary org: list){
			org.setOpDate(new Date());
			//更新实发工资
			Double kxk = org.getKxk()==null?0:org.getKxk();
			Double kzl = org.getKzl()==null?0:org.getKzl();
			Double pyf = org.getPyf()==null?0:org.getPyf();
			Double zjeOne = org.getZjeOne()==null?0:org.getZjeOne();
			Double zjeTwo = org.getZjeTwo()==null?0:org.getZjeTwo();
			BigDecimal pieceFinal = new BigDecimal(org.getPieceSum()-kxk-kzl-pyf+zjeOne+zjeTwo).setScale(config.getDecimals(), BigDecimal.ROUND_HALF_DOWN);
			org.setPieceFinal(pieceFinal.doubleValue());
			//更新更时分价格
			if(org.getTimeSum() > 0){
				//固定6位
				org.setPiecePrice(pieceFinal.divide(new BigDecimal(org.getTimeSum()),6,
						BigDecimal.ROUND_HALF_DOWN).doubleValue());
			}
			
			if(org.getUuid() == null){
				Object[] param = {org.getUuid(),org.getShop(),org.getOrgId(),org.getOrgNo(),org.getOrgName(),org.getGroupNo(),
						org.getEmSum(),org.getPieceSum(),org.getTimeSum(),org.getPiecePrice(),org.getPieceFinal(),org.getKxk(),org.getKzl(),org.getPyf(),
						org.getZjeOne(),org.getZjeTwo(),org.getMonth(),org.getOpDate()};
				batchAdd.add(param);
				
			}else{
				Object[] param = {org.getShop(),org.getOrgId(),org.getOrgNo(),org.getOrgName(),org.getGroupNo(),
						org.getEmSum(),org.getPieceSum(),org.getTimeSum(),org.getPiecePrice(),org.getPieceFinal(),org.getKxk(),org.getKzl(),org.getPyf(),
						org.getZjeOne(),org.getZjeTwo(),org.getMonth(),org.getOpDate(),org.getUuid()};
				batchUpdate.add(param);
				
			}
			
		}
		if(batchAdd.size() > 0){
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_ORGSALARY, batchAdd);
		}
		if(batchUpdate.size() > 0){
			this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.UPDATE_ORGSALARY, batchUpdate);
		}
		
		//更新计件员工工资
		//this.renewalSalary(list.get(0).getShop(),list.get(0).getMonth());
	}
	public void saveOrUpdateOrgSalaryCallback(List<OrgSalary> list){
		//更新计件员工工资
	    this.renewalSalary(list.get(0).getShop(),list.get(0).getMonth());
	}

	//4.前提条件的数据发生更改以后, 需要重新调整工资, 修改了计件工资, 还需要修改 实际计件工资
	//工段的总工资开始, 以后都是取整
	@Override
	public void renewalOrgSalaryPrice(String shop, String month) {
		Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
		String sqlUpdate = "UPDATE  org_salary  t SET t.piece_Sum = ? WHERE t.org_id = ? AND t.month = ? and shop = ?";
		//1. 车工的计件工资
		String sqlCg = "SELECT d.org_id , sum(d.fetch_nums*p.cggj) fetchJe , sum(d.complete_nums*p.cggj) completeJe "+
					   "FROM product_data  d,product_price p "+
					   "WHERE d.item_id = p.item_id  AND d.month=p.month AND d.MONTH= '"+month+
					    "' and d.shop=p.shop and p.shop='"+shop+"' GROUP BY  d.org_id ";
		List<ProductData> listData = this.jdbcTemplate.queryForList(sqlCg, ProductData.class);
		List<Object[]> parameters = new ArrayList<Object[]>();
		
		for(ProductData tempD: listData){
			double pieceSum = 0.0;
			//车工
			if("车工完工数".equals(config.getCgpz())){
				pieceSum = tempD.getCompleteJe();
			}else if("车工领活数".equals(config.getCgpz())){
				pieceSum = tempD.getFetchJe();
			}
			pieceSum =  new BigDecimal(pieceSum).setScale(config.getDecimals(),BigDecimal.ROUND_HALF_DOWN).doubleValue();
			parameters.add(new Object[] {pieceSum,tempD.getOrgId(), month, shop});
		}
		
		//2. 设置裁剪, 手工, 烫工组
		String sqlQt = "SELECT sum(cjje) cjje, sum(sgje) sgje, sum(tgje) tgje FROM product_price t "+
		               "where  month = '"+month+"' and shop ='"+shop+"'";
		List<ProductPrice> listPP = this.jdbcTemplate.queryForList(sqlQt, ProductPrice.class);
		ProductPrice pp = listPP.get(0);
		List<Organize> orgs = this.jdbcTemplate.queryForList("select * From Organize where shop='"+shop+"'",Organize.class);
		for(Organize org: orgs){
			if(org.getOrgName().indexOf("裁剪") != -1){
				Double cjje = new BigDecimal(pp.getCjje()).setScale(config.getDecimals(),BigDecimal.ROUND_HALF_DOWN).doubleValue();
				parameters.add(new Object[] {cjje,org.getUuid(), month, shop});
			}else if(org.getOrgName().indexOf("手工") != -1){
				Double sgje = new BigDecimal(pp.getSgje()).setScale(config.getDecimals(),BigDecimal.ROUND_HALF_DOWN).doubleValue();
				parameters.add(new Object[] {sgje,org.getUuid(), month,shop});
			}else if(org.getOrgName().indexOf("烫工") != -1){
				Double tgje = new BigDecimal(pp.getTgje()).setScale(config.getDecimals(),BigDecimal.ROUND_HALF_DOWN).doubleValue();
				parameters.add(new Object[] {tgje,org.getUuid(), month,shop});
			} 
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(sqlUpdate, parameters);
		
		//3.修改了计件工资 还需要修改 实际计件工资  以及  置工时价(保留位数6位  固定, 是工段工资扣质量, 线款等以后的总工资 除以总工时分)
		List<OrgSalary> orgList = this.jdbcTemplate.queryForList("select * From Org_Salary where month ='"+month+"' and shop = '"+shop+"'",OrgSalary.class);
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(OrgSalary org: orgList){
			Double pieceFinal = org.getPieceSum()-org.getKxk()-org.getKzl()-org.getPyf()+org.getZjeOne()+org.getZjeTwo();
			pieceFinal = new BigDecimal(pieceFinal).setScale(config.getDecimals(),BigDecimal.ROUND_HALF_DOWN).doubleValue();
			
			double piecePrice = ZhugxUtils.divDouble(pieceFinal, org.getTimeSum(), 6, BigDecimal.ROUND_HALF_DOWN);
			
			Object[] param = {org.getShop(),org.getOrgId(),org.getOrgNo(),org.getOrgName(),org.getGroupNo(),
					org.getEmSum(),org.getPieceSum(),org.getTimeSum(),piecePrice,pieceFinal,org.getKxk(),org.getKzl(),org.getPyf(),
					org.getZjeOne(),org.getZjeTwo(),org.getMonth(),new Date(),org.getUuid()};
			batchArgs.add(param);
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.UPDATE_ORGSALARY, batchArgs);
		//4. 更新员工 计件工资
		this.renewalSalary(shop,month);
	}

	@Override
	public void renewalOrgSalaryTime(String shop, String month) {
		Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
		String sqlUpdate = "UPDATE  org_salary  t SET t.time_Sum = ?, t.em_sum=? WHERE t.org_id = ? AND t.month = ? and shop = ?";
		//1. 获取总工时分
		String sqlT = "SELECT org_id, sum(sum_grade) sum_grade, count(em_id) emSum  FROM product_time WHERE  MONTH = '"+month+"' and shop='"+shop+"' GROUP BY org_id ";
		List<ProductTime> list = this.jdbcTemplate.queryForList(sqlT, ProductTime.class);
		List<Object[]> parameters = new ArrayList<Object[]>();
		
		for(ProductTime pt: list){
			parameters.add(new Object[] {pt.getSumGrade(),pt.getEmSum(),pt.getOrgId(), month, shop});
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(sqlUpdate, parameters);
		
		//2.设置工时价
		List<OrgSalary> orgList = this.jdbcTemplate.queryForList("select * From Org_Salary where month ='"+month+"' and shop = '"+shop+"'",OrgSalary.class);
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(OrgSalary org: orgList){
			Double pieceFinal = org.getPieceSum()-org.getKxk()-org.getKzl()-org.getPyf()+org.getZjeOne()+org.getZjeTwo();
			pieceFinal = new BigDecimal(pieceFinal).setScale(config.getDecimals(),BigDecimal.ROUND_HALF_DOWN).doubleValue();
			org.setPieceFinal(pieceFinal);
			
			if(org.getTimeSum() != null && org.getTimeSum() > 0){
				//工时分价,固定6位  
				org.setPiecePrice(new BigDecimal(pieceFinal/org.getTimeSum()).setScale(6,BigDecimal.ROUND_HALF_DOWN).doubleValue());
			}
			
			Object[] param = {org.getShop(),org.getOrgId(),org.getOrgNo(),org.getOrgName(),org.getGroupNo(),
					org.getEmSum(),org.getPieceSum(),org.getTimeSum(),org.getPiecePrice(),org.getPieceFinal(),org.getKxk(),org.getKzl(),org.getPyf(),
					org.getZjeOne(),org.getZjeTwo(),org.getMonth(),new Date(),org.getUuid()};
			batchArgs.add(param);
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.UPDATE_ORGSALARY, batchArgs);
		//3. 更新计件工资
		this.renewalSalary(shop, month);
	}

	
	@Override
	public void renewalSalary(String shop, String month) {
		Configuration config = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
		//1.设置工时价
		String sqlPrice = "UPDATE  month_salary  t SET t.piece_price = (select piece_price FROM org_salary o WHERE  o.org_id = t.org_id and t.month = o.month and t.shop=o.shop)"+
	                      " where t.month ='"+month+"' and shop ='"+shop+"'";
		this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlPrice);
		
		//2. 更新工时分
		String sqlTime = "UPDATE  month_salary  t  left JOIN  product_time p  ON t.em_id=p.em_id AND t.month=p.month and t.shop=p.shop"+
					     " SET t.time = p.time, t.grade=p.grade, t.sum_grade=p.sum_grade where t.month='"+month+"' and t.shop='"+shop+"'";
		this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlTime);
		
		//3.设置计件工资
		String sqlPiece = "select * From Month_Salary where month='"+month+"' and shop='"+shop+"' AND is_Piece = '√'";
		List<MonthSalary> salaryist = this.jdbcTemplate.queryForList(sqlPiece, MonthSalary.class);
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		//每个组的实际总额工资
		Map<String, Double> sumOrgPiecePay = new HashMap<String, Double>();
		for(MonthSalary salary: salaryist){
			Double piecePrice = salary.getPiecePrice()==null?0.0:salary.getPiecePrice();
			Double sumGrade = salary.getSumGrade()==null?0.0:salary.getSumGrade();
			Double piecePay = new BigDecimal(piecePrice*sumGrade).setScale(config.getDecimals(),BigDecimal.ROUND_HALF_DOWN).doubleValue();
			salary.setPiecePay(piecePay);
			
			Object[] param = {salary.getEmId(),salary.getEmNo(),salary.getEmName(),salary.getShop(),salary.getOrgId(),salary.getOrgNo(),salary.getOrgName(),
					salary.getGroupNo(),salary.getMonth(),salary.getIsPiece(),salary.getBaseSalary(),salary.getPiecePay(),salary.getTotalPay(),salary.getFinalPay(),salary.getF01(),salary.getF02(),salary.getF03(),
					salary.getF04(),salary.getF05(),salary.getF06(),salary.getF07(),salary.getF08(),salary.getF09(),salary.getF10(),salary.getF11(),salary.getF12(),salary.getF13(),
					salary.getF14(),salary.getF15(),salary.getWorkingAge(),salary.getWorkDay(),salary.getTime(),salary.getGrade(),salary.getSumGrade(),salary.getRealPay(),salary.getPiecePrice(),salary.getOpTime(),salary.getJob(),salary.getUuid()};
			batchArgs.add(param);
			Double orgPiecePay = sumOrgPiecePay.get(salary.getOrgId())==null? 0:sumOrgPiecePay.get(salary.getOrgId());
			sumOrgPiecePay.put(salary.getOrgId(), orgPiecePay+piecePay);
		}
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.UPDATE_MONTHSALARY, batchArgs);
		
		//5  更新组长计件工资, 所有员工的工资之和  与 工段计件工资的差值  设置给组长的计件工资
		this.updateTeamer(month, shop, sumOrgPiecePay);
				
		//4.  如果部门就只有一个人, 那么车工段的工工资就等于该员工的工资
		List<OrgSalary>  orgSalarys = this.jdbcTemplate.queryForList("select * From Org_Salary where month='"+month+"' and shop ='"+shop+"'",OrgSalary.class);
		for(OrgSalary orgSalry: orgSalarys){
			if(orgSalry.getEmSum()==1){
				String sqlOrg = "UPDATE MONTH_salary t set t.piece_Pay = "+orgSalry.getPieceFinal() + 
						        " where month ='"+month+"' and shop='"+shop+"' and org_id='"+orgSalry.getOrgId()+"'";
				this.jdbcTemplate.getSimpleJdbcTemplate().update(sqlOrg);
			}
		}
		
		
		
		//6.设置实发工资
		List<MonthSalary> list = this.jdbcTemplate.queryForList("select * From Month_Salary where month='"+month+"' and shop='"+shop+"' AND is_Piece = '√'",MonthSalary.class);
		this.saveOrUpdateSalary(list, "√");
	}

	@Override
	public List<MonthAnalyze> getMonthAnalyze(String shop,String month) {
		ArrayList result = new ArrayList();
		String sql ="SELECT t.org_id, t.month, t.org_no,t.org_name,t.em_sum, m.time timeSum, m.grade sumGrade, piece_Price timeSalary, " +
		            " t.piece_sum pieceSum, (t.piece_sum-t.piece_final) koukuanSum, t.piece_final salarySum ,s.piece_pay salaryMax "+
					"FROM org_salary t , "+
					"(select org_id,month,sum(time) TIME, sum(sum_grade) grade FROM product_time  GROUP BY org_id, month ) m, "+
					"(SELECT org_id, month ,max(piece_pay )   piece_pay  FROM MONTH_salary  GROUP BY org_id, month )  s "+
					"WHERE t.org_id=m.org_id AND t.month=m.month  AND  t.org_id=s.org_id AND t.month=s.month    "+
					"AND t.month='"+month+"' and shop='"+shop+"'  ";
		//车工, 
		String sqlCg = sql + " AND t.org_id like '2_2%' order by org_no";
		
		//获取车工完工数
		String sqlCgNum = "SELECT org_id,month, sum(fetch_nums) fetch_nums, sum(complete_nums) complete_nums "+
			      "FROM  product_data where month='"+month+"' and shop='"+shop+"' GROUP BY org_id,month   order by org_id";
		List<ProductData> listCgNum = this.jdbcTemplate.queryForList(sqlCgNum, ProductData.class);
		HashMap<String, Integer> cgNumMap = new HashMap<String, Integer>();
		Configuration conf = (Configuration)this.jdbcTemplate.queryForList(SqlConstant.GET_CONFIG, Configuration.class).get(0);
		int decimals = conf.getDecimals();
		for(ProductData data: listCgNum){
			if("车工完工数".equals(conf.getCgpz())){
				cgNumMap.put(data.getOrgId(), data.getCompleteNums());
			}else{
				cgNumMap.put(data.getOrgId(), data.getFetchNums());
			}
		}
		List<MonthAnalyze> listCg = this.jdbcTemplate.queryForList(sqlCg, MonthAnalyze.class);
		Double pieceSum = 0.0;  //应发
		Double koukuanSum = 0.0; //扣款
		Double salarySum = 0.0; //实发 = 应发-扣款
		
		Integer emSum = 0;
		Double evgSalary = 0.0;
		Double timeSum = 0.0;
		Double timeSalary = 0.0;
		Double sumGrade = 0.0;
		Double gradeSalary = 0.0;
		Integer sumItem = 0;
		Double evgPrice = 0.0;
		for(MonthAnalyze analyze: listCg){
			Double  pieceSumTemp = analyze.getPieceSum() == null?0.0:analyze.getPieceSum();
			Double  koukuanSumTemp = analyze.getKoukuanSum() == null?0.0:analyze.getKoukuanSum();
			Double  salarySumTemp = analyze.getSalarySum() == null?0.0:analyze.getSalarySum();
			
			//先计算  分值工资  工时工资  人均工资  产品数量  平均工价
			if(analyze.getEmSum() !=null && analyze.getEmSum() != 0 ){
				analyze.setEvgSalary(ZhugxUtils.divDouble(salarySumTemp, (double)analyze.getEmSum(), 0, BigDecimal.ROUND_HALF_DOWN));
			}else{
				analyze.setEvgSalary(0.0);
			}
			
//			if(analyze.getTimeSum() !=null && analyze.getTimeSum() != 0){
//				analyze.setTimeSalary(ZhugxUtils.divDouble(salarySumTemp, (double)analyze.getTimeSum(), 2, BigDecimal.ROUND_HALF_DOWN));
//			}else{
//				analyze.setTimeSalary(0.0);
//			}
			
			if(analyze.getSumGrade()!= null && analyze.getSumGrade() != 0){
				analyze.setGradeSalary(ZhugxUtils.divDouble(salarySumTemp, (double)analyze.getSumGrade(), 2, BigDecimal.ROUND_HALF_DOWN));
			}else{
				analyze.setGradeSalary(0.0);
			}
			analyze.setSumItem(cgNumMap.get(analyze.getOrgId()) == null?0:cgNumMap.get(analyze.getOrgId()));
			if(analyze.getSumItem() != null && analyze.getSumItem() != 0){
				analyze.setEvgPrice(ZhugxUtils.divDouble(pieceSumTemp, (double)analyze.getSumItem(), 2, BigDecimal.ROUND_HALF_DOWN));
			}else{
				analyze.setEvgPrice(0.0);
			}
			
			//计算合计值
			pieceSum += pieceSumTemp;
			koukuanSum += koukuanSumTemp;
			salarySum += salarySumTemp;
			
			emSum += analyze.getEmSum()==null?0:analyze.getEmSum();
			evgSalary += analyze.getEvgSalary()==null?0.0:analyze.getEvgSalary();
			timeSum += analyze.getTimeSum()==null?0.0:analyze.getTimeSum();
			timeSalary += analyze.getTimeSalary()==null?0.0:analyze.getTimeSalary();
			sumGrade += analyze.getSumGrade()==null?0.0:analyze.getSumGrade();
			gradeSalary += analyze.getGradeSalary()==null?0.0:analyze.getGradeSalary();
			sumItem += analyze.getSumItem()==null?0:analyze.getSumItem();
			evgPrice += analyze.getEvgPrice()==null?0.0:analyze.getEvgPrice();
		}
		MonthAnalyze cgSum = new MonthAnalyze(); //车工合计行
		cgSum.setOrgName("车工段小计");
		cgSum.setSalarySum(salarySum);
		cgSum.setPieceSum(pieceSum);
		cgSum.setKoukuanSum(koukuanSum);
		cgSum.setEmSum(emSum);
		
		cgSum.setEvgSalary(ZhugxUtils.divDouble(evgSalary, (double)listCg.size(), 0, BigDecimal.ROUND_HALF_DOWN));
		cgSum.setTimeSum(timeSum);
		cgSum.setTimeSalary(ZhugxUtils.divDouble(timeSalary, (double)listCg.size(), 2, BigDecimal.ROUND_HALF_DOWN));
		cgSum.setSumGrade(sumGrade);
		cgSum.setGradeSalary(ZhugxUtils.divDouble(gradeSalary, (double)listCg.size(), 2, BigDecimal.ROUND_HALF_DOWN));
		cgSum.setSumItem(sumItem);
		cgSum.setEvgPrice(ZhugxUtils.divDouble(evgPrice, (double)listCg.size(), 2, BigDecimal.ROUND_HALF_DOWN));
		
		
		//其他工段 
		 pieceSum = 0.0;  //应发
		 koukuanSum = 0.0; //扣款
		 salarySum = 0.0; //实发 = 应发-扣款
		 
		 emSum = 0;
		 evgSalary = 0.0;
		 timeSum = 0.0;
		 timeSalary = 0.0;
		 sumGrade = 0.0;
		 gradeSalary = 0.0;
		 sumItem = 0;
		 evgPrice = 0.0;
		 Double cjTimeSum = 0.0, cjGradeSum=0.0, cjSalarySum=0.0, cjKoukuanSum=0.0, cjPieceSum=0.0;
		 Double sgTimeSum = 0.0, sgGradeSum=0.0, sgSalarySum=0.0, sgKoukuanSum=0.0, sgPieceSum=0.0;
		 Double tgTimeSum = 0.0, tgGradeSum=0.0, tgSalarySum=0.0, tgKoukuanSum=0.0, tgPieceSum=0.0;
		
		String sqlQt = sql + " AND t.org_id not  like '2_2%'  order by org_id  ";
		String sqlNum = "SELECT sum(cjsl) cjsl, sum(sgsl) sgsl, sum(tgsl) tgsl  FROM product_price  WHERE MONTH = '"+month+"'";
		ProductPrice qtNum = (ProductPrice)this.jdbcTemplate.queryForList(sqlNum, ProductPrice.class).get(0);
		List<MonthAnalyze> listQt = this.jdbcTemplate.queryForList(sqlQt, MonthAnalyze.class);
		
		for(MonthAnalyze analyze: listQt){
			Double  pieceSumTemp = analyze.getPieceSum() == null?0.0:analyze.getPieceSum();
			Double  koukuanSumTemp = analyze.getKoukuanSum() == null?0.0:analyze.getKoukuanSum();
			Double  salarySumTemp = analyze.getSalarySum() == null?0.0:analyze.getSalarySum();
			
			//先计算  分值工资  工时工资  人均工资  产品数量  平均工价
			if(analyze.getEmSum() !=null && analyze.getEmSum() != 0){
				analyze.setEvgSalary(ZhugxUtils.divDouble(salarySumTemp, (double)analyze.getEmSum(), 0, BigDecimal.ROUND_HALF_DOWN));
			}else{
				analyze.setEvgSalary(0.0);
			}
			
//			if(analyze.getTimeSum() !=null && analyze.getTimeSum() != 0){
//				analyze.setTimeSalary(ZhugxUtils.divDouble(salarySumTemp, (double)analyze.getTimeSum(), 2, BigDecimal.ROUND_HALF_DOWN));
//			}else{
//				analyze.setTimeSalary(0.0);
//			}
			
			if(analyze.getSumGrade() !=null && analyze.getSumGrade() != 0){
				analyze.setGradeSalary(ZhugxUtils.divDouble(salarySumTemp, (double)analyze.getSumGrade(), 2, BigDecimal.ROUND_HALF_DOWN));
			}else{
				analyze.setGradeSalary(0.0);
			}
			if(analyze.getOrgName().indexOf("裁剪") != -1){
				analyze.setSumItem(qtNum.getCjsl() == null?0:qtNum.getCjsl());
				cjTimeSum = analyze.getTimeSum()==null?0.0:analyze.getTimeSum(); 
				cjGradeSum= analyze.getSumGrade()==null?0.0:analyze.getSumGrade();
				cjSalarySum=salarySumTemp; 
				cjPieceSum=pieceSumTemp;
				cjKoukuanSum=koukuanSumTemp;
			}else  if(analyze.getOrgName().indexOf("手工") != -1){
				analyze.setSumItem(qtNum.getSgsl() == null?0:qtNum.getSgsl());
				sgTimeSum = analyze.getTimeSum()==null?0.0:analyze.getTimeSum(); 
				sgGradeSum= analyze.getSumGrade()==null?0.0:analyze.getSumGrade();
				sgSalarySum=salarySumTemp; 
				sgPieceSum=pieceSumTemp;
				sgKoukuanSum=koukuanSumTemp;
			}else if(analyze.getOrgName().indexOf("烫工") != -1){
				analyze.setSumItem(qtNum.getTgsl() == null?0:qtNum.getTgsl());
				tgTimeSum = analyze.getTimeSum()==null?0.0:analyze.getTimeSum(); 
				tgGradeSum= analyze.getSumGrade()==null?0.0:analyze.getSumGrade();
				tgSalarySum=salarySumTemp; 
				tgPieceSum=pieceSumTemp;
				tgKoukuanSum=koukuanSumTemp;
			}
			
			if(analyze.getSumItem()!=null && analyze.getSumItem() != 0){
				
				analyze.setEvgPrice(ZhugxUtils.divDouble(pieceSumTemp, (double)analyze.getSumItem(), 2, BigDecimal.ROUND_HALF_DOWN));
			}else{
				analyze.setEvgPrice(0.0);
			} 
			
			//计算合计值
			pieceSum += pieceSumTemp;
			koukuanSum += koukuanSumTemp;
			salarySum += salarySumTemp;
			
			emSum += analyze.getEmSum()==null?0:analyze.getEmSum();
			evgSalary += analyze.getEvgSalary()==null?0.0:analyze.getEvgSalary();
			timeSum += analyze.getTimeSum()==null?0.0:analyze.getTimeSum();
			timeSalary += analyze.getTimeSalary()==null?0.0:analyze.getTimeSalary();
			sumGrade += analyze.getSumGrade()==null?0.0:analyze.getSumGrade();
			gradeSalary += analyze.getGradeSalary()==null?0.0:analyze.getGradeSalary();
			sumItem += analyze.getSumItem()==null?0:analyze.getSumItem();
			evgPrice += analyze.getEvgPrice()==null?0.0:analyze.getEvgPrice();
		}
		MonthAnalyze qtSum = new MonthAnalyze(); //其他合计行
		qtSum.setOrgName("其他段小计");
		qtSum.setSalarySum(salarySum);
		qtSum.setKoukuanSum(koukuanSum);
		qtSum.setPieceSum(pieceSum);
		qtSum.setEmSum(emSum);
		qtSum.setEvgSalary(ZhugxUtils.divDouble(evgSalary, (double)listCg.size(), 0, BigDecimal.ROUND_HALF_DOWN));
		qtSum.setTimeSum(timeSum);
		qtSum.setTimeSalary(ZhugxUtils.divDouble(timeSalary, (double)listCg.size(), 2, BigDecimal.ROUND_HALF_DOWN));
		qtSum.setSumGrade(sumGrade);
		qtSum.setGradeSalary(ZhugxUtils.divDouble(gradeSalary, (double)listCg.size(), 2, BigDecimal.ROUND_HALF_DOWN));
		qtSum.setSumItem(sumItem);
		qtSum.setEvgPrice(ZhugxUtils.divDouble(evgPrice, (double)listCg.size(), 2, BigDecimal.ROUND_HALF_DOWN));
		
		MonthAnalyze scSum = new MonthAnalyze(); //生产合计
		scSum.setOrgName("生产合计");
		scSum.setSalarySum(ZhugxUtils.addDouble(cgSum.getSalarySum(),qtSum.getSalarySum()));
		scSum.setKoukuanSum(ZhugxUtils.addDouble(cgSum.getKoukuanSum(),qtSum.getKoukuanSum()));
		scSum.setPieceSum(ZhugxUtils.addDouble(cgSum.getPieceSum(),qtSum.getPieceSum()));
		
		/*
		scSum.setEmSum(cgSum.getEmSum()+qtSum.getEmSum());
		scSum.setEvgSalary(ZhugxUtils.addDouble(cgSum.getEvgSalary(),qtSum.getEvgSalary()));
		scSum.setTimeSum(ZhugxUtils.addDouble(cgSum.getTimeSum(),qtSum.getTimeSum()));
		scSum.setTimeSalary(ZhugxUtils.addDouble(cgSum.getTimeSalary(),qtSum.getTimeSalary()));
		scSum.setSumGrade(ZhugxUtils.addDouble(cgSum.getSumGrade(),qtSum.getSumGrade()));
		scSum.setGradeSalary(ZhugxUtils.addDouble(cgSum.getGradeSalary(),qtSum.getGradeSalary()));
		scSum.setSumItem(cgSum.getSumItem()+qtSum.getSumItem());
		scSum.setEvgPrice(ZhugxUtils.addDouble(cgSum.getEvgPrice(), qtSum.getEvgPrice()));
		*/
		
		//设置比例值
		MonthAnalyze cjBl = new MonthAnalyze(); //裁剪比例
		cjBl.setOrgName("裁段占车工%");
		if(cgSum.getSalarySum() != 0){
			//cjBl.setSalarySum(ZhugxUtils.divDouble(cjSalarySum, cgSum.getSalarySum(), 4, BigDecimal.ROUND_HALF_DOWN));
			//cjBl.setKoukuanSum(ZhugxUtils.divDouble(cjKoukuanSum, cgSum.getKoukuanSum(), 4, BigDecimal.ROUND_HALF_DOWN));
			cjBl.setPieceSum(ZhugxUtils.divDouble(cjPieceSum*100, cgSum.getPieceSum(), 4, BigDecimal.ROUND_HALF_DOWN));
		}
//		if(cgSum.getTimeSum() != 0){
//			cjBl.setTimeSum(ZhugxUtils.divDouble(cjTimeSum, cgSum.getTimeSum(), 4, BigDecimal.ROUND_HALF_DOWN));
//		}
//		if(cgSum.getSumGrade() != 0){
//			cjBl.setSumGrade(ZhugxUtils.divDouble(cjGradeSum, cgSum.getSumGrade(), 4, BigDecimal.ROUND_HALF_DOWN));
//		}
		
		MonthAnalyze sgBl = new MonthAnalyze(); //手工比例
		sgBl.setOrgName("手工占车工%");
		if(cgSum.getSalarySum() != 0){
			//sgBl.setSalarySum(ZhugxUtils.divDouble(sgSalarySum, cgSum.getSalarySum(), 4, BigDecimal.ROUND_HALF_DOWN));
			//sgBl.setKoukuanSum(ZhugxUtils.divDouble(sgKoukuanSum, cgSum.getSalarySum(), 4, BigDecimal.ROUND_HALF_DOWN));
			sgBl.setPieceSum(ZhugxUtils.divDouble(sgPieceSum*100, cgSum.getSalarySum(), 4, BigDecimal.ROUND_HALF_DOWN));
		}
//		if(cgSum.getTimeSum() != 0){
//			sgBl.setTimeSum(ZhugxUtils.divDouble(sgTimeSum, cgSum.getTimeSum(), 4, BigDecimal.ROUND_HALF_DOWN));
//		}
//		if(cgSum.getSumGrade() != 0){
//			sgBl.setSumGrade(ZhugxUtils.divDouble(sgGradeSum, cgSum.getSumGrade(), 4, BigDecimal.ROUND_HALF_DOWN));
//		}
		MonthAnalyze tgBl = new MonthAnalyze(); //烫工比例
		tgBl.setOrgName("烫工占车工%");
		if(cgSum.getSalarySum() != 0){
			//tgBl.setSalarySum(ZhugxUtils.divDouble(tgSalarySum, cgSum.getSalarySum(), 4, BigDecimal.ROUND_HALF_DOWN));
			//tgBl.setKoukuanSum(ZhugxUtils.divDouble(tgKoukuanSum, cgSum.getKoukuanSum(), 4, BigDecimal.ROUND_HALF_DOWN));
			tgBl.setPieceSum(ZhugxUtils.divDouble(tgPieceSum*100, cgSum.getPieceSum(), 4, BigDecimal.ROUND_HALF_DOWN));
		}
//		if(cgSum.getTimeSum() != 0){
//			tgBl.setTimeSum(ZhugxUtils.divDouble(tgTimeSum, cgSum.getTimeSum(), 4, BigDecimal.ROUND_HALF_DOWN));
//		}
//		if(cgSum.getSumGrade() != 0){
//			tgBl.setSumGrade(ZhugxUtils.divDouble(tgGradeSum, cgSum.getSumGrade(), 4, BigDecimal.ROUND_HALF_DOWN));
//		}
		
		
		result.add(0, tgBl);
		result.add(0, sgBl);
		result.add(0, cjBl);
		result.add(0, scSum);
		//result.add(0, qtSum);
		result.addAll(0, listQt);
		result.add(0, cgSum);
		result.addAll(0, listCg);
		return result;
	}
	
	/**
	 * 更新组长工资
	 */
	public void updateTeamer(String month, String shop, Map<String, Double> sumOrgPiecePay){
		String sqlTeam = "UPDATE  MONTH_salary  x SET x.piece_pay  = x.piece_pay +?  "+
		                "WHERE x.org_id=? AND month = ? and shop=? and x.piece_pay > 0"+
		                " AND x.em_no = (select min(e.em_no) FROM employee e WHERE e.org_id = x.org_id  and e.em_no is not null  and e.em_state = 1)";
		//获取工段的总工资
		String sqlOrgFinal = "select * From org_salary where month='"+month+"' and shop='"+shop+"'";
		List<OrgSalary> orgFinal = this.jdbcTemplate.queryForList(sqlOrgFinal, OrgSalary.class);
		List<Object[]> parameters = new ArrayList<Object[]>();
		for(OrgSalary teamSalary: orgFinal){
			Double realPiecePay = sumOrgPiecePay.get(teamSalary.getOrgId());
			if(realPiecePay != null && teamSalary.getPieceFinal() - realPiecePay != 0){
				parameters.add(new Object[]{teamSalary.getPieceFinal() - realPiecePay, teamSalary.getOrgId(),month, shop});
			}
		}
		this.jdbcTemplate.getJdbcTemplate().batchUpdate(sqlTeam, parameters);
	}

	@Override
	public void calTimeSalary(String orgId, String month) {
		
		//1.删除离职的员工
		String sqlDel = "delete from  month_salary  where org_id in ("+orgId+")  and month='"+month+"'  AND (is_Piece != '√' or is_Piece is null )  "+
                        " and em_id  not in  (select uuid from employee where org_id in ("+orgId+") and em_state=1 )  " ;
		this.jdbcTemplate.getJdbcTemplate().execute(sqlDel);
		
		
		//2. 插入新增的员工
		this.initSalaryUpdate(orgId, month);
		
		//3.更新基本工资
		String sql = " update  month_salary  t INNER JOIN  employee e  on  e.org_id=t.org_id and e.uuid = t.em_id  "+
					 "	set t.base_salary = e.base_salary, t.em_name=e.em_name   "+
				     "  where t.org_id in("+orgId+")  and t.month='"+month+"'    AND (t.is_Piece != '√' or t.is_Piece is null )  " ;
		this.jdbcTemplate.getJdbcTemplate().update(sql);
		
		//4. 获取这个小组的所有数据, 然后调用更新
		List<MonthSalary> list = this.getSalary(month, null, orgId);
		this.saveOrUpdateSalary(list,null);
	}
	
	//点击重新获取的时候更新, 新增的员工
	public void initSalaryUpdate(String orgId, String month) {
		//2. 获取所有员工基本信息
		String sqlEm = "select t.*, o.org_name orgName,o.org_no orgNo,o.group_no groupNo, o.shop "+
		               " From Employee t, organize o where t.org_id=o.uuid and t.em_state=1 and t.org_id in ("+orgId+") " +
				       " AND t.uuid not in (select em_id from month_salary where month='"+month+"' and org_id  in ("+orgId+") )";
		List<Employee> ems = this.jdbcTemplate.queryForList(sqlEm, Employee.class);
		
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for(Employee em:ems){
			
			MonthSalary salary = new MonthSalary();
			salary.setUuid(Math.random()*1000+"");
			salary.setShop(em.getShop());
			salary.setOrgId(em.getOrgId());
			salary.setOrgName(em.getOrgName());
			salary.setGroupNo(em.getGroupNo());
			salary.setOrgNo(em.getOrgNo());
			salary.setEmId(em.getUuid());
			salary.setEmNo(em.getEmNo());
			salary.setEmName(em.getEmName());
			salary.setJob(em.getJob());
			salary.setIsPiece(em.getIsPiece());
			salary.setBaseSalary(em.getBaseSalary());
			//工龄
			Calendar now = Calendar.getInstance();
			String[] p = month.split("-");
			now.set(Calendar.YEAR, Integer.parseInt(p[0]));
			now.set(Calendar.MONTH, Integer.parseInt(p[1])-1);
			now.set(Calendar.DAY_OF_MONTH, 1); 
			//出勤天数 本月最大天数
			salary.setWorkDay(now.getActualMaximum(Calendar.DATE)+0.0);
			//初始化的时候, 先计算计时间员工的工资
			if(!"√".equals(em.getIsPiece())){
				salary.setTotalPay(em.getBaseSalary());
				salary.setFinalPay(em.getBaseSalary());
				salary.setRealPay(em.getBaseSalary());
			}else{
				salary.setTotalPay(0.0);
				salary.setFinalPay(0.0);
				salary.setRealPay(0.0);
			}
			salary.setTime(0.0);
			salary.setGrade(em.getGrade() == null?0.0:em.getGrade());
			salary.setSumGrade(0.0);
			salary.setPiecePay(0.0);
			salary.setPiecePrice(0.0);
			//设置 默认值  加减项目
			salary.setF01(0.0);
			salary.setF02(0.0);
			salary.setF03(0.0);
			salary.setF04(0.0);
			salary.setF05(0.0);
			salary.setF06(0.0);
			salary.setF07(0.0);
			salary.setF08(0.0);
			salary.setF09(0.0);
			salary.setF10(0.0);
			salary.setF11(0.0);
			salary.setF12(0.0);
			salary.setF13(0.0);
			salary.setF14(0.0);
			salary.setF15(0.0);
			salary.setMonth(month);
			salary.setOpTime(new Date());
			
			Object[] param = {salary.getUuid(),salary.getEmId(),salary.getEmNo(),salary.getEmName(),salary.getShop(),salary.getOrgId(),salary.getOrgNo(),salary.getOrgName(),
				salary.getGroupNo(),salary.getMonth(),salary.getIsPiece(),salary.getBaseSalary(),salary.getPiecePay(),salary.getTotalPay(),salary.getFinalPay(),salary.getF01(),salary.getF02(),salary.getF03(),
				salary.getF04(),salary.getF05(),salary.getF06(),salary.getF07(),salary.getF08(),salary.getF09(),salary.getF10(),salary.getF11(),salary.getF12(),salary.getF13(),
				salary.getF14(),salary.getF15(),salary.getWorkingAge(),salary.getWorkDay(),salary.getTime(),salary.getGrade(),salary.getSumGrade(),salary.getRealPay(),salary.getPiecePrice(),salary.getOpTime(),salary.getJob()};
			batchArgs.add(param);
		}
		
		this.jdbcTemplate.getSimpleJdbcTemplate().batchUpdate(SqlConstant.ADD_MONTHSALARY, batchArgs);
	}

}
