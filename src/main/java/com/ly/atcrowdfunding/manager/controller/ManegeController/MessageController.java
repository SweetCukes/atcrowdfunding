package com.ly.atcrowdfunding.manager.controller.ManegeController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manege/message")
public class MessageController {

	@RequestMapping("message")
	public String message() {
		return "manege/message/message";
	}
}
