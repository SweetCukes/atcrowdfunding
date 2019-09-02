package com.ly.atcrowdfunding.manager.controller.UserController.copy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.atcrowdfunding.bean.AJAXResult;
import com.atcrowdfunding.bean.Datas;
import com.atcrowdfunding.bean.Page;
import com.atcrowdfunding.bean.Role;
import com.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.RoleService;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.StringUtil;

@Controller
@RequestMapping("/manage/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	/**
	 * 文件上传
	 * @return
	 */
	@RequestMapping("/upLoadFile")
	public String upLoadFile(HttpServletRequest req) throws Exception {
		
		MultipartHttpServletRequest request
		= (MultipartHttpServletRequest)req;
		
		MultipartFile file = request.getFile("userIcon");
		
		//System.out.println(file.getName());
		//System.out.println(file.getOriginalFilename());
		
		//文件流（装饰者设计模式）
		//文件复制
		String picsPath = req.getSession().getServletContext().getRealPath("pics");

		String filename = UUID.randomUUID().toString();
		file.transferTo(new File(picsPath+"/user/"+file.getOriginalFilename()));
//		InputStream in = file.getInputStream();
//		FileOutputStream out = new FileOutputStream(new File(picsPath+"/user/"+file.getOriginalFilename()));
//		int i = -1;
//		while ((i = in.read()) != -1) {
//			out.write(i);
//		}
//		in.close();
//		out.close();
		
		
		return "manage/user/upLoadFile";
	}
	
	
	@RequestMapping("/list")
	public String list() {
		return "manage/user/list";
	}
	
	@RequestMapping("/add")
	public String add() {
		return "manage/user/add";
	}
	
	@RequestMapping("/multiadd")
	public String multiadd() {
		return "manage/user/multiadd";
	}
	
	@RequestMapping("/edit")
	public String edit(Integer id,Model model) {
		User user = userService.queryById(id);
		model.addAttribute("user",user);
		return "manage/user/edit";
	}
	
	@RequestMapping("/assign")
	public String assign(Integer id,Model model) {
		User user = userService.queryById(id);
		model.addAttribute("user",user);
		List<Role> roles = roleService.queryAll();
		
		//获取当前用户所分配的角色ID集合
		List<Integer> roleIds = userService.queryRoleidsByUserid(id);
		
		//未分配的角色列表
		List<Role> unassignList = new ArrayList<Role>(); 
		//已分配的角色列表
		List<Role> assignList = new ArrayList<Role>();
		
		for ( Role role : roles) {
			if (roleIds.contains(role.getId())) {
				assignList.add(role);
			}else {
				unassignList.add(role);
			}
		}
		model.addAttribute("unassignList",unassignList);
		model.addAttribute("assignList",assignList);
		return "manage/user/assign";
		
	}
	
	@ResponseBody
	@RequestMapping("/assignRole")
	public Object assignRole(Integer userid,Datas ds) {
		AJAXResult<User> result = new AJAXResult<User>();
		
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userid", userid);
			paramMap.put("roleids",ds.getIds());
			int count = userService.insertUserRoles(paramMap);
			result.setSuccess(count == ds.getIds().size());
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/unassignRole")
	public Object unassignRole(Integer userid,Datas ds) {
		AJAXResult<User> result = new AJAXResult<User>();
		
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("userid", userid);
			paramMap.put("roleids", ds.getIds());
			int count = userService.deleteUserRoles(paramMap);
			result.setSuccess(count == ds.getIds().size());
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
			int count = userService.deleteUsers(ds);
			result.setSuccess(count == ds.getIds().size());
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(User user) {
		AJAXResult<User> result = new AJAXResult<User>();
		
		try {
			int count = userService.deleteUser(user);
			result.setSuccess(count == 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Object update(User user) {
		AJAXResult<User> result = new AJAXResult<User>();
		
		try {
			int count = userService.updateUser(user);
			result.setSuccess(count == 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@RequestMapping("/multiInsert")
	public String multiInsert(Datas ds) {
		List<User > userList = ds.getUsers();
		Iterator<User> userIter = userList.iterator();
		while(userIter.hasNext()) {
			 User user = userIter.next();
			 if (StringUtil.isEmpty(user.getLoginacct())) {
				userIter.remove();
			}
		}
		
		return "succes";
	}
	
	@ResponseBody
	@RequestMapping("/insert")
	public Object insert(User user,HttpServletRequest req) {
		AJAXResult<User> result = new AJAXResult<User>();
		
		try {
			//保存用户信息
			user.setCreatetime(StringUtil.getSystemtime());
			user.setUserpswd("123");
			userService.insertUser(user);
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
		AJAXResult<User> result = new AJAXResult<User>();
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
			
			List<User> users = userService.queryPageData(paramMap);
			
			int totalsize = userService.queryPageCount(paramMap);
			
			int totalno = 0;
			if (totalsize % pagesize == 0) {
				totalno = totalsize / pagesize;
			}else {
				totalno = totalsize / pagesize+1;
			}
			
			Page<User> userPage = new Page<User>();
			userPage.setDatas(users);
			userPage.setTotalsize(totalno);
			userPage.setTotalno(totalno);
			userPage.setPageno(pageno);
			userPage.setPagesize(pagesize);
			result.setPage(userPage);
			
			result.setSuccess(true);
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
