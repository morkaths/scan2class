package com.morkath.scan2class.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.morkath.scan2class.core.BaseController;
import com.morkath.scan2class.service.ClassroomService;
import com.morkath.scan2class.service.RoleService;
import com.morkath.scan2class.service.SessionService;
import com.morkath.scan2class.service.UserService;

@Controller
@RequestMapping("/admin")
public class DashboardController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private ClassroomService classroomService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private RoleService roleService;

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("totalUsers", userService.count());
		model.addAttribute("totalClassrooms", classroomService.count());
		model.addAttribute("totalSessions", sessionService.count());
		model.addAttribute("totalRoles", roleService.count());

		// Chart: Sessions Last 7 Days
		java.time.LocalDate today = java.time.LocalDate.now();
		java.util.LinkedHashMap<String, Integer> sessionsData = new java.util.LinkedHashMap<>();
		for (int i = 6; i >= 0; i--) {
			sessionsData.put(today.minusDays(i).toString(), 0);
		}

		java.util.List<com.morkath.scan2class.entity.attendance.SessionEntity> sessions = sessionService.getAll();
		for (com.morkath.scan2class.entity.attendance.SessionEntity session : sessions) {
			if (session.getStartTime() != null) {
				String dateKey = session.getStartTime().toLocalDate().toString();
				if (sessionsData.containsKey(dateKey)) {
					sessionsData.put(dateKey, sessionsData.get(dateKey) + 1);
				}
			}
		}

		// Chart: User Roles Distribution
		java.util.Map<String, Integer> rolesData = new java.util.HashMap<>();
		for (com.morkath.scan2class.entity.auth.RoleEntity role : roleService.getAll()) {
			rolesData.put(role.getName(), 0);
		}
		java.util.List<com.morkath.scan2class.entity.auth.UserEntity> users = userService.getAll();
		for (com.morkath.scan2class.entity.auth.UserEntity user : users) {
			for (com.morkath.scan2class.entity.auth.RoleEntity role : user.getRoles()) {
				rolesData.put(role.getName(), rolesData.getOrDefault(role.getName(), 0) + 1);
			}
		}

		try {
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			model.addAttribute("sessionsChartLabels", mapper.writeValueAsString(sessionsData.keySet()));
			model.addAttribute("sessionsChartData", mapper.writeValueAsString(sessionsData.values()));

			model.addAttribute("rolesChartLabels", mapper.writeValueAsString(rolesData.keySet()));
			model.addAttribute("rolesChartData", mapper.writeValueAsString(rolesData.values()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		preparePage(model, "pages/admin/dashboard", "Dashboard");
		return "layouts/vertical";
	}
}
