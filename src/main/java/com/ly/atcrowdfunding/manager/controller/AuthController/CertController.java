package com.ly.atcrowdfunding.manager.controller.AuthController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/cert")
public class CertController {
	
	@RequestMapping("/cert")
	public String cert(){
		return "auth/cert/cert";
	}
}
