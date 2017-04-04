package com.zhugx.salary.pojo;

import java.util.Date;

/**
 * ProductPrice entity. @author MyEclipse Persistence Tools
 */
public class ProductPrice implements java.io.Serializable {

	// Fields

	private String uuid;
	private String shop;
	private String itemId;
	private String itemNo;
	private String itemName;
	private Double gj;
	private Double gyj;
	
	private Double cjgj;
	private Integer cjsl;  //裁剪数
	private Double cjje;
	
	private Double cggj;
	private Integer cgsl;  //车工数量
	private Double cgje;   
	
	private Double sggj;
	private Integer sgsl;   //手工数
	private Double sgje;
	
	private Double tggj;
	private Integer tgsl;   //烫工数
	private Double tgje;
	
	private String month;  //月份
	private Date opDate;

	//extends 
	private String orgId;
	private Double piecePay;
	private Double sumGj;
	
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Double getPiecePay() {
		return piecePay;
	}
	public void setPiecePay(Double piecePay) {
		this.piecePay = piecePay;
	}
	public Double getSumGj() {
		return sumGj;
	}
	public void setSumGj(Double sumGj) {
		this.sumGj = sumGj;
	}
	
	// Constructors
	/** default constructor */
	public ProductPrice() {
	}

	/** minimal constructor */
	public ProductPrice(String uuid) {
		this.uuid = uuid;
	}

	/** full constructor */
	public ProductPrice(String uuid, String itemId, String itemNo,
			String itemName,  Double gj, Double gyj,
			Double cjgj, Integer cjsl, Double cjje, Double cggj, Integer cgsl,
			Double cgje, Double sggj, Integer sgsl, Double sgje, 
			Double tggj, Integer tgsl, Double tgje,
			String month, Date opDate) {
		this.uuid = uuid;
		this.itemId = itemId;
		this.itemNo = itemNo;
		this.itemName = itemName;
		this.gj = gj;
		this.gyj = gyj;
		this.cjgj = cjgj;
		this.cjsl = cjsl;
		this.cjje = cjje;
		this.cggj = cggj;
		this.cgsl = cgsl;
		this.cgje = cgje;
		this.sggj = sggj;
		this.sgsl = sgsl;
		this.sgje = sgje;
		this.tggj = tggj;
		this.tgsl = tgsl;
		this.tgje = tgje;
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


	public Double getGj() {
		return this.gj;
	}

	public void setGj(Double gj) {
		this.gj = gj;
	}

	public Double getGyj() {
		return this.gyj;
	}

	public void setGyj(Double gyj) {
		this.gyj = gyj;
	}

	public Double getCjgj() {
		return this.cjgj;
	}

	public void setCjgj(Double cjgj) {
		this.cjgj = cjgj;
	}

	public Integer getCjsl() {
		return this.cjsl;
	}

	public void setCjsl(Integer cjsl) {
		this.cjsl = cjsl;
	}

	public Double getCjje() {
		return this.cjje;
	}

	public void setCjje(Double cjje) {
		this.cjje = cjje;
	}

	public Double getCggj() {
		return this.cggj;
	}

	public void setCggj(Double cggj) {
		this.cggj = cggj;
	}

	public Integer getCgsl() {
		return this.cgsl;
	}

	public void setCgsl(Integer cgsl) {
		this.cgsl = cgsl;
	}

	public Double getCgje() {
		return this.cgje;
	}

	public void setCgje(Double cgje) {
		this.cgje = cgje;
	}

	public Double getSggj() {
		return this.sggj;
	}

	public void setSggj(Double sggj) {
		this.sggj = sggj;
	}

	public Integer getSgsl() {
		return this.sgsl;
	}

	public void setSgsl(Integer sgsl) {
		this.sgsl = sgsl;
	}

	public Double getSgje() {
		return this.sgje;
	}

	public void setSgje(Double sgje) {
		this.sgje = sgje;
	}
	
	public Double getTggj() {
		return this.tggj;
	}

	public void setTggj(Double tggj) {
		this.tggj = tggj;
	}

	public Integer getTgsl() {
		return this.tgsl;
	}

	public void setTgsl(Integer tgsl) {
		this.tgsl = tgsl;
	}

	public Double getTgje() {
		return this.tgje;
	}

	public void setTgje(Double tgje) {
		this.tgje = tgje;
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