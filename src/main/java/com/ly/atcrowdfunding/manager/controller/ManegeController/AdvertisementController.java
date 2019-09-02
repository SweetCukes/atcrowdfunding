package com.ly.atcrowdfunding.manager.controller.ManegeController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manege/advertisement")
public class AdvertisementController {

	@RequestMapping("advertisement")
	public String Advertisement() {
		return "manege/advertisement/advertisement";
	}
}
