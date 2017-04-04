var sysSetMenu = [ {
	name : "基本",
	open : true,
	children : [ {
		name : "颜色设置",
		menuUrl : "sysSet/colorSet.jsp"
	}, {
		name : "尺码设置",
		menuUrl : "sysSet/sizeSet.jsp"
	}, {
		name : "仓库设置",
		menuUrl : "sysSet/storeSet.jsp"
	} ]
}, {
	name : "货品",
	children : [ {
		name : "货品种类管理",
		menuUrl : "sysSet/goodClassSet.jsp"
	}, {
		name : "货品设置",
		menuUrl : "sysSet/goodSet.jsp"
	}, {
		name : "货品条码管理",
		menuUrl : "sysSet/barCodeSet.jsp"
	} ]
}, {
	name : "价格",
	children : [
//	{
//		name : "价格种类管理",
//		menuUrl : "sysSet/priceTypeSet.jsp"
//	}, 
	{
		name : "价格设置",
		menuUrl : "sysSet/priceSet.jsp"
	}
//	, {
//		name : "价格特别折扣设置",
//		menuUrl : "sysSet/goodRateSet.jsp"
//	} 
	]
}, {
	name : "客户",
	children : [ {
		name : "区域设置 ",
		menuUrl : "sysSet/areaSet.jsp"
	}, {
		name : "客户设置",
		menuUrl : "sysSet/clientSet.jsp"
	}, {
		name : "客户金额限度设置",
		menuUrl : "sysSet/controlSet.jsp"
	} ]
}, {
	name : "商场",
	children : [ {
		name : "商场分类一设置 ",
		menuUrl : "sysSet/shopAreaSet.jsp"
	}, {
		name : "商场分类二设置",
		menuUrl : "sysSet/shopSellerSet.jsp"
	}, {
		name : "商场资料设置",
		menuUrl : "sysSet/shopSet.jsp"
	} ]
}, {
	name : "车间",
	children : [ {
		name : "车间设置 ",
		menuUrl : "sysSet/workShopSet.jsp"
	} ]
}, {
	name : "班组",
	children : [ {
		name : "车工段班组设置 ",
		menuUrl : "sysSet/groupSet.jsp"
	} ]
}, {
	name : "系统初始化",
	children : [{
		name : "初始化数据日期 ",
		menuUrl : "sysSet/initDataTime.jsp"
	},{
		name : "客户上存数据",
		menuUrl : "sysSet/clientBalSet.jsp"
	}, {
		name : "仓库上存数据",
		menuUrl : "sysSet/storeBalSet.jsp"
	}, {
		name : "商场上存数据",
		menuUrl : "sysSet/shopBalSet.jsp"
	}, {
		name : "生产上存数据",
		children : [ {
			name : "裁剪段上存数据 ",
			menuUrl : "sysSet/tailorTotalSet.jsp"
		}, {
			name : "车工段上存数据",
			menuUrl : "sysSet/latheTotalSet.jsp"
		}, {
			name : "尾段上存数据",
			menuUrl : "sysSet/trailTotalSet.jsp"
		} ]
	}, {
		name : "清空数据"
	} ]
}, 
/**
{
	name : "结转日期设置",
	children : [ {
		name : "生产库存结转日期设置"
	}, {
		name : "商场每月结转日期设置",
		menuUrl : "sysSet/shopCarryOverSet.jsp"
	} ]
}, **/
{
	name : "系统功能设置",
	children : [ {
		name : "系统功能设置",
		menuUrl : "sysSet/sysFuncSet.jsp"
	} ]
}

];