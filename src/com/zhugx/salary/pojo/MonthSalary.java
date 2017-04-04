package com.zhugx.salary.pojo;

import java.util.Date;

/**
 * MonthSalary entity. @author MyEclipse Persistence Tools
 */
public class MonthSalary implements java.io.Serializable {

	// Fields

	public String uuid;
	public String emId;
	public String emNo;
	public String emName;
	public String job;
	public String shop;
	public String orgId;
	public String orgNo;
	public String orgName;
	
	public String groupNo;
	public String month;
	public String isPiece;
	public Double baseSalary;
	public Double piecePay;
	public Double totalPay;
	public Double finalPay;
	public Double f01;
	public Double f02;
	public Double f03;
	
	public Double f04;
	public Double f05;
	public Double f06;
	public Double f07;
	public Double f08;
	public Double f09;
	public Double f10;
	public Double f11;
	public Double f12;
	public Double f13;
	
	public Double f14;
	public Double f15;
	public Integer workingAge;
	public Double workDay;
	public Double time;
	public Double grade;
	public Double sumGrade;
	public Double realPay;
	public Double piecePrice;
	public Date opTime;
	
	//extends 
	public String sign;  //签名, 用来表格占位的, 没有实际用处
	

	// Constructors

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	/** default constructor */
	public MonthSalary() {
	}

	/** minimal constructor */
	public MonthSalary(String uuid) {
		this.uuid = uuid;
	}

	/** full constructor */
	public MonthSalary(String uuid, String emId, String emNo, String emName,
			String orgId, String orgNo, String orgName, String groupNo,
			 String month,
			String isPiece, Double baseSalary, Double piecePay,
			Double totalPay, Double finalPay, Double f01, Double f02,
			Double f03, Double f04, Double f05, Double f06, Double f07,
			Double f08, Double f09, Double f10, Double f11, Double f12,
			Double f13, Double f14, Double f15, Integer workingAge,
			Double workDay, Double time, Double grade, Double sumGrade,
			Double realPay, Double piecePrice, Date opTime) {
		this.uuid = uuid;
		this.emId = emId;
		this.emNo = emNo;
		this.emName = emName;
		this.orgId = orgId;
		this.orgNo = orgNo;
		this.orgName = orgName;
		this.groupNo = groupNo;
		this.month = month;
		this.isPiece = isPiece;
		this.baseSalary = baseSalary;
		this.piecePay = piecePay;
		this.totalPay = totalPay;
		this.finalPay = finalPay;
		this.f01 = f01;
		this.f02 = f02;
		this.f03 = f03;
		this.f04 = f04;
		this.f05 = f05;
		this.f06 = f06;
		this.f07 = f07;
		this.f08 = f08;
		this.f09 = f09;
		this.f10 = f10;
		this.f11 = f11;
		this.f12 = f12;
		this.f13 = f13;
		this.f14 = f14;
		this.f15 = f15;
		this.workingAge = workingAge;
		this.workDay = workDay;
		this.time = time;
		this.grade = grade;
		this.sumGrade = sumGrade;
		this.realPay = realPay;
		this.piecePrice = piecePrice;
		this.opTime = opTime;
	}

	public Double getWorkDay() {
		return workDay;
	}

	public void setWorkDay(Double workDay) {
		this.workDay = workDay;
	}

	// Property accessors
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getEmId() {
		return this.emId;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
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

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
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

	public Double getPiecePay() {
		return this.piecePay;
	}

	public void setPiecePay(Double piecePay) {
		this.piecePay = piecePay;
	}

	public Double getTotalPay() {
		return this.totalPay;
	}

	public void setTotalPay(Double totalPay) {
		this.totalPay = totalPay;
	}

	public Double getFinalPay() {
		return this.finalPay;
	}

	public void setFinalPay(Double finalPay) {
		this.finalPay = finalPay;
	}

	public Double getF01() {
		return this.f01;
	}

	public void setF01(Double f01) {
		this.f01 = f01;
	}

	public Double getF02() {
		return this.f02;
	}

	public void setF02(Double f02) {
		this.f02 = f02;
	}

	public Double getF03() {
		return this.f03;
	}

	public void setF03(Double f03) {
		this.f03 = f03;
	}

	public Double getF04() {
		return this.f04;
	}

	public void setF04(Double f04) {
		this.f04 = f04;
	}

	public Double getF05() {
		return this.f05;
	}

	public void setF05(Double f05) {
		this.f05 = f05;
	}

	public Double getF06() {
		return this.f06;
	}

	public void setF06(Double f06) {
		this.f06 = f06;
	}

	public Double getF07() {
		return this.f07;
	}

	public void setF07(Double f07) {
		this.f07 = f07;
	}

	public Double getF08() {
		return this.f08;
	}

	public void setF08(Double f08) {
		this.f08 = f08;
	}

	public Double getF09() {
		return this.f09;
	}

	public void setF09(Double f09) {
		this.f09 = f09;
	}

	public Double getF10() {
		return this.f10;
	}

	public void setF10(Double f10) {
		this.f10 = f10;
	}

	public Double getF11() {
		return this.f11;
	}

	public void setF11(Double f11) {
		this.f11 = f11;
	}

	public Double getF12() {
		return this.f12;
	}

	public void setF12(Double f12) {
		this.f12 = f12;
	}

	public Double getF13() {
		return this.f13;
	}

	public void setF13(Double f13) {
		this.f13 = f13;
	}

	public Double getF14() {
		return this.f14;
	}

	public void setF14(Double f14) {
		this.f14 = f14;
	}

	public Double getF15() {
		return this.f15;
	}

	public void setF15(Double f15) {
		this.f15 = f15;
	}

	public Integer getWorkingAge() {
		return this.workingAge;
	}

	public void setWorkingAge(Integer workingAge) {
		this.workingAge = workingAge;
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

	public Double getRealPay() {
		return this.realPay;
	}

	public void setRealPay(Double realPay) {
		this.realPay = realPay;
	}

	public Double getPiecePrice() {
		return this.piecePrice;
	}

	public void setPiecePrice(Double piecePrice) {
		this.piecePrice = piecePrice;
	}

	public Date getOpTime() {
		return this.opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

}