package com.ly.atcrowdfunding.manager.controller.AuthController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/adv")
public class AdvController {
	
	@RequestMapping("/adv")
	public String adv(){
		return "auth/adv/adv";
	}
}
