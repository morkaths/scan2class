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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morkath.scan2class.core.BaseController;
import com.morkath.scan2class.dto.AssetDto;
import com.morkath.scan2class.dto.UserDto;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.service.RoleService;
import com.morkath.scan2class.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping
	public String index(Model model) {
		model.addAttribute("users", userService.getAll());
		AssetDto assets = new AssetDto("Users Management");
		assets.addStylesheets("/assets/compiled/css/table-datatable.css");
		assets.addScripts("/assets/extensions/simple-datatables/umd/simple-datatables.js");
		assets.addScripts("/assets/static/js/pages/simple-datatables.js");
		preparePage(model, "pages/admin/user/index", assets);
		return "layouts/vertical";
	}

	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("dto", new UserDto());
		model.addAttribute("roles", roleService.getAll());
		preparePage(model, "pages/admin/user/create", "Create New User");
		return "layouts/vertical";
	}

	@PostMapping("/create")
	public String doCreate(@Valid @ModelAttribute("dto") UserDto dto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("dto", dto);
			model.addAttribute("roles", roleService.getAll());
			preparePage(model, "pages/admin/user/create", "Create New User");
			return "layouts/vertical";
		}
		UserEntity user = new UserEntity();
		user.setUsername(dto.getUsername());
		user.setPassword(dto.getPassword());
		user.setEmail(dto.getEmail());
		user.setFullname(dto.getFullname());
		user.setStatus(dto.getStatus());
		user.setRoles(new HashSet<>(roleService.getByIds(dto.getRoleIds())));
		userService.save(user);
		redirectAttributes.addFlashAttribute("message", "Tạo người dùng thành công!");
		return "redirect:/admin/users";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		UserEntity user = userService.getById(id);
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setFullname(user.getFullname());
		dto.setStatus(user.getStatus());
		dto.setRoleIds(user.getRoles().stream().map(r -> r.getId()).toList());
		model.addAttribute("dto", dto);
		model.addAttribute("roles", roleService.getAll());
		preparePage(model, "pages/admin/user/edit", "Edit User");
		return "layouts/vertical";
	}

	@PostMapping("/edit")
	public String doEdit(@Valid @ModelAttribute("dto") UserDto dto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("dto", dto);
			model.addAttribute("roles", roleService.getAll());
			preparePage(model, "pages/admin/user/edit", "Edit User");
			return "layouts/vertical";
		}
		UserEntity user = new UserEntity();
		user.setUsername(dto.getUsername());
		user.setPassword(dto.getPassword());
		user.setEmail(dto.getEmail());
		user.setFullname(dto.getFullname());
		user.setStatus(dto.getStatus());
		user.setRoles(new HashSet<>(roleService.getByIds(dto.getRoleIds())));
		userService.save(user);
		redirectAttributes.addFlashAttribute("message", "Cập nhật thành công!");
		return "redirect:/admin/users";
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		userService.delete(id);
		redirectAttributes.addFlashAttribute("message", "Xóa thành công!");
		return "redirect:/admin/users";
	}

}
