package com.zhugx.salary.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 *用户登陆
 * @author jushin
 *
 */
public class LoginAction extends ActionSupport {
	private String user;
	private String password;
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String login() throws Exception {
		HttpServletRequest  request = ServletActionContext.getRequest();
		
		if(user.equalsIgnoreCase("manager") && password.equalsIgnoreCase("8888") ){
			request.getSession().setAttribute("cur_user", user); 
			message = SUCCESS;
		}else{
			message = Action.LOGIN;
		}
		
		return message;
	}
	
}
