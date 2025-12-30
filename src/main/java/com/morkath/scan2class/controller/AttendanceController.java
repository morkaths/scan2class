package com.morkath.scan2class.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

import com.morkath.scan2class.core.BaseController;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.service.AttendanceService;
import com.morkath.scan2class.service.UserService;

@Controller
@RequestMapping("/attend")
public class AttendanceController extends BaseController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private com.morkath.scan2class.repository.attendance.SessionRepository sessionRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public String scan(@RequestParam(name = "token", required = false) String token, Model model,
            HttpServletRequest request) {
    	
        if (token == null || token.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("token", token);
        preparePage(model, "pages/attendance/scan", "Xác nhận điểm danh");
        return "layouts/horizontal";
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submit(@RequestParam("token") String token,
            @RequestParam(name = "latitude", required = false) Double latitude,
            @RequestParam(name = "longitude", required = false) Double longitude,
            @RequestParam(name = "accuracy", required = false) Double accuracy,
            @RequestParam(name = "deviceInfo", required = false) String deviceInfo,
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        UserEntity user = userService.getCurrent();

        try {
            if (deviceInfo == null || deviceInfo.isEmpty()) {
                deviceInfo = request.getHeader("User-Agent");
            }

            attendanceService.attend(user, token, latitude, longitude, accuracy, deviceInfo);

            // Fetch session to calculate distance for feedback
            com.morkath.scan2class.entity.attendance.SessionEntity session = sessionRepository.findByToken(token);
            double dist = 0.0;
            if (session != null && latitude != null && longitude != null) {
                dist = session.calculateDistance(latitude, longitude);
            }

            response.put("success", true);
            response.put("message", "Điểm danh thành công!");
            response.put("distance", Math.round(dist * 100.0) / 100.0);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
