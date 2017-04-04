package com.zhugx.salary.pojo;

import java.util.Date;

/**
 * OrgSalary entity. @author MyEclipse Persistence Tools
 */
public class OrgSalary implements java.io.Serializable {

	// Fields

	private String uuid;
	private String shop;
	private String orgId;
	private String orgNo;
	private String orgName;
	private String groupNo;
	private Integer emSum;
	private Double pieceSum;
	private Double timeSum;
	private Double piecePrice;
	private Double pieceFinal;
	
	private Double kxk;
	private Double kzl;
	private Double pyf;
	private Double zjeOne;
	private Double zjeTwo;
	private String month;
	private Date opDate;

	// Constructors

	/** default constructor */
	public OrgSalary() {
	}

	/** minimal constructor */
	public OrgSalary(String uuid) {
		this.uuid = uuid;
	}

	/** full constructor */
	public OrgSalary(String uuid, String orgId, String orgNo, String orgName,
			String groupNo, Integer emSum, Double pieceSum, Double timeSum,
			Double piecePrice, Double pieceFinal, Double kxk, Double kzl,
			Double pyf, Double zjeOne, Double zjeTwo, String month, Date opDate) {
		this.uuid = uuid;
		this.orgId = orgId;
		this.orgNo = orgNo;
		this.orgName = orgName;
		this.groupNo = groupNo;
		this.emSum = emSum;
		this.pieceSum = pieceSum;
		this.timeSum = timeSum;
		this.piecePrice = piecePrice;
		this.pieceFinal = pieceFinal;
		this.kxk = kxk;
		this.kzl = kzl;
		this.pyf = pyf;
		this.zjeOne = zjeOne;
		this.zjeTwo = zjeTwo;
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

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public Integer getEmSum() {
		return this.emSum;
	}

	public void setEmSum(Integer emSum) {
		this.emSum = emSum;
	}

	public Double getPieceSum() {
		return this.pieceSum;
	}

	public void setPieceSum(Double pieceSum) {
		this.pieceSum = pieceSum;
	}

	public Double getTimeSum() {
		return this.timeSum;
	}

	public void setTimeSum(Double timeSum) {
		this.timeSum = timeSum;
	}

	public Double getPiecePrice() {
		return this.piecePrice;
	}

	public void setPiecePrice(Double piecePrice) {
		this.piecePrice = piecePrice;
	}

	public Double getPieceFinal() {
		return this.pieceFinal;
	}

	public void setPieceFinal(Double pieceFinal) {
		this.pieceFinal = pieceFinal;
	}

	public Double getKxk() {
		return this.kxk;
	}

	public void setKxk(Double kxk) {
		this.kxk = kxk;
	}

	public Double getKzl() {
		return this.kzl;
	}

	public void setKzl(Double kzl) {
		this.kzl = kzl;
	}

	public Double getPyf() {
		return this.pyf;
	}

	public void setPyf(Double pyf) {
		this.pyf = pyf;
	}

	public Double getZjeOne() {
		return this.zjeOne;
	}

	public void setZjeOne(Double zjeOne) {
		this.zjeOne = zjeOne;
	}

	public Double getZjeTwo() {
		return this.zjeTwo;
	}

	public void setZjeTwo(Double zjeTwo) {
		this.zjeTwo = zjeTwo;
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