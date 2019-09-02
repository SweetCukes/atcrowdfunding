package com.ly.atcrowdfunding.manager.controller.UserController.copy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atcrowdfunding.bean.AJAXResult;
import com.atcrowdfunding.bean.Datas;
import com.atcrowdfunding.bean.Page;
import com.atcrowdfunding.bean.Role;
import com.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.manager.service.RoleService;

@Controller
@RequestMapping("/manage/role")
public class RoleController extends BaseController<Role> {
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping("/list")
	public String list() {
		return "manage/role/list";
	}
	
	@RequestMapping("/add")
	public String add() {
		return "manage/role/add";
	}
	
	@RequestMapping("/role")
	public String role() {
		return "manage/role/role";
	}
	
	
	@RequestMapping("/edit")
	public String edit(Integer id,Model model) {
		Role role = roleService.queryById(id);
		model.addAttribute("role",role);
		return "manage/role/edit";
	}
	
	@RequestMapping("/assign")
	public String assign(Integer id,Model model) {
		Role role = roleService.queryById(id);
		model.addAttribute("role",role);
		return "manage/role/assign";
	}
	
	@ResponseBody
	@RequestMapping("/assignPermission")
	public Object assignPermission(Integer roleid, Datas ds) {
		start();
		
		try {
			
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("roleid", roleid);
			paraMap.put("permissionids", ds.getIds());
			roleService.insertRolePermissions(paraMap);
			
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
	}
	
	
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(Role role) {
		AJAXResult<Role> result = new AJAXResult<Role>();
		
		try {
			int count = roleService.deleteRole(role);
			result.setSuccess(count == 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Object update(Role role) {
		AJAXResult<Role> result = new AJAXResult<Role>();
		
		try {
			int count = roleService.updateRole(role);
			result.setSuccess(count == 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping("/insert")
	public Object insert(Role role) {
		AJAXResult<Role> result = new AJAXResult<Role>();
		
		try {
			//保存用户信息
			roleService.insertRole(role);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	/**
	 * 分页查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(String querytext,Integer pageno,Integer pagesize) {
		AJAXResult<Role> result = new AJAXResult<Role>();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("start", (pageno-1)*pagesize);
			paramMap.put("size", pagesize);
			if (org.apache.commons.lang3.StringUtils.isNotEmpty(querytext)) {
				if (querytext.indexOf("\\")!=-1) {
					querytext = querytext.replaceAll("\\", "\\\\\\\\%");
				}
				if (querytext.indexOf("_")!=-1) {
					querytext = querytext.replaceAll(")", "\\\\_%");
				}
				if (querytext.indexOf("%")!=-1) {
					querytext = querytext.replaceAll("%", "\\\\%");
				}
					paramMap.put("querytext", querytext);
				}
			
			List<Role> roles = roleService.queryPageData(paramMap);
			
			int totalsize = roleService.queryPageCount(paramMap);
			
			int totalno = 0;
			if (totalsize % pagesize == 0) {
				totalno = totalsize / pagesize;
			}else {
				totalno = totalsize / pagesize+1;
			}
			
			Page<Role> rolePage = new Page<Role>();
			rolePage.setDatas(roles);
			rolePage.setTotalsize(totalno);
			rolePage.setTotalno(totalno);
			rolePage.setPageno(pageno);
			rolePage.setPagesize(pagesize);
			result.setPage(rolePage);
			
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/deletes")
	public Object deletes(Datas ds) {
		AJAXResult<User> result = new AJAXResult<User>();
		
		try {
			int count = roleService.deleteRoles(ds);
			result.setSuccess(count == ds.getIds().size());
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	/*
	@RequestMapping("/list")
	public String list(@RequestParam(required = false,defaultValue = "1")Integer pageno, @RequestParam(required = false,defaultValue = "2")Integer pagesize,Model model) {
		
		//查询用户分页数据
		//limit startINdex,size
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("start", (pageno-1)*pagesize);
		paramMap.put("size", pagesize);
		
		List<User> users = userService.queryPageData(paramMap);
		
		int totalsize = userService.queryPageCount(paramMap);
		
		int totalno = 0;
		if (totalsize % pagesize == 0) {
			totalno = totalsize / pagesize;
		}else {
			totalno = totalsize / pagesize+1;
		}
		
		//查询结果保存到请求范围中
		model.addAttribute("users",users);
		model.addAttribute("totalno",totalno);
		model.addAttribute("pageno", pageno);
		
		return "manage/user/list";
	}
	*/
}
