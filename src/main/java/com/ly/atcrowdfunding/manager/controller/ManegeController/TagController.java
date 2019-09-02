package com.ly.atcrowdfunding.manager.controller.ManegeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.manager.service.TagService;
import com.atcrowdfunding.bean.AJAXResult;
import com.atcrowdfunding.bean.Tag;

@Controller
@RequestMapping("/manege/tag")
public class TagController extends BaseController<com.atcrowdfunding.bean.Tag>{

	@Autowired
	private TagService tagService;
	
	@RequestMapping("/tag")
	public String Tag() {
		return "manege/tag/tree";
	}
	
	@RequestMapping("/edit")
	public String edit() {
		return "manege/tag/edit";
	}
	
	@RequestMapping("/add")
	public String add() {
		return "manege/tag/add";
	}
	@ResponseBody
	@RequestMapping("/insert")
	public Object insert(Tag tag) {
		start();
		try {
			tagService.insertTag(tag);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		return end();
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
			List<Tag> roots = new ArrayList<Tag>();
			List<Tag> tags = tagService.queryAll();
			Map<Integer, Tag> tagMap = new HashMap<Integer, Tag>();
			for (Tag tag : tags) {
				tagMap.put(tag.getId(), tag);
			}
			for (Tag tag : tags) {
				if (tag.getPid() == 0) {
					roots.add(tag);
				}else {
						Tag child = tag;
						Tag parent = tagMap.get(child.getPid());
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
	
	@ResponseBody
	@RequestMapping("/update")
	public Object update(Tag tag) {
		start();
		try {
			tagService.updateTag(tag);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/loadAsyncDatas")
	public Object loadAsyncDatas() {
		List<Tag> roots = new ArrayList<Tag>();
		
		List<Tag> tags = tagService.queryAll();
		
		Map<Integer, Tag> tagMap = new HashMap<Integer, Tag>();
		//查询数据
		for (Tag tag : tags) {
			tagMap.put(tag.getId(), tag);
		}
		//100
		for (Tag tag : tags) {
			if (tag.getPid() == 0) {
				roots.add(tag);
			}else {
					Tag child = tag;
					Tag parent = tagMap.get(child.getPid());
					parent.getChildren().add(child);
			}
		
		}
		
		return roots;
	}
	
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(Integer id) {
		start();
		try {
			tagService.deleteTagById(id);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		return end();
	}
	/**
	 * 查询子节点
	 * 递归算法
	 * 1)自己调用自己
	 * 2)算法应该具有跳出的逻辑
	 */
	private void queryChildTags(Tag parent) {
		
		//查询子节点集合
		List<Tag> childTags = tagService.queryChildTags(parent.getId());
		for (Tag child : childTags) {
			queryChildTags(child);
		}
		//组合父子节点关系
	
		parent.setChildren(childTags);
	}
}
