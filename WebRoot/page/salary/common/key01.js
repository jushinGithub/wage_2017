//键盘控制
$.extend($.fn.datagrid.methods, {
    keyCtr : function (jq) {
        return jq.each(function () {
            var grid = $(this);
            grid.datagrid('getPanel').panel('panel').attr('tabindex', 1).bind('keydown', function (e) {
            	if(SampleInfo.editorIndex == -1){
            		return;
            	}
            	var keyCode = e.keyCode;
            	//当键盘按下键为左右键时,把网格键盘按下的事件默认动作去除,要不然会影响按左右键选择文本框值
            	if( keyCode == 37 || keyCode == 39){
            		e.preventDefault();
                	e.stopPropagation();
            	}
            	rows = $dg.datagrid('getRows');
            	if(SampleInfo.editorField == ''){
            		SampleInfo.editorField = 'formalStyleNo';
            	}
                switch (keyCode) {
                case 38: // up	
                	var editors = grid.datagrid('getEditors',SampleInfo.editorIndex);
                    if (editors) {
                    	if(rows.length>SampleInfo.editorIndex && SampleInfo.editorIndex>=1){
                    		SampleInfo.editorIndex--;
                    		
                    	}else{
                    		SampleInfo.editorIndex = rows.length-1;
                    	}
                    	grid.datagrid('beginEdit',SampleInfo.editorIndex);
                    	//单元格聚焦
                    	var ed = grid.datagrid('getEditor',{index:SampleInfo.editorIndex,
                    										field:SampleInfo.editorField});
                    	if(ed != null && ed != ""){
                       	 $(ed.target).focus();
                       	 $(ed.target).select(); 
                    	}
                    } else {
                    }
                    break;
                case 40: // down
                	var editors = grid.datagrid('getEditors',SampleInfo.editorIndex);
                    if (editors) {
                    	if(rows.length-1>SampleInfo.editorIndex){
                    		SampleInfo.editorIndex++;
                    		
                    	}else{
                    		SampleInfo.editorIndex = 0;
                    	}
                    	grid.datagrid('beginEdit',SampleInfo.editorIndex);
                    	//单元格聚焦
                    	var ed = grid.datagrid('getEditor',{index:SampleInfo.editorIndex,
                    										field:SampleInfo.editorField});
                    	if(ed != null && ed != ""){
                       	 $(ed.target).focus();
                       	 $(ed.target).select(); 
                    	}
                    }
                    break;
                case 37: // left
                	var editors = grid.datagrid('getEditors',SampleInfo.editorIndex);
                    if (editors) {
                    	if(rows.length-1>SampleInfo.editorIndex){
                    		SampleInfo.editorIndex++;
                    		
                    	}else{
                    		SampleInfo.editorIndex = 0;
                    	}
                    	grid.datagrid('beginEdit',SampleInfo.editorIndex);
                    	//单元格聚焦
                    	if(SampleInfo.editorField == 'formalStyleNo'){
                    		SampleInfo.editorField = 'formalCategoryNo';
                    	}else if(SampleInfo.editorField == 'formalColorNo'){
                    		SampleInfo.editorField = 'formalStyleNo';
                    	}else if(SampleInfo.editorField == 'formalCategoryNo'){
                    		SampleInfo.editorField = 'formalColorNo';
                    	}
                    	var ed = grid.datagrid('getEditor',{index:SampleInfo.editorIndex,
                    										field:SampleInfo.editorField});
                    	if(ed != null && ed != ""){
                       	 $(ed.target).focus();
                       	 $(ed.target).select(); 
                    	}
                    }
                    break;
                case 39: // right
                	var editors = grid.datagrid('getEditors',SampleInfo.editorIndex);
                    if (editors) {
                    	if(rows.length-1>SampleInfo.editorIndex){
                    		SampleInfo.editorIndex++;
                    		
                    	}else{
                    		SampleInfo.editorIndex = 0;
                    	}
                    	grid.datagrid('beginEdit',SampleInfo.editorIndex);
                    	//单元格聚焦
                    	if(SampleInfo.editorField == 'formalStyleNo'){
                    		SampleInfo.editorField = 'formalCategoryNo';
                    	}else if(SampleInfo.editorField == 'formalColorNo'){
                    		SampleInfo.editorField = 'formalStyleNo';
                    	}else if(SampleInfo.editorField == 'formalCategoryNo'){
                    		SampleInfo.editorField = 'formalColorNo';
                    	}
                    	var ed = grid.datagrid('getEditor',{index:SampleInfo.editorIndex,
                    										field:SampleInfo.editorField});
                    	if(ed != null && ed != ""){
                       	 $(ed.target).focus();
                       	 $(ed.target).select(); 
                    	}
                    }
                    break;
                case 13: // enter键
                	removeDataGridCommon(itemStyle.dataGridItemStyle);
        			itemStyle.dealItemStyleProc(); 
                    break;
                case 113: //F2保存键
                	parent.operaItemStyleInsert();
                	var c = parent.billOrder.ItemStylePanel ;
                	c.panel('close',true);

                }
            });
           
        });
    }
});