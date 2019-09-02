package com.ly.atcrowdfunding.manager.controller.UserController.copy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atcrowdfunding.bean.AJAXResult;
import com.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;

@Controller
@RequestMapping("/manage/permission")
public class PermissionController extends BaseController<Permission> {

	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/permission")
	public String permission(){
		return "manage/permission/permission";
	}
	
	@RequestMapping("/add")
	public String add(){
		return "manage/permission/add";
	}
	
	@RequestMapping("/edit")
	public String edit(Integer id,Model model){
		
		Permission permission = permissionService.queryById(id);
		model.addAttribute("permission",permission);
		
		return "manage/permission/edit";
	}
	
	@ResponseBody
	@RequestMapping("/insert")
	public Object insert(Permission permission) {
		//AJAXResult<Permission> result  =new AJAXResult<Permission>();
		start();
		try {
			permissionService.insertPermission(permission);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		return end();
	}
	
	/**
	 * 修改许可数据
	 * @param permission
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update")
	public Object update(Permission permission) {
		start();
		try {
			permissionService.updatePermission(permission);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(Integer id) {
		start();
		try {
			permissionService.deletePermissionById(id);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/loadAsyncCheckedDatas")
	public Object loadAsyncCheckedDatas(Integer roleid) {
		List<Permission> roots = new ArrayList<Permission>();
		
		List<Permission> permissions = permissionService.queryAll();
		
		// 获取角色许可的关系数据
		List<Integer> permissionIds = permissionService.queryPermissionIdsByRoleid(roleid);
		
		Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();
		//查询数据
		for (Permission permission : permissions) {
			//判断当前许可是否在关系数据中
			if (permissionIds.contains(permission.getId())) {
				permission.setChecked(true);
			}
			permissionMap.put(permission.getId(), permission);
		}
		//100
		for (Permission permission : permissions) {
			if (permission.getPid() == 0) {
				roots.add(permission);
			}else {
					Permission child = permission;
					Permission parent = permissionMap.get(child.getPid());
					parent.getChildren().add(child);
			}
		
		}
		
		return roots;
	}
	
	@ResponseBody
	@RequestMapping("/loadAsyncDatas")
	public Object loadAsyncDatas() {
		List<Permission> roots = new ArrayList<Permission>();
		
		List<Permission> permissions = permissionService.queryAll();
		
		Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();
		//查询数据
		for (Permission permission : permissions) {
			permissionMap.put(permission.getId(), permission);
		}
		//100
		for (Permission permission : permissions) {
			if (permission.getPid() == 0) {
				roots.add(permission);
			}else {
					Permission child = permission;
					Permission parent = permissionMap.get(child.getPid());
					parent.getChildren().add(child);
			}
		
		}
		
		return roots;
	}
	
	/**
	 * 查询许可树形数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loadDatas")
	public Object loadDatas() {
		AJAXResult result = new AJAXResult();
		try {
			List<Permission> roots = new ArrayList<Permission>();
			List<Permission> permissions = permissionService.queryAll();
			Map<Integer, Permission> permissionMap = new HashMap<Integer, Permission>();
			for (Permission permission : permissions) {
				permissionMap.put(permission.getId(), permission);
			}
			for (Permission permission : permissions) {
				if (permission.getPid() == 0) {
					roots.add(permission);
				}else {
						Permission child = permission;
						Permission parent = permissionMap.get(child.getPid());
						parent.getChildren().add(child);
				}
			}
			result.setData(roots);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	/**
	 * 查询子节点
	 * 递归算法
	 * 1)自己调自己
	 * 2)算法应具有跳出的逻辑
	 */
	private void queryChildPermissions(Permission parent) {
		
		List<Permission> childPermissions = permissionService.queryChildPermissions(parent.getId());
		//查询子节点集合 
		for (Permission child : childPermissions) {
			queryChildPermissions(child);
		}
		//组合父子节点的关系
		parent.setChildren(childPermissions);
	}
}
