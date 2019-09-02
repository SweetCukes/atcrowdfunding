package com.ly.atcrowdfunding.manager.controller.ManegeController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manege/project_type")
public class Project_typeController {

	@RequestMapping("project_type")
	public String project_type() {
		return "manege/project_type/project_type";
	}
}
