package com.zhugx.salary.dao.common;

public class SqlConstant {
	
	/** Configuration */
	//新增
	public static String ADD_CONFIG = "INSERT INTO CONFIGURATION(UUID,CGPZ,CJPZ,SGPZ,TGPZ,CGGJ,CJGJ,SGGJ,TGGJ,DECIMALS,JSSD,IP)"+
	                                  "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	//更新
	public static String UPDATE_CONFIG = "UPDATE CONFIGURATION T SET T.CGPZ=?,T.CJPZ=?,T.SGPZ=?,T.TGPZ=?,T.CGGJ=?,T.CJGJ=?, "+
	                                     " T.SGGJ=?,T.TGGJ=?,T.DECIMALS=?,T.JSSD=?,T.IP=?,T.company=? WHERE UUID=?";
	//查询
	public static String GET_CONFIG = "SELECT * FROM CONFIGURATION";
	
	/** Employee */
	//新增
	public static String ADD_EM = "INSERT INTO EMPLOYEE(UUID,ORG_ID,EM_NO,EM_NAME,IS_PIECE,BASE_SALARY,EM_STATE,JOB,grade)"+
							       " VALUES(?,?,?,?,?,?,?,?,?)";
	//更新
	public static String UPDATE_EM = "UPDATE EMPLOYEE T SET T.ORG_ID=?,T.EM_NO=?,T.EM_NAME=?,T.IS_PIECE=?,T.BASE_SALARY=?,T.EM_STATE=?, "+
	                                 "  t.job=?, t.grade=? WHERE UUID=?";
	//查询
	public static String GET_EM = "SELECT * FROM EMPLOYEE";
	
	/**Organize*/
	//新增
	public static String ADD_ORG = "INSERT INTO ORGANIZE_base(UUID,ORG_NO,ORG_NAME,GROUP_NO,shop, PID) VALUES(?,?,?,?,?,?)";
	
	/**Organize*/
	//新增
	public static String ADD_WAGE = "INSERT INTO WAGE(UUID,WAGE_NO,WAGE_NAME,SYMBOL,IS_PIECE,IS_TIME,DEFAULTE_VALUE)"+
	                                " VALUES(?,?,?,?,?,?,?)";
	//更新
	public static String UPDATE_WAGE = "UPDATE WAGE T SET T.WAGE_NO=?,T.WAGE_NAME=?,T.SYMBOL=?,T.IS_PIECE=?,T.IS_TIME=?,T.DEFAULTE_VALUE=? "+
	                                   " WHERE UUID=?";
	//查询
	public static String GET_WAGE = "SELECT * FROM WAGE";
	
	/** orgSalary */
	//新增
	 public static String ADD_ORGSALARY = "INSERT INTO ORG_SALARY(UUID,SHOP,ORG_ID,ORG_NO,ORG_NAME,GROUP_NO,EM_SUM,PIECE_SUM,TIME_SUM,"+
			 "PIECE_PRICE,PIECE_FINAL,KXK,KZL,PYF,ZJE_ONE,ZJE_TWO,MONTH, OP_DATE) VALUES(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?)";
	//更新
	public static String UPDATE_ORGSALARY = "update ORG_SALARY t set t.SHOP=?,t.ORG_ID=?,t.ORG_NO=?,t.ORG_NAME=?,t.GROUP_NO=?,t.EM_SUM=?,t.PIECE_SUM=?,t.TIME_SUM=?,t.PIECE_PRICE=?,"+
	           " t.PIECE_FINAL=?,t.KXK=?,t.KZL=?,t.PYF=?,t.ZJE_ONE=?,t.ZJE_TWO=?,t.MONTH=?,t.OP_DATE=?  where t.uuid=?";
	//查询
	public static String GET_ORGSALARY = "SELECT * FROM ORG_SALARY";
	
	/** month_salary */
	//新增
    public static String ADD_MONTHSALARY = "INSERT INTO MONTH_SALARY(UUID,EM_ID,EM_NO,EM_NAME,SHOP,ORG_ID,ORG_NO,ORG_NAME,GROUP_NO,MONTH,IS_PIECE, "+
	 "BASE_SALARY,PIECE_PAY,TOTAL_PAY,FINAL_PAY,F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13,F14,F15,"+
    		" WORKING_AGE,WORK_DAY,TIME,GRADE,SUM_GRADE,REAL_PAY,PIECE_PRICE,OP_TIME,job) "+
	"VALUES(?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?,  ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?)";
    
    //更新
  	public static String UPDATE_MONTHSALARY = "UPDATE MONTH_SALARY T SET T.EM_ID=?, T.EM_NO=?, T.EM_NAME=?, t.SHOP=?,T.ORG_ID=?, T.ORG_NO=?, T.ORG_NAME=?, T.GROUP_NO=?,"+
      "  T.MONTH=?, T.IS_PIECE=?, T.BASE_SALARY=?, T.PIECE_PAY=?, T.TOTAL_PAY=?, T.FINAL_PAY=?, T.F01=?, T.F02=?, T.F03=?, T.F04=?,"+
      "  T.F05=?, T.F06=?, T.F07=?, T.F08=?, T.F09=?, T.F10=?, T.F11=?, T.F12=?, T.F13=?, T.F14=?,"+
      "  T.F15=?, T.WORKING_AGE=?, T.WORK_DAY=?, T.TIME=?, T.GRADE=?, T.SUM_GRADE=?, T.REAL_PAY=?, T.PIECE_PRICE=?, T.OP_TIME=?, t.job=? WHERE T.UUID=?";
  	
  	
  	/** product_data */
    //新增
  	public static String ADD_PRODUCTDATA = "INSERT INTO PRODUCT_DATA(UUID,ITEM_ID,ITEM_NO,ITEM_NAME,FETCH_NUMS,COMPLETE_NUMS,SHOP,ORG_ID,ORG_NAME,GROUP_NO,MONTH,OP_DATE) "+
                                           "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
   //更新
  	public static String UPDATE_PRODUCTDATA = "UPDATE PRODUCT_DATA T SET T.ITEM_ID=?,T.ITEM_NO=?,T.ITEM_NAME=?,T.FETCH_NUMS=?,T.COMPLETE_NUMS=?,t.SHOP=?,T.ORG_ID=?,T.ORG_NAME=?,"+
  											  " T.GROUP_NO=?,T.MONTH=?,T.OP_DATE=? WHERE T.UUID=?";
  	
  	/** product_time */
  	//新增
  	public static String ADD_TIME = "INSERT INTO PRODUCT_TIME(UUID,EM_ID,EM_NO,EM_NAME,GROUP_NO,TIME,GRADE,SUM_GRADE,SHOP,ORG_ID,ORG_NAME,MONTH,OP_DATE) "+
  	                                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
  	
    //更新
  	public static String UPDATE_TIME = "UPDATE PRODUCT_TIME T SET T.EM_ID=?,T.EM_NO=?,T.EM_NAME=?,T.GROUP_NO=?, T.TIME=?,T.GRADE=?,T.SUM_GRADE=?,T.SHOP=?,T.ORG_ID=?, "+
                                       " T.ORG_NAME=?,T.MONTH=?,T.OP_DATE=?  WHERE T.UUID=?";
  	/** product_price */
  	//新增
  	public static String ADD_PRICE = "INSERT INTO PRODUCT_PRICE(UUID,SHOP,ITEM_ID,ITEM_NO,ITEM_NAME,GJ,GYJ,CJGJ,CJSL,CJJE,CGGJ, "+
  									 " CGSL,CGJE,SGGJ,SGSL,SGJE,TGGJ,TGSL,TGJE,MONTH,OP_DATE )"+
  	                                 "VALUES(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?)";
    //更新
  	public static String UPDATE_PRICE = "UPDATE PRODUCT_PRICE T SET t.SHOP=?, T.ITEM_ID=?,T.ITEM_NO=?,T.ITEM_NAME=?,T.GJ=?,T.GYJ=?,T.CJGJ=?,T.CJSL=?,T.CJJE=?,T.CGGJ=?,"+
  	                                    " T.CGSL=?,T.CGJE=?,T.SGGJ=?,T.SGSL=?,T.SGJE=?,T.TGGJ=?,T.TGSL=?,T.TGJE=?,T.MONTH=?,T.OP_DATE=? WHERE T.UUID=?";
	
	 
	
	
	
}
