package com.ly.atcrowdfunding.manager.controller.ManegeController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manege/cort")
public class CortController {

	@RequestMapping("cort")
	public String cert() {
		return "manege/cort/cort";
	}
}
