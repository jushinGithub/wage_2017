package com.zhugx.salary.pojo;


/**
 * Organize entity. @author MyEclipse Persistence Tools
 */
public class Organize implements java.io.Serializable {

	// Fields

	private String uuid;
	private String orgNo; 
	private String orgName;
	private String groupNo;
	private String shop;
	private String pid;

	// Constructors

	/** default constructor */
	public Organize() {
	}

	/** minimal constructor */
	public Organize(String uuid) {
		this.uuid = uuid;
	}

	/** full constructor */
	public Organize(String uuid, String orgNo, String orgName, String groupNo,String shop,
			 String pid) {
		this.uuid = uuid;
		this.orgNo = orgNo;
		this.orgName = orgName;
		this.shop = shop;
		this.groupNo = groupNo;
		this.pid = pid;
	}

	// Property accessors
	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}
	
	
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOrgNo() {
		return this.orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
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

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}