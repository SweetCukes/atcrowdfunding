package com.ly.atcrowdfunding.manager.controller.ManegeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atcrowdfunding.bean.Page;
import com.atguigu.atcrowdfunding.common.BaseController;


@Controller
@RequestMapping("/manege/process")
public class ProcessController extends BaseController {

	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping("process")
	public String process() {
		return "manege/process/process";
	}
	
	@RequestMapping("/pageQuery")
	public Object pageQuery(Integer pageno,Integer pagesize) {
		start();
		
		try {
			//	
			ProcessDefinitionQuery query =
			repositoryService.createProcessDefinitionQuery();
			
			List<ProcessDefinition> procDefs = 
					query.listPage((pageno-1)*pagesize, pagesize);
			
			List<Map<String, Object>> procDefMapList = new ArrayList<Map<String,Object>>();
			
			for(ProcessDefinition proDef : procDefs) {
				Map<String , Object> procDefMap = new HashMap<String, Object>();
				procDefMap.put("id", proDef.getId());
				procDefMap.put("name", proDef.getName());
				procDefMap.put("key", proDef.getKey());
				procDefMap.put("version", proDef.getVersion());
				procDefMapList.add(procDefMap);
			}
			
			int count =(int)query.count();
			
			int totalno = 0;
			if (count % pagesize == 0) {
				totalno = count / pagesize;
			}else {
				totalno = count / pagesize+1;
			}
			
			Page<Map<String , Object>> procDefPage = new Page<Map<String , Object>>();
			procDefPage.setDatas(procDefMapList);
			procDefPage.setTotalsize(totalno);
			procDefPage.setTotalno(totalno);
			procDefPage.setPageno(pageno);
			procDefPage.setPagesize(pagesize);
			page(procDefPage);
			
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		return end();
		
	}
}
