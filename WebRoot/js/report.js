var reportMenu = [ {
	name : "客户",
	open : true,
	children : [ {
		name : "往来客户日报表",
		menuUrl : "rep/clientDay.jsp"
	},{
		name : "往来客户月报表",
		menuUrl : "rep/clientMonth.jsp"
	},{
		name : "往来客户年报表",
		menuUrl : "rep/clientYear.jsp"
	},{
		name : "客户本月开票明细表",
		menuUrl : "rep/clientInvoiceDetail.jsp"
	},{
		name : "往来客户对账单",
		menuUrl : "rep/clientBalAcc.jsp"
	}]
},{
	name : "仓库",
	open : true,
	children : [ {
		name : "仓库库存明细",
		menuUrl : "rep/storeDetailRep.jsp"
	},{
		name : "仓库进销存月报表",
		menuUrl : "rep/storeInOutMonRep.jsp"
	},{
		name : "仓库进销存月报表(不分仓库)",
		menuUrl : "rep/storeInOutMonRep2.jsp"
	}]
},{
	name : "销售",
	open : true,
	children : [{
		name : "客户拿货情况一览表(1)",
		menuUrl : "rep/takeGood1.jsp"
	},{
		name : "客户拿货情况一览表(2)",
		menuUrl : "rep/takeGood2.jsp"
	},{
		name : "一款多客户发货物流表",
		menuUrl : "rep/oneGood2Nclient.jsp"
	},{
		name : "一款多客户发货色码规物流表",
		menuUrl : "rep/oneGood2Nclient2.jsp"
	},{
		name : "客户退货比例分析表",
		menuUrl : "rep/retRatio.jsp"
	},{
		name : "客户销售/回款排行",
		menuUrl : "rep/clientSellRecRank.jsp"
	},{
		name : "客户销售明细表",
		menuUrl : "rep/clientSellDetail.jsp"
	},{
		name : "批发销售日报",
		menuUrl : "rep/wholeSaleDay.jsp"
	}]
},{
	name : "生产",
	open : true,
	children : [ {
		name : "车工日报表",
		menuUrl : "rep/latheDay.jsp"
	},{
		name : "车工月报表",
		menuUrl : "rep/latheMonth.jsp"
	},{
		name : "车工段生产进度汇总表",
		menuUrl : "rep/latheSchMonth.jsp"
	},{
		name : "车工完工产量汇总表",
		menuUrl : "rep/latheFinishOutput.jsp"
	},{
		name : "生产进度日报表",
		menuUrl : "rep/prtSchDay.jsp"
	},{
		name : "生产进度月报表",
		menuUrl : "rep/prtSchMonth.jsp"
	}]
},{
	name : "综合",
	open : true,
	children : [ {
		name : "生产销售综合报表",
		menuUrl : "rep/prtSellGeneral.jsp"
	}]
}  ];