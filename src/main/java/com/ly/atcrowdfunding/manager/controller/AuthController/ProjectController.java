package com.ly.atcrowdfunding.manager.controller.AuthController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/project")
public class ProjectController {
	
	@RequestMapping("/project")
	public String cert(){
		return "auth/project/project";
	}
}