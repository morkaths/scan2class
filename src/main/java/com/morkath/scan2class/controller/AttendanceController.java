package com.morkath.scan2class.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.morkath.scan2class.core.BaseController;
import com.morkath.scan2class.dto.AssetDto;
import com.morkath.scan2class.dto.ClassroomStatsDTO;
import com.morkath.scan2class.dto.SessionStatsDTO;
import com.morkath.scan2class.entity.attendance.SessionEntity;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.repository.attendance.SessionRepository;
import com.morkath.scan2class.service.AttendanceService;
import com.morkath.scan2class.service.UserService;

@Controller
@RequestMapping("/attend")
public class AttendanceController extends BaseController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private SessionRepository sessionRepository;

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
            SessionEntity session = sessionRepository.findByToken(token);
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

    @PostMapping("/manual-update")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @RequestParam("userId") Long userId,
            @RequestParam("sessionId") Long sessionId,
            @RequestParam("status") String status,
            @RequestParam(name = "note", required = false) String note) {

        Map<String, Object> response = new HashMap<>();
        UserEntity teacher = userService.getCurrent();

        try {
            attendanceService.updateAttendanceStatus(userId, sessionId, status, note, teacher);

            response.put("success", true);
            response.put("message", "Cập nhật trạng thái thành công!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/classrooms/{classroomId}/analytics")
    public String analytics(@PathVariable("classroomId") Long classroomId,
            Model model) {
        model.addAttribute("classroomId", classroomId);
        AssetDto assets = new AssetDto("Thống kê điểm danh");
        assets.addStylesheets("/assets/compiled/css/table-datatable.css");
        assets.addScripts("/assets/extensions/simple-datatables/umd/simple-datatables.js");
        assets.addScripts("/assets/static/js/pages/simple-datatables.js");
        preparePage(model, "pages/classroom/stats", assets);
        return "layouts/horizontal";
    }

    @GetMapping("/classrooms/{classroomId}/stats")
    public ResponseEntity<?> getClassroomStats(@PathVariable("classroomId") Long classroomId) {
        try {
            ClassroomStatsDTO stats = attendanceService.getClassroomStatistics(classroomId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/sessions/{sessionId}/stats")
    public ResponseEntity<?> getSessionStats(@PathVariable("sessionId") Long sessionId) {
        try {
            SessionStatsDTO stats = attendanceService.getSessionStatistics(sessionId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/classrooms/{classroomId}/export")
    public ResponseEntity<byte[]> exportStats(@PathVariable("classroomId") Long classroomId) {
        try {
            byte[] fileContent = attendanceService.exportClassroomStatsToExcel(classroomId);
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
            String filename = String.format("Report_%s.xlsx", date);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + ".xlsx\"")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(fileContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
