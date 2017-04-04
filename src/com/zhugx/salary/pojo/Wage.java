package com.zhugx.salary.pojo;


/**
 * Wage entity. @author MyEclipse Persistence Tools
 */
public class Wage implements java.io.Serializable {

	// Fields

	private String uuid;
	private String wageNo;
	private String wageName;
	private String symbol;
	private String isPiece;
	private String isTime;
	private String defaulteValue;

	// Constructors

	/** default constructor */
	public Wage() {
	}

	/** minimal constructor */
	public Wage(String uuid) {
		this.uuid = uuid;
	}

	/** full constructor */
	public Wage(String uuid, String wageNo, String wageName,
			String symbol, String isPiece, String isTime, String defaulteValue) {
		this.uuid = uuid;
		this.wageNo = wageNo;
		this.wageName = wageName;
		this.symbol = symbol;
		this.isPiece = isPiece;
		this.isTime = isTime;
		this.defaulteValue = defaulteValue;
	}

	// Property accessors
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getWageNo() {
		return this.wageNo;
	}

	public void setWageNo(String wageNo) {
		this.wageNo = wageNo;
	}

	public String getWageName() {
		return this.wageName;
	}

	public void setWageName(String wageName) {
		this.wageName = wageName;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getIsPiece() {
		return this.isPiece;
	}

	public void setIsPiece(String isPiece) {
		this.isPiece = isPiece;
	}

	public String getIsTime() {
		return this.isTime;
	}

	public void setIsTime(String isTime) {
		this.isTime = isTime;
	}

	public String getDefaulteValue() {
		return this.defaulteValue;
	}

	public void setDefaulteValue(String defaulteValue) {
		this.defaulteValue = defaulteValue;
	}

}