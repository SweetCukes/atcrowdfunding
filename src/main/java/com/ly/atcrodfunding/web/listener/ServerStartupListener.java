package com.ly.atcrodfunding.web.listener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.common.Const;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.util.StringUtil;

/*
 * 服务器启动监听器
 */
public class ServerStartupListener implements ServletContextListener {
  
	public void contextInitialized(ServletContextEvent sce) {
		// 将WEB应用路径保存在APPLICATION范围中，让所有用户共享。
		ServletContext application = sce.getServletContext();
		String appPath = application.getContextPath();
		application.setAttribute("APP_PATH", appPath);
		
		//获取所有权限路径
		ApplicationContext applicationContext = 
			WebApplicationContextUtils.getWebApplicationContext(application);
				PermissionService permissionService = applicationContext.getBean(PermissionService.class);
				List<Permission> permissions = permissionService.queryAll();
				Set<String> allAuthURIS = new HashSet<String>();
				for (Permission permission : permissions) {
					if (StringUtil.isNotEmpty(permission.getUrl())) {
						allAuthURIS.add(permission.getUrl());
				}
		}
			application.setAttribute(Const.AUTH_URIS,allAuthURIS);
			
			//哪些数据放置在application中
	}

	public void contextDestroyed(ServletContextEvent sce) {

	}
	
}
