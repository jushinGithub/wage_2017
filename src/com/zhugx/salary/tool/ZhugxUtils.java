package com.zhugx.salary.tool;

import java.math.BigDecimal;

public class ZhugxUtils {

	/**
	 * 小数相加
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double addDouble(Double d1, Double d2){
		if(d1 == null){
			d1 = 0.0;
		}
		if(d2 == null){
			d2 = 0.0;
		}
		return new BigDecimal(d1+"").add(new BigDecimal(d2+"")).doubleValue();
	}
	
	/**
	 * 
	 * @param d1
	 * @param d2
	 * @param decimals  保留的小数位数
	 * @return
	 */
	public static double addDouble(Double d1, Double d2, int decimals, int roundHalf){
		if(d1 == null){
			d1 = 0.0;
		}
		if(d2 == null){
			d2 = 0.0;
		}
		return new BigDecimal(d1+"").add(new BigDecimal(d2+"")).setScale(decimals, roundHalf).doubleValue();
	}
	
	/**
	 * 小数相减
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double subDouble(Double d1, Double d2){
		if(d1 == null){
			d1 = 0.0;
		}
		if(d2 == null){
			d2 = 0.0;
		}
		return new BigDecimal(d1+"").subtract(new BigDecimal(d2+"")).doubleValue();
	}
	
	/**
	 * 
	 * @param d1
	 * @param d2
	 * @param decimals  保留的小数位数
	 * @return
	 */
	public static double subDouble(Double d1, Double d2, int decimals, int roundHalf){
		if(d1 == null){
			d1 = 0.0;
		}
		if(d2 == null){
			d2 = 0.0;
		}
		return new BigDecimal(d1+"").subtract(new BigDecimal(d2+"")).setScale(decimals, roundHalf).doubleValue();
	}
	
	/**
	 * 小数相除
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double divDouble(Double d1, Double d2){
		double result = 0.0;
		if(d1 == null){
			d1 = 0.0;
		}
		if(d2 == null){
			d2 = 0.0;
		}
		BigDecimal d1Big = new BigDecimal(d1+"");
		BigDecimal d2Big = new BigDecimal(d2+"");
		
		if(BigDecimal.ZERO.compareTo(d1Big) != 0 && BigDecimal.ZERO.compareTo(d2Big) != 0){
			result = d1Big.divide(d2Big).doubleValue();
		}
		return result;
	}
	
	public static double divDouble(Double d1, Double d2, int decimals, int roundHalf){
		double result = 0.0;
		if(d1 == null){
			d1 = 0.0;
		}
		if(d2 == null){
			d2 = 0.0;
		}
		BigDecimal d1Big = new BigDecimal(d1+"");
		BigDecimal d2Big = new BigDecimal(d2+"");
		
		if(BigDecimal.ZERO.compareTo(d1Big) != 0 && BigDecimal.ZERO.compareTo(d2Big) != 0){
			result = d1Big.divide(d2Big,decimals,roundHalf).doubleValue();
		}
		return result;
	}
	
	
	/**
	 * 小数相除
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double mutilDouble(Double d1, Double d2){
		double result = 0.0;
		if(d1 == null){
			d1 = 0.0;
		}
		if(d2 == null){
			d2 = 0.0;
		}
		BigDecimal d1Big = new BigDecimal(d1+"");
		BigDecimal d2Big = new BigDecimal(d2+"");
		
		if(BigDecimal.ZERO.compareTo(d1Big) != 0 && BigDecimal.ZERO.compareTo(d2Big) != 0){
			result = d1Big.multiply(d2Big).doubleValue();
		}
		return Math.round(result*100)/100;
	}
	
	
	
	/**
	 * 首字母大写
	 * @param name
	 * @return
	 */
    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
        
    }
}
