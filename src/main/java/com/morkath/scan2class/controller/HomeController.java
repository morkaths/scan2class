package com.morkath.scan2class.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.morkath.scan2class.core.BaseController;

@Controller
public class HomeController extends BaseController {

	@GetMapping("/")
	public String home(Model model) {
		preparePage(model, "pages/admin/dashboard", "Dashboard");
		return "layouts/vertical";
	}

}
