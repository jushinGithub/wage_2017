package com.zhugx.salary.pojo;


/**
 * Employee entity. @author MyEclipse Persistence Tools
 */
public class Employee implements java.io.Serializable {

	// Fields

	private String uuid;
	private String orgId;
	private String emNo;
	private String emName;
	private String job;
	private String isPiece;
	private Double baseSalary;
	private Double grade;
	private String emState;
	

	//extends
	private String orgName;
	private String groupNo;
	private String orgNo;
	private String shop;;

	
	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}
	
	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	
	public String getOrgNo() {
		return orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}
	
	// Constructors
	/** default constructor */
	public Employee() {
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	/** minimal constructor */
	public Employee(String uuid) {
		this.uuid = uuid;
	}

	/** full constructor */
	public Employee(String uuid, String orgId, String emNo, String emName,
			String isPiece, Double baseSalary, String emState) {
		this.uuid = uuid;
		this.orgId = orgId;
		this.emNo = emNo;
		this.emName = emName;
		this.isPiece = isPiece;
		this.baseSalary = baseSalary;
		this.emState = emState;
	}

	// Property accessors
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getEmNo() {
		return this.emNo;
	}

	public void setEmNo(String emNo) {
		this.emNo = emNo;
	}

	public String getEmName() {
		return this.emName;
	}

	public void setEmName(String emName) {
		this.emName = emName;
	}

	public String getIsPiece() {
		return this.isPiece;
	}

	public void setIsPiece(String isPiece) {
		this.isPiece = isPiece;
	}

	public Double getBaseSalary() {
		return this.baseSalary;
	}

	public void setBaseSalary(Double baseSalary) {
		this.baseSalary = baseSalary;
	}

	public String getEmState() {
		return this.emState;
	}

	public void setEmState(String emState) {
		this.emState = emState;
	}

}