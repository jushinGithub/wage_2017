package com.zhugx.salary.pojo;

/**
 * 月工资分析表
 * @author jushin
 *
 */
public class MonthAnalyze {
	String  month;   //月份
	String  orgName; //组名
	Integer emSum;   //人数
	Double  timeSum; //总工时
	Double  sumGrade;//总分值
	
	Double  pieceSum;//工资总额(应发发)
	Double  koukuanSum;//扣款合计
	Double  salarySum;//工资总额(实发) 
	
	Double  salaryMax;//最高工资
	Double  gradeSalary;//分值工资
	Double  timeSalary;//工时工资
	Double  evgSalary; //人均工资
	Integer  sumItem;   //产品数量
	Double  evgPrice;  //平均工价
	
	String orgId;
	
	
	public Double getKoukuanSum() {
		return koukuanSum;
	}
	public void setKoukuanSum(Double koukuanSum) {
		this.koukuanSum = koukuanSum;
	}
	public Double getPieceSum() {
		return pieceSum;
	}
	public void setPieceSum(Double pieceSum) {
		this.pieceSum = pieceSum;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Integer getEmSum() {
		return emSum;
	}
	public void setEmSum(Integer emSum) {
		this.emSum = emSum;
	}
	public Double getTimeSum() {
		return timeSum;
	}
	public void setTimeSum(Double timeSum) {
		this.timeSum = timeSum;
	}
	public Double getSumGrade() {
		return sumGrade;
	}
	public void setSumGrade(Double sumGrade) {
		this.sumGrade = sumGrade;
	}
	public Double getSalarySum() {
		return salarySum;
	}
	public void setSalarySum(Double salarySum) {
		this.salarySum = salarySum;
	}
	public Double getSalaryMax() {
		return salaryMax;
	}
	public void setSalaryMax(Double salaryMax) {
		this.salaryMax = salaryMax;
	}
	public Double getGradeSalary() {
		return gradeSalary;
	}
	public void setGradeSalary(Double gradeSalary) {
		this.gradeSalary = gradeSalary;
	}
	public Double getTimeSalary() {
		return timeSalary;
	}
	public void setTimeSalary(Double timeSalary) {
		this.timeSalary = timeSalary;
	}
	public Double getEvgSalary() {
		return evgSalary;
	}
	public void setEvgSalary(Double evgSalary) {
		this.evgSalary = evgSalary;
	}
	public Integer getSumItem() {
		return sumItem;
	}
	public void setSumItem(Integer sumItem) {
		this.sumItem = sumItem;
	}
	public Double getEvgPrice() {
		return evgPrice;
	}
	public void setEvgPrice(Double evgPrice) {
		this.evgPrice = evgPrice;
	}
	
}
