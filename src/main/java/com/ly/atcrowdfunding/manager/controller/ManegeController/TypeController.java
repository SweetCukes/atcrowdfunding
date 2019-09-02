package com.ly.atcrowdfunding.manager.controller.ManegeController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.common.BaseController;
import com.atguigu.atcrowdfunding.manager.service.CertService;

@Controller
@RequestMapping("/manege/type")
public class TypeController extends BaseController{

	@Autowired
	private CertService certService;
	
	@RequestMapping("type")
	public String type( Model model ) {
		
		//查询所有证明文件
		List<Cert> certs = certService.queryAll();
		 model.addAttribute("certs",certs);
		 
		 //查询关系数据
		 List<Map<String, Object>> datas = certService.queryAccttypeCerts();
		 model.addAttribute("datas",datas);
		 
		return "manege/type/type";
	}
	
	@ResponseBody
	@RequestMapping("/deleteAccttypeCert")
	
	public Object deleteAccttypeCert( Integer certid,  String accttype) {
		start();
		
		try {
			Map<String , Object> paramMap = new HashMap<String, Object>();
			paramMap.put("certid", certid);
			paramMap.put("accttype", accttype);
			certService.deleteAccttypeCert(paramMap);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		return end();
	}
	
	@ResponseBody
	@RequestMapping("/insertAccttypeCert")
	
	public Object insertAccttypeCert( Integer certid,  String accttype) {
		start();
		
		try {
			Map<String , Object> paramMap = new HashMap<String, Object>();
			paramMap.put("certid", certid);
			paramMap.put("accttype", accttype);
			certService.insertAccttypeCert(paramMap);
			success();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		return end();
	}
}
