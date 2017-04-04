package com.zhugx.salary.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zhugx.salary.pojo.ProductData;

public class ShengChanInterface {
	
	/**
	 * 获取本月生产数据(车工)
	 * @param shop 211|1  orgId|wsNo
	 * @param month
	 * @return
	 */
	public static  List<ProductData> getCgData(List<CheGong> listCg,Map<String, String> orgIds,String shop, String month){
		//参数：repMonth(201603),bz(01) | String[] p month.split("-") p[0]+p[1]2 shop
		//根据shop 获取车工组的uuid 
		
		String[] mons = month.split("-");
		//List<CheGong> listCg = initCgData(shop, mons[0]+mons[1]); //通过接口获取
		
		List<ProductData> list = new ArrayList<ProductData>();
		//车工数据
		for(CheGong cg: listCg){
			ProductData p =  new ProductData();
			p.setUuid(Math.random()*1000+"");
			p.setItemId(cg.getGoodNo());
			p.setItemNo(cg.getGoodNo());
			p.setItemName(cg.getGoodName());
			p.setOrgId(orgIds.get(cg.getGroupNo()));
			p.setOrgNo(cg.getGroupNo());
			p.setOrgName(cg.getGroupName());
			p.setGroupNo(cg.getGroupNo().split(" ")[0]);
			p.setShop(shop);
			p.setMonth(month);
			p.setOpDate(new Date());
			
			p.setFetchNums(cg.getThisMonRec());
			p.setCompleteNums(cg.getThisMonFinish());
			p.setCjsl(0);
			p.setSgsl(0);
			p.setTgsl(0);
			
			list.add(p);
		 }

		return list;
	}
	
	
	/**
	 * 获取本月生产数据(其他工段, 裁剪  手工 烫工)
	 * @param shop 211|1  orgId|wsNo
	 * @param month
	 * @return
	 */
	public static  List<ProductData> getQtData(List<Others> listQt, Map<String, String> orgIds,String shop, String month){
		//参数：repMonth(201603),bz(01) | String[] p month.split("-") p[0]+p[1]2 shop
		String[] mons = month.split("-");
		//List<Others> listQt = initQtData(shop, mons[0]+mons[1]); //通过接口获取
		
		List<ProductData> list = new ArrayList<ProductData>();
		//尾段
		for(Others cg: listQt){
			ProductData p =  new ProductData();
			p.setUuid(Math.random()*1000+"");
			p.setItemId(cg.getGoodNo());
			p.setItemNo(cg.getGoodNo());
			p.setItemName(cg.getGoodName());
			p.setShop(shop);
			p.setMonth(month);
			p.setOpDate(new Date());
			
			p.setFetchNums(0);
			p.setCompleteNums(0);
			p.setCjsl(cg.getThisMonDress()== null? 0:cg.getThisMonDress());
			p.setSgsl(cg.getThisMonTrail()== null? 0:cg.getThisMonTrail());
			p.setTgsl(cg.getThisMonTrail()== null? 0:cg.getThisMonTrail());
			list.add(p);
		}
		return list;
	}
	
	public static  List<CheGong> initCgData(String shop, String month) {
		List<CheGong> list = new ArrayList<CheGong>();
		for(int i=1; i<=6;i++){
			CheGong p1 = new CheGong();
			String itemNo = "";
			String itemName = "";
			if(i%3 == 1){
				itemNo = "15Y-1501";
				itemName = "时尚涟漪裙";
				p1.setThisMonRec(60);
				p1.setThisMonFinish(65);
			}
			if(i%3 == 2){
				itemNo = "15Y-1502";
				itemName = "毛妮外衣";
				p1.setThisMonRec(30);
				p1.setThisMonFinish(36);
			}
			if(i%3 == 0){
				itemNo = "15Y-1503";
				itemName = "格子外套";
				p1.setThisMonRec(60);
				p1.setThisMonFinish(98);
			}
			String orgName = "";
			String groupNo = "";
			
			if("1".equals(shop)){
				if(i>=1 && i <=3){
					groupNo = "01 1组";
					orgName = "1组";
				}
				if(i>=4 && i <=6){
					groupNo = "02 2组";
					orgName = "2组";
				}
				if(i>=7 && i <=9){
					groupNo = "03 3组";
					orgName = "3组";
				}
			}
			
			if("2".equals(shop)){
				if(i>=1 && i <=3){
					groupNo = "011组";
					orgName = "一组";
				}
				if(i>=4 && i <=6){
					groupNo = "02 2组";
					orgName = "2组";
				}
			}
			p1.setGoodNo(itemNo);
			p1.setGoodName(itemName);
			p1.setGroupNo(groupNo);
			p1.setGroupName(orgName);
			p1.setWsNo(shop);
			list.add(p1);
		}
		return list;
	}
	
	public static  List<Others> initQtData(String shop, String month) {
		List<Others> list = new ArrayList<Others>();
		for(int i=1; i<=4;i++){
			Others p1 = new Others();
			String itemNo = "";
			String itemName = "";
			if(i%4 == 1){
				itemNo = "15Y-1502";
				itemName = "毛妮外衣";
				p1.setThisMonDress(100);
				p1.setThisMonTrail(90);
				p1.setThisMonTrail(90);
			}
			if(i%4 == 2){
				itemNo = "15Y-1503";
				itemName = "格子外套";
				p1.setThisMonDress(80);
				p1.setThisMonTrail(70);
				p1.setThisMonTrail(70);
			}
			if(i%4 == 3){
				itemNo = "15Y-1505";
				itemName = "花花外套";
				p1.setThisMonDress(50);
				p1.setThisMonTrail(40);
				p1.setThisMonTrail(40);
			}
			if(i%4 == 0){
				itemNo = "15Y-1501";
				itemName = "时尚涟漪裙";
				p1.setThisMonDress(50);
				p1.setThisMonTrail(40);
				p1.setThisMonTrail(40);
			}
			p1.setGoodNo(itemNo);
			p1.setGoodName(itemName);
			p1.setWsNo("1");
			p1.setWsName("车间一");
			list.add(p1);
		}
		return list;
	}
}
