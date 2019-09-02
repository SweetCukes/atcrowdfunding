package com.ly.atcrowdfunding.interceptor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.common.Const;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.util.StringUtil;

/**
 * 授权拦截器
 * 获取用户请求地址
 * 判断请求地址是否需要授权
 * 如果不需要授权，可以直接访问
 * 如果需要授权，需要判断当前用户是否有相应的权利
 * 如果没有权利，就跳转到错误界面
 * @author ashikotakeshi
 *
 */
public class AuthInterCeptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String uri = request.getRequestURI();
		
		//获取所有的授权路径
		Set<String> allAuthURIS = 
				(Set<String>) request.getSession().getServletContext().getAttribute(Const.AUTH_URIS);
		//判断请求地址是否授权
		if (allAuthURIS.contains(uri)) {
			//需要授权
			Set<String> userAuthURIs = (Set<String>) request.getSession().getAttribute("userAuthURIs");
			if (userAuthURIs.contains(uri)) {
				return true;
			}else {
				response.sendRedirect("/error.htm");
				return false;
			}
		}
		
		return true;
	}

}
