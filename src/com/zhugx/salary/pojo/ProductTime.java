package com.zhugx.salary.pojo;

import java.util.Date;

/**
 * ProductTime entity. @author MyEclipse Persistence Tools
 */
public class ProductTime implements java.io.Serializable {

	// Fields

	private String uuid;
	private String emId;
	private String emNo;
	private String emName;
	private String groupNo;
	private Double time;
	private Double grade;
	private Double sumGrade;
	private String shop;
	private String orgId;
	private String orgName;
	private String month;
	private Date opDate;
	
	//extends
	private Integer emSum;

	// Constructors

	public Integer getEmSum() {
		return emSum;
	}

	public void setEmSum(Integer emSum) {
		this.emSum = emSum;
	}

	/** default constructor */
	public ProductTime() {
	}

	/** minimal constructor */
	public ProductTime(String uuid) {
		this.uuid = uuid;
	}

	/** full constructor */
	public ProductTime(String uuid, String emId, String emNo, String emName,
			String groupNo, Double time, Double grade, Double sumGrade,
			String orgId, String orgName, String month, Date opDate) {
		this.uuid = uuid;
		this.emId = emId;
		this.emNo = emNo;
		this.emName = emName;
		this.groupNo = groupNo;
		this.time = time;
		this.grade = grade;
		this.sumGrade = sumGrade;
		this.orgId = orgId;
		this.orgName = orgName;
		this.month = month;
		this.opDate = opDate;
	}

	// Property accessors
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getEmId() {
		return this.emId;
	}

	public void setEmId(String emId) {
		this.emId = emId;
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

	public String getGroupNo() {
		return this.groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public Double getTime() {
		return this.time;
	}

	public void setTime(Double time) {
		this.time = time;
	}

	public Double getGrade() {
		return this.grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public Double getSumGrade() {
		return this.sumGrade;
	}

	public void setSumGrade(Double sumGrade) {
		this.sumGrade = sumGrade;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Date getOpDate() {
		return this.opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

}