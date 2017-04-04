	
  $(function(){
		/** 获取数据 */
		 $.ajax({
	         url : "getMyConfig.action",
	         data : null,
	         dataType : "json",
	         success : function (rsp) {
	        	 var config = {};
	        	 for(var x in rsp){
	        		 config["configuration."+x] = rsp[x];
	        	 }
	        	 $('#ff').form('load', config);
	         },
	         error : function () {
	             options.onLoadError.apply(target, arguments);
	         }
	     });
	}); 
	
	/** 保存 */
	function submitForm(){
        $('#ff').form('submit', {
            success: function(data){
            	$.ajax({
	       	         url : "getMyConfig.action",
	       	         data : null,
	       	         dataType : "json",
	       	         success : function (rsp) {
	       	        	 var config = {};
	       	        	 for(var x in rsp){
	       	        		 config["configuration."+x] = rsp[x];
	       	        	 }
	       	        	 $('#ff').form('load', config);
	       	         },
	       	         error : function () {
	       	             options.onLoadError.apply(target, arguments);
	       	         }
	       	     });
            	
            	$.messager.show({msg:"保存成功",title:'提示'});
            }
        });
    }
	
	/** 撤销 */
    function clearForm(){
        $('#ff').form('clear');
    }