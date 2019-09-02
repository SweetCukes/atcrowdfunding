package com.ly.atcrowdfunding.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atcrowdfunding.bean.AJAXResult;
import com.atcrowdfunding.bean.Member;
import com.atcrowdfunding.bean.Permission;
import com.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.common.Const;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.portal.service.MemberService;
import com.atguigu.atcrowdfunding.util.StringUtil;

@Controller
public class DispatcherController extends BaseController{
	
	@Autowired
	private UserService userService;
	@Autowired
	private MemberService memberService;
	
	@RequestMapping("/index")
	public String index() {
		return "index"; //view name
	}
	
	@RequestMapping("/error")
	public String error() {
		return "error"; //view name
	}
	
	/**
	 * 跳转到登录
	 * @return
	 */
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	/**
	 * 跳转到会员
	 */
	@RequestMapping("/member")
	public String member() {
		return "member";
	}
	
	/**
	 * 跳转到注册
	 */
	
	@RequestMapping("/reg")
	public String reg(){
		return "reg";
	}
	 
	@ResponseBody
	@RequestMapping("/doreg")
	public Object doreg(User user) {
		AJAXResult<User> result = new AJAXResult<User>();
		try {
			user.setCreatetime(StringUtil.getSystemtime());
			userService.regUser(user);
			result.setSuccess(true);
	} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	/**
	 * 会员登录
	 * @param user
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/doMemberLogin")
	public Object doMemberLogin(Member member,HttpSession session) {
		start();
		
		try {
			Member dbMember = memberService.queryLoginMember(member);
			if (dbMember == null) {
				fail();
			}else {
				session.setAttribute(Const.LOGIN_MEMBER, dbMember);
			success();
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	/**
	 * 用户登录
	 * AJAXResult ==> JSON ==>
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/dologin")
	public Object dologin(User user,HttpSession session) {
		AJAXResult result = new AJAXResult();
		try {
			
			User dbUser = userService.queryLoginUser(user);
			if (dbUser == null) {
				result.setSuccess(false);
			}else {
				session.setAttribute(Const.LOGIN_USER, dbUser);
				
				//保存用户的权限菜单（许可）信息
				List<Permission> permissions = 
						userService.queryPermissionsByUserid(dbUser.getId());
				
				//组合父子菜单关系
				Permission root = null;
				Map<Integer, Permission> permissioMap = new HashMap<Integer, Permission>();
				//用户权限路径集合
				Set<String> userAuthURIs = new HashSet<String>();
				for (Permission permission : permissions) {
					if (StringUtil.isNotEmpty(permission.getUrl())) {
						userAuthURIs.add(permission.getUrl());
					}
					permissioMap.put(permission.getId(), permission);
				}
				
				for (Permission permission : permissions) {
					if (permission.getPid() == 0) {
						root = permission;
					}else {
						//子菜单
						Permission child = permission;
						//父菜单
						Permission parent = permissioMap.get(child.getPid());
						//组合父子菜单
						parent.getChildren().add(child);
					}
					
				}
				session.setAttribute("rootPermission", root);
				session.setAttribute("userAuthURIs", userAuthURIs);
				
				result.setSuccess(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	/*
	@RequestMapping("/dologin")
	public String dologin(User user,HttpSession session) {
		try {
			User dbUser = userService.queryLoginUser(user);
			if (dbUser == null) {
				String error = "用户账号或者密码输入不正确,登录失败";
				session.setAttribute("errorMsg",error);
				session.setAttribute("loginUser", user);
				return "redirect:/login.htm";
			}else {
				return "main";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "main";
		}
	}
	*/
}
