package com.ly.atcrowdfunding.manager.controller.CommonController;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage")
public class ManagerController {

	@RequestMapping("/main")
	public String main() {
		return "manage/main";
	}
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login.htm";
	}
}
