package com.zhugx.salary.pojo;


/**
 * Configuration entity. @author MyEclipse Persistence Tools
 */
public class Configuration implements java.io.Serializable {

	// Fields

	private String uuid;
	private String cgpz;
	private String cjpz;
	private String sgpz;
	private String tgpz;
	private String cggj;
	private String cjgj;
	private String sggj;
	private String tggj;
	//小数点值影响到   , monthSalary  计件工资 piece_pay   应发工资total_pay   实发工资final_pay |  基本工资base_salary 实际工资real_pay 
	private Integer decimals;  
	private String jssd;
	private String ip;
	private String company;

	// Constructors

	/** default constructor */
	public Configuration() {
	}

	/** minimal constructor */
	public Configuration(String uuid) {
		this.uuid = uuid;
	}

	/** full constructor */
	public Configuration(String uuid, String cgpz, String cjpz, String sgpz,
			String tgpz, String cggj, String cjgj, String sggj, String tggj,
			Integer decimals, String jssd, String ip) {
		this.uuid = uuid;
		this.cgpz = cgpz;
		this.cjpz = cjpz;
		this.sgpz = sgpz;
		this.tgpz = tgpz;
		this.cggj = cggj;
		this.cjgj = cjgj;
		this.sggj = sggj;
		this.tggj = tggj;
		this.decimals = decimals;
		this.jssd = jssd;
		this.ip = ip;
	}

	// Property accessors
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCgpz() {
		return this.cgpz;
	}

	public void setCgpz(String cgpz) {
		this.cgpz = cgpz;
	}

	public String getCjpz() {
		return this.cjpz;
	}

	public void setCjpz(String cjpz) {
		this.cjpz = cjpz;
	}

	public String getSgpz() {
		return this.sgpz;
	}

	public void setSgpz(String sgpz) {
		this.sgpz = sgpz;
	}

	public String getTgpz() {
		return this.tgpz;
	}

	public void setTgpz(String tgpz) {
		this.tgpz = tgpz;
	}

	public String getCggj() {
		return this.cggj;
	}

	public void setCggj(String cggj) {
		this.cggj = cggj;
	}

	public String getCjgj() {
		return this.cjgj;
	}

	public void setCjgj(String cjgj) {
		this.cjgj = cjgj;
	}

	public String getSggj() {
		return this.sggj;
	}

	public void setSggj(String sggj) {
		this.sggj = sggj;
	}

	public String getTggj() {
		return this.tggj;
	}

	public void setTggj(String tggj) {
		this.tggj = tggj;
	}

	public Integer getDecimals() {
		return this.decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	public String getJssd() {
		return this.jssd;
	}

	public void setJssd(String jssd) {
		this.jssd = jssd;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}