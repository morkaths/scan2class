package com.morkath.scan2class.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.morkath.scan2class.core.BaseController;
import com.morkath.scan2class.service.PermissionService;
import com.morkath.scan2class.service.RoleService;

@Controller
@RequestMapping("/admin/roles")
public class RoleController extends BaseController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PermissionService permissionService;
	
	@GetMapping
	public String index(Model model) {
		model.addAttribute("roles", roleService.getList());
		model.addAttribute("permissions", permissionService);
		preparePage(model, "pages/role/index", "Roles Management");
		return "layouts/vertical";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("permissions", permissionService.getAll());
		preparePage(model, "pages/role/create", "Create New Role");
		return "layouts/vertical";
	}
}
