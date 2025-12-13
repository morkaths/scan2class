package com.morkath.scan2class.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.morkath.scan2class.core.BaseController;

@Controller
@RequestMapping("/error")
public class ErrorController extends BaseController {
	
	@GetMapping("/404")
    public String error404(Model model) {
        preparePage(model, "pages/error/404", "Not Found");
        return "layouts/error";
    }

    @GetMapping("/403")
    public String error403(Model model) {
        preparePage(model, "pages/error/403", "Access Forbidden");
        return "layouts/error";
    }

    @GetMapping("/500")
    public String error500(Model model) {
        preparePage(model, "pages/error/500", "System Error");
        return "layouts/error";
    }
}
