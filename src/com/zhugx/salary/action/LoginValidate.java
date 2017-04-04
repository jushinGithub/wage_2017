package com.zhugx.salary.action;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.zhugx.salary.pojo.Employee;

public class LoginValidate extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String namespace  = invocation.getProxy().getNamespace();  
        String actionName = invocation.getProxy().getActionName();  
          
        if(("/".equals(namespace) && "loginAction".equals(actionName))){  
            return invocation.invoke();  
        }else{  
            Map<String, Object> session = ActionContext.getContext().getSession();  
            Employee e = (Employee) session.get("cur_user");  
            if(e == null) {  
            	//HttpServletRequest  request  =  ServletActionContext.getRequest();
            	//HttpServletResponse response =  ServletActionContext.getResponse();
            	//request.getRequestDispatcher("index.jsp").forward(request,response);
            	
                return Action.LOGIN;  
            } else {  
                return invocation.invoke();  
            }  
	   }
	}

}
