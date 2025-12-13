package com.morkath.scan2class.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.morkath.scan2class.core.BaseController;
import com.morkath.scan2class.dto.LoginForm;
import com.morkath.scan2class.dto.RegisterForm;
import com.morkath.scan2class.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController {
	@Autowired
	private AuthService authService;

	@GetMapping("/login")
	public String login(Model model, @RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "registered", required = false) String registered) {
		preparePage(model, "pages/auth/login", "Login");
		model.addAttribute("loginForm", new LoginForm());
		if (registered != null) {
			model.addAttribute("message", "Registration successful. Please login.");
		}
		if (error != null) {
			model.addAttribute("errorMessage", "Invalid username or password.");
		}
		return "layouts/auth";
	}

	@GetMapping("/register")
	public String register(Model model) {
		preparePage(model, "pages/auth/register", "Register");
		model.addAttribute("registerForm", new RegisterForm());
		return "layouts/auth";
	}

	@PostMapping("/register")
	public String doRegister(Model model, @Valid @ModelAttribute("registerForm") RegisterForm registerForm,
			BindingResult bindingResult) {
		preparePage(model, "pages/auth/register", "Register");
		if (bindingResult.hasErrors()) {
			preparePage(model, "pages/auth/register", "Register");
			return "layouts/auth";
		}
		boolean success = authService.register(registerForm.getUsername(), registerForm.getPassword(),
				registerForm.getEmail());
		if (!success) {
			preparePage(model, "pages/auth/register", "Register");
			model.addAttribute("errorMessage", "Username or email already exists.");
			return "layouts/auth";
		}
		return "redirect:/auth/login?registered=true";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/auth/login";
	}
}
