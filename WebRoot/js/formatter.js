
	function fommater(){
		//ÑÕÉ«ÁÐ±í
		this.scDataList = getAllData("syys.action");
		this.ysid2ysInfo = {};
		this.sm2idMap = {};
		this.id2smMap = {};
		var me = this;
		$.each(me.scDataList,function(i,v){
			me.ysid2ysInfo[v.colorId] = v;
			me.sm2idMap[v.colorNo] = v.colorId;
			me.id2smMap[v.colorId] = v.colorNo;
		});
	}
	
	fommater.prototype = {
		formatSm : function(cellvalue, options, cell){
			debugger;
			return cellvalue? sm2idMap[cellvalue]:"";
		},
		umFormatSm : function(cellvalue, options, cell){
			return cellvalue ? id2smMap[cellvalue] : "";
		}
	};