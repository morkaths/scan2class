package com.morkath.scan2class.controller;

import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.morkath.scan2class.core.BaseController;
import com.morkath.scan2class.dto.AssetDto;
import com.morkath.scan2class.dto.RoleDto;
import com.morkath.scan2class.entity.auth.RoleEntity;
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
		model.addAttribute("roles", roleService.getAll());
		model.addAttribute("permissions", permissionService.getAll());
		AssetDto assets = new AssetDto("Roles Management");
		assets.addStylesheets("/assets/compiled/css/table-datatable.css");
		assets.addScripts("/assets/extensions/simple-datatables/umd/simple-datatables.js");
		assets.addScripts("/assets/static/js/pages/simple-datatables.js");
		preparePage(model, "pages/admin/role/index", assets);
		return "layouts/vertical";
	}
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("dto", new RoleDto());
		model.addAttribute("permissions", permissionService.getAll());
		preparePage(model, "pages/admin/role/create", "Create New Role");
		return "layouts/vertical";
	}
	
	@PostMapping("/create")
	public String doCreate(
			@Valid @ModelAttribute("dto") RoleDto dto, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("dto", dto);
			model.addAttribute("permissions", permissionService.getAll());
			System.out.println(permissionService.getAll());
			preparePage(model, "pages/admin/role/create", "Create New Role");
			return "layouts/vertical";
		}
		System.out.println("Received permissionIds: " + dto.getPermissionIds());
		RoleEntity role = new RoleEntity();
		role.setName(dto.getName());
		role.setCode(dto.getCode());
		role.setPermissions(new HashSet<>(permissionService.getByIds(dto.getPermissionIds())));
		roleService.save(role);
		return "redirect:/admin/roles";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		model.addAttribute("dto", roleService.getById(id));
		model.addAttribute("permissions", permissionService.getAll());
		preparePage(model, "pages/admin/role/edit", "Edit Role");
		return "layouts/vertical";
	}
	
	@PostMapping("/edit")
	public String doEdit(@Valid @ModelAttribute("dto") RoleDto dto, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("dto", dto);
			model.addAttribute("permissions", permissionService.getAll());
			preparePage(model, "pages/admin/role/edit", "Edit Role");
			return "layouts/vertical";
		}
		RoleEntity role = new RoleEntity();
		role.setId(dto.getId());
		role.setName(dto.getName());
		role.setCode(dto.getCode());
		role.setPermissions(new HashSet<>(permissionService.getByIds(dto.getPermissionIds())));
		roleService.save(role);
		return "redirect:/admin/roles";
	}
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id) {
		roleService.delete(id);
		return "redirect:/admin/roles";
	}
}
