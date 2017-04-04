package com.zhugx.salary.tool;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zhugx.salary.pojo.Employee;

public class LoginFilter implements javax.servlet.Filter{

	

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		// 获得在下面代码中要用的request,response,session对象
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpSession session = servletRequest.getSession();
 
        // 获得用户请求的URI
         String path = servletRequest.getRequestURI();
         
        
         if(path.indexOf("main.jsp") > -1 ||
            path.indexOf("product_data_comp.jsp") > -1 ||
            path.indexOf("product_data_fetch.jsp") > -1 ||
            path.indexOf("product_amount.jsp") > -1 ||
            path.indexOf("product_price.jsp") > -1 ||
            path.indexOf("product_time.jsp") > -1 ||
            path.indexOf("salary_org.jsp") > -1 ||
            path.indexOf("salary_piece.jsp") > -1 ||
            path.indexOf("salary_time.jsp") > -1 ||
            
            path.indexOf("report_two.jsp") > -1 ||
            path.indexOf("report_three_one.jsp") > -1 ||
            path.indexOf("report_three_two.jsp") > -1 ||
            path.indexOf("report_five.jsp") > -1 ||
            path.indexOf("report_six.jsp") > -1 ||
            path.indexOf("org_salary_piece.jsp") > -1 ||
            path.indexOf("org_salary_time.jsp") > -1 ||
            path.indexOf("report_monthAnalyze.jsp") > -1 ||
            
            path.indexOf("organize.jsp") > -1 ||
            path.indexOf("wage.jsp") > -1 ||
            path.indexOf("config.jsp") > -1 ){
        	// 从session里取员工工号信息
        	 String em = (String) session.getAttribute("cur_user");
             // 判断如果没有取到员工信息,就跳转到登陆页面
              if (em == null) {
                  // 跳转到登陆页面
                  servletResponse.sendRedirect("index.jsp");
              } 
         }
         
         chain.doFilter(request, response);
		
	}

	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
