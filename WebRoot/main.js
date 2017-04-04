$(function () {
    //动态菜单数据
    var treeData = [{
            text : "工资管理",
            children : [{
                    text : "工资计算",
                    state : "closed",
                    children : [{text : "车工完工数", attributes : {url : "page/salary/product_data_comp.jsp"}}, 
                                {text : "车工领用数", attributes : {url : "page/salary/product_data_fetch.jsp"}},
                                {text : "生产数量调整",attributes : {url : "page/salary/product_amount.jsp"}}, 
                                {text : "输入工价",  attributes : {url : "page/salary/product_price.jsp"}},
                            	{text : "工时分设置", attributes : {url : "page/salary/product_time.jsp"}}, 
                            	{text : "各项扣款",  attributes : {url : "page/salary/salary_org.jsp"}},
                                {text : "班组计件工资",attributes : {url : "page/salary/salary_piece.jsp"}}, 
                                {text : "公司计时工资",attributes : {url : "page/salary/salary_time.jsp"}}
                               ]
                	},{
                    text : "报表",
                    state : "closed",
                    children : [{text : "工价表",       attributes : {url : "page/salary/report/report_two.jsp"}}, 
                                {text : "计件工资明细",   attributes : {url : "page/salary/report/report_three_one.jsp"}}, 
                                {text : "工段工资测算",   attributes : {url : "page/salary/report/report_three_two.jsp"}},
                                {text : "计件工资测算表",  attributes : {url : "page/salary/report/report_five.jsp"}}, 
                                {text : "计件工资明细表",  attributes : {url : "page/salary/report/report_six.jsp"}}, 
                                {text : "计件人员工资汇总表",attributes : {url : "page/salary/report/org_salary_piece.jsp"}}, 
                                {text : "计时人员工资汇总表",attributes : {url : "page/salary/report/org_salary_time.jsp"}}, 
                                {text : "月工资分析表",   attributes : {url : "page/salary/report/report_monthAnalyze.jsp"}}
                               ]
                	},{
                    text : "基础设置",
                    state : "closed",
                    children : [{text : "员工管理",  attributes : {url : "page/salary/organize.jsp"}}, 
                                {text : "考勤表",    attributes : {url : "page/salary/attendance.jsp"}},
                                {text : "工资项目设置",attributes : {url : "page/salary/wage.jsp"}}, 
                                {text : "系统设置",  attributes : {url : "page/salary/config.jsp"}}
                               ]
                	}
            ]
        }
    ];
    
    //实例化树形菜单
    $("#tree").tree({
        data : treeData,
        lines : true,
        onClick : function (node) {
            if (node.attributes) {
                Open(node.text, node.attributes.url);
            }
        }
    });
    
    //默认展开 
    $("#tree").tree('expandAll'); 
    //默认打开车工生产数据
    Open("车工完工数", "page/salary/product_data_comp.jsp");
    
    //在右边center区域打开菜单，新增tab
    function Open(text, url) {
        if ($("#tabs").tabs('exists', text)) {
            $('#tabs').tabs('select', text);
        } else {
        	 var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'; 
            $('#tabs').tabs('add', {
                title : text,
                closable : true,
                content : content
            });
        }
    }
    
    //绑定tabs的右键菜单
    $("#tabs").tabs({
        onContextMenu : function (e, title) {
            e.preventDefault();
            $('#tabsMenu').menu('show', {
                left : e.pageX,
                top : e.pageY
            }).data("tabTitle", title);
        }
    });
    
    //实例化menu的onClick事件
    $("#tabsMenu").menu({
        onClick : function (item) {
            CloseTab(this, item.name);
        }
    });
    
    //几个关闭事件的实现
    function CloseTab(menu, type) {
        var curTabTitle = $(menu).data("tabTitle");
        var tabs = $("#tabs");
        
        if (type === "close") {
            tabs.tabs("close", curTabTitle);
            return;
        }
        
        var allTabs = tabs.tabs("tabs");
        var closeTabsTitle = [];
        
        $.each(allTabs, function () {
            var opt = $(this).panel("options");
            if (opt.closable && opt.title != curTabTitle && type === "Other") {
                closeTabsTitle.push(opt.title);
            } else if (opt.closable && type === "All") {
                closeTabsTitle.push(opt.title);
            }
        });
        
        for (var i = 0; i < closeTabsTitle.length; i++) {
            tabs.tabs("close", closeTabsTitle[i]);
        }
    }
});