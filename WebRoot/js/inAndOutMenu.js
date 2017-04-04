var inAndOutMenu = [ {
	name : "厂批销售",
	open : true,
	children : [ {
		name : "厂批销售开票",
		menuUrl : "inOut/sellInvoice.jsp"
	},{
		name : "退货开票",
		menuUrl : "inOut/returnInvoice.jsp"
	},{
		name : "调价开票",
		menuUrl : "inOut/adjustInvoice.jsp"
	},{
		name : "撤销票查询",
		menuUrl : "inOut/cancelInvoice.jsp"
	}]
}, {
	name : "仓库",
	open : true,
	children : [ {
		name : "仓库调拨",
		menuUrl : "inOut/storeTrans.jsp"
	},{
		name : "仓库色码库存调整",
		menuUrl : "inOut/storeColorAdjust.jsp"
	},{
		name : "仓库盘点",
		menuUrl : "inOut/storeCheck.jsp"
	}
	]
}, {
	name : "费用",
	open : true,
	children : [ {
		name : "收付款开票",
		menuUrl : "inOut/moneypass.jsp"
	}, {
		name : "费用及账务调整",
		menuUrl : "inOut/feeAdjust.jsp"
	} ]
}, {
	name : "商场",
	open : true,
	children : [ {
		name : "商场送货开票",
		menuUrl : "inOut/shopsend.jsp"
	}, {
		name : "商场销售",
		menuUrl : "inOut/shopsell.jsp"
	} ]
}
/**
, {
	name : "仓库调拨",
	menuUrl : "inAndOut/goodClassSet.jsp"
}**/
, {
	name : "色码库存",
	open : true,
	children : [ {
		name : "产品色码库存",
		menuUrl : "inOut/goodStock.jsp"
	} ]
} ];