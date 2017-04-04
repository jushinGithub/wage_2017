package com.zhugx.salary.pojo;

import java.util.Date;

/**
 * ProductData entity. @author MyEclipse Persistence Tools
 */
public class ProductData implements java.io.Serializable {

	// Fields

	private String uuid;
	private String itemId;//货物主键
	private String itemNo;//货号
	private String itemName;//品名
	private Integer fetchNums;//领用数
	private Integer completeNums;//完工数
	private Integer cjsl; //裁剪数
	private Integer sgsl; //手工数
	private Integer tgsl; //烫工数
	private String shop; //车间
	private String orgId;//部门id
	private String orgNo;//部门编号
	private String orgName;//部门名称
	private String groupNo;//小组
	private String month; //月份  2016-03 字符串
	private Date opDate;
	
	//extends 
	private Double fetchJe;
	private Double completeJe;
	
	public Integer getCjsl() {
		return cjsl;
	}
	public void setCjsl(Integer cjsl) {
		this.cjsl = cjsl;
	}
	public Integer getSgsl() {
		return sgsl;
	}
	public void setSgsl(Integer sgsl) {
		this.sgsl = sgsl;
	}
	public Integer getTgsl() {
		return tgsl;
	}
	public void setTgsl(Integer tgsl) {
		this.tgsl = tgsl;
	}
	
	public Double getFetchJe() {
		return fetchJe;
	}
	public void setFetchJe(Double fetchJe) {
		this.fetchJe = fetchJe;
	}
	public Double getCompleteJe() {
		return completeJe;
	}

	public void setCompleteJe(Double completeJe) {
		this.completeJe = completeJe;
	}
	
	// Constructors
	/** default constructor */
	public ProductData() {
	}

	/** minimal constructor */
	public ProductData(String uuid) {
		this.uuid = uuid;
	}

	/** full constructor */
	public ProductData(String uuid, String itemId, String itemNo,
			String itemName, Integer fetchNums,
			Integer completeNums, String orgId, String orgName, String orgNo,String groupNo,
			String month, Date opDate) {
		this.uuid = uuid;
		this.itemId = itemId;
		this.itemNo = itemNo;
		this.itemName = itemName;
		this.fetchNums = fetchNums;
		this.completeNums = completeNums;
		this.orgId = orgId;
		this.orgName = orgName;
		this.orgNo = orgNo;
		this.groupNo = groupNo;
		this.month = month;
		this.opDate = opDate;
	}

	// Property accessors
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public String getOrgNo() {
		return orgNo;
	}
	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}
	
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getFetchNums() {
		return this.fetchNums;
	}

	public void setFetchNums(Integer fetchNums) {
		this.fetchNums = fetchNums;
	}

	public Integer getCompleteNums() {
		return this.completeNums;
	}

	public void setCompleteNums(Integer completeNums) {
		this.completeNums = completeNums;
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

	public String getGroupNo() {
		return this.groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
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