function forbidKey(e){
	if(!window.event){
		event = e;
	}
	var elem = event.target;
	 if(event.keyCode == 8){
	 	//退格
		var forbidFlag = false;
	 	var nodeName = elem.nodeName.toUpperCase();
	 	if(nodeName!='INPUT' && nodeName!='TEXTAREA'){  
	 		forbidFlag = true;
        }else{
        	var nodeType = elem.type.toUpperCase();  
    	 	var isReadOnly = elem.readOnly==true || elem.disabled == true;
    	 	
    	 	if(nodeName == 'INPUT' && (nodeType == 'TEXT' || nodeType == 'PASSWORD') || nodeName == 'TEXTAREA'){
    	 		//控件类型符合条件
    	 		if(isReadOnly){
    	 			//只读属性
    	 			forbidFlag = true;
    	 		}
    	 	}
        } 
	 	
	 	if(forbidFlag){
	 		if(event.returnValue){
	 			event.returnValue = false ;
            }
            if(event.preventDefault ){
            	event.preventDefault();
            }                
            return false;
	 	}
	 }
}