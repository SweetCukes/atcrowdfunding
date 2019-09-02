package com.ly.atcrowdfunding.interceptor;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.common.Const;
/**
 * 登录拦截器
 * 判断用户是否登录
 * @author ashikotakeshi
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{

	/**
	 * 控制器之前进行处理
	 */
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/*
		 * 判断当前用户是否登录，如果登录，那么请求继续访问
		 * 如果没有登录，需要跳转到登录页面
		 */
		String uri = request.getRequestURI();
		Set<String> whiteListSet = new HashSet<String>();
		whiteListSet.add("/dologin.do");
		whiteListSet.add("/login.htm");
		whiteListSet.add("/index.htm");
		
		if (whiteListSet.contains(uri)) {
			return true;
		}
		else {
			HttpSession session = request.getSession();
			User loginUser = (User) session.getAttribute(Const.LOGIN_USER);
			
			if (loginUser == null) {
				response.sendRedirect("/login.htm");
				return false;
			}
			return true;
		}
	}
	
	/**
	 * 控制器执行之后进行处理
	 */

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 在视图解析之后进行处理
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}
	
}
