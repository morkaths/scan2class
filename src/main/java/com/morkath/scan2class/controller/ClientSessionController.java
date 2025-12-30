package com.morkath.scan2class.controller;

import java.time.LocalDateTime;

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
import com.morkath.scan2class.dto.SessionDto;
import com.morkath.scan2class.entity.attendance.SessionEntity;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.entity.classroom.ClassroomEntity;
import com.morkath.scan2class.service.ClassroomService;
import com.morkath.scan2class.service.SessionService;
import com.morkath.scan2class.service.UserService;

@Controller
@RequestMapping("/classrooms/{classId}/sessions")
public class ClientSessionController extends BaseController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private UserService userService;

    @GetMapping("/create")
    public String create(@PathVariable("classId") Long classId, Model model) {
        UserEntity currentUser = userService.getCurrent();
        ClassroomEntity classroom = classroomService.getById(classId);

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        SessionDto dto = new SessionDto();
        dto.setRadius(50.0); // Default
        dto.setDuration(15); // Default

        model.addAttribute("dto", dto);
        model.addAttribute("classroom", classroom);
        preparePage(model, "pages/session/create", "Tạo phiên điểm danh");
        return "layouts/horizontal";
    }

    @PostMapping
    public String store(@PathVariable("classId") Long classId,
            @Valid @ModelAttribute("dto") SessionDto dto,
            BindingResult result,
            Model model) {

        UserEntity currentUser = userService.getCurrent();
        ClassroomEntity classroom = classroomService.getById(classId);

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        if (result.hasErrors()) {
            model.addAttribute("dto", dto);
            model.addAttribute("classroom", classroom);
            preparePage(model, "pages/session/create", "Tạo phiên điểm danh");
            return "layouts/horizontal";
        }

        SessionEntity session = new SessionEntity(dto.getName(), classroom);
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(LocalDateTime.now().plusMinutes(dto.getDuration()));
        session.setActive(true);
        session.setToken(java.util.UUID.randomUUID().toString());
        session.setTokenExpiry(session.getEndTime());
        session.setRadius(dto.getRadius());
        session.setLatitude(dto.getLatitude());
        session.setLongitude(dto.getLongitude());

        sessionService.save(session);

        return "redirect:/classrooms/" + classId + "/sessions/" + session.getId() + "/qr";
    }

    @GetMapping("/{sessionId}/qr")
    public String qr(@PathVariable("classId") Long classId,
            @PathVariable("sessionId") Long sessionId,
            Model model) {

        UserEntity currentUser = userService.getCurrent();
        ClassroomEntity classroom = classroomService.getById(classId);

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        SessionEntity session = sessionService.getById(sessionId);
        if (session == null || !session.getClassroom().getId().equals(classId) || !session.isActive()) {
            return "redirect:/classrooms/" + classId + "/sessions/" + sessionId;
        }

        model.addAttribute("classroom", classroom);
        model.addAttribute("session", session);
        preparePage(model, "pages/session/qr", "Mã QR Điểm danh");
        return "layouts/horizontal";
    }

    @PostMapping("/{sessionId}/end")
    public String end(@PathVariable("classId") Long classId,
            @PathVariable("sessionId") Long sessionId,
            RedirectAttributes redirectAttributes) {

        UserEntity currentUser = userService.getCurrent();
        ClassroomEntity classroom = classroomService.getById(classId);

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        sessionService.closeSession(sessionId);
        redirectAttributes.addFlashAttribute("message", "Phiên điểm danh đã kết thúc.");
        return "redirect:/classrooms/" + classId;
    }

    @GetMapping("/{sessionId}")
    public String detail(@PathVariable("classId") Long classId,
            @PathVariable("sessionId") Long sessionId,
            Model model) {
        UserEntity currentUser = userService.getCurrent();
        ClassroomEntity classroom = classroomService.getById(classId);

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        SessionEntity session = sessionService.getById(sessionId);
        if (session == null || !session.getClassroom().getId().equals(classId)) {
            return "redirect:/classrooms/" + classId;
        }

        model.addAttribute("classroom", classroom);
        model.addAttribute("session", session);

        AssetDto assets = new AssetDto("Chi tiết phiên");
        assets.addStylesheets("/assets/compiled/css/table-datatable.css");
        assets.addScripts("/assets/extensions/simple-datatables/umd/simple-datatables.js");
        assets.addScripts("/assets/static/js/pages/simple-datatables.js");

        preparePage(model, "pages/session/detail", assets);
        return "layouts/horizontal";
    }

    @GetMapping("/{sessionId}/edit")
    public String edit(@PathVariable("classId") Long classId,
            @PathVariable("sessionId") Long sessionId,
            Model model) {
        UserEntity currentUser = userService.getCurrent();
        ClassroomEntity classroom = classroomService.getById(classId);

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        SessionEntity session = sessionService.getById(sessionId);
        if (session == null || !session.getClassroom().getId().equals(classId)) {
            return "redirect:/classrooms/" + classId;
        }

        SessionDto dto = new SessionDto();
        dto.setName(session.getName());
        dto.setRadius(session.getRadius());
        dto.setLatitude(session.getLatitude());
        dto.setLongitude(session.getLongitude());
        if (session.getEndTime() != null && session.getStartTime() != null) {
            long minutes = java.time.Duration.between(session.getStartTime(), session.getEndTime()).toMinutes();
            dto.setDuration((int) minutes);
        }

        model.addAttribute("dto", dto);
        model.addAttribute("classroom", classroom);
        model.addAttribute("session", session);
        preparePage(model, "pages/session/edit", "Chỉnh sửa phiên");
        return "layouts/horizontal";
    }

    @PostMapping("/{sessionId}/edit")
    public String update(@PathVariable("classId") Long classId,
            @PathVariable("sessionId") Long sessionId,
            @Valid @ModelAttribute("dto") SessionDto dto,
            BindingResult result,
            Model model) {

        UserEntity currentUser = userService.getCurrent();
        ClassroomEntity classroom = classroomService.getById(classId);

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        SessionEntity session = sessionService.getById(sessionId);
        if (session == null || !session.getClassroom().getId().equals(classId)) {
            return "redirect:/classrooms/" + classId;
        }

        if (result.hasErrors()) {
            model.addAttribute("dto", dto);
            model.addAttribute("classroom", classroom);
            model.addAttribute("session", session);
            preparePage(model, "pages/session/edit", "Chỉnh sửa phiên");
            return "layouts/horizontal";
        }

        session.setName(dto.getName());
        session.setRadius(dto.getRadius());
        session.setLatitude(dto.getLatitude());
        session.setLongitude(dto.getLongitude());

        if (session.getStartTime() != null) {
            session.setEndTime(session.getStartTime().plusMinutes(dto.getDuration()));
            session.setTokenExpiry(session.getEndTime());
        }

        sessionService.save(session);

        return "redirect:/classrooms/" + classId;
    }

    @PostMapping("/{sessionId}/delete")
    public String delete(@PathVariable("classId") Long classId,
            @PathVariable("sessionId") Long sessionId,
            RedirectAttributes redirectAttributes) {

        UserEntity currentUser = userService.getCurrent();
        ClassroomEntity classroom = classroomService.getById(classId);

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        SessionEntity session = sessionService.getById(sessionId);
        if (session != null && session.getClassroom().getId().equals(classId)) {
            sessionService.delete(sessionId);
            redirectAttributes.addFlashAttribute("message", "Đã xoá phiên điểm danh.");
        }

        return "redirect:/classrooms/" + classId;
    }
}
