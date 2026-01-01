package com.morkath.scan2class.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.morkath.scan2class.dto.ClassroomDto;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.entity.classroom.ClassroomEntity;
import com.morkath.scan2class.service.ClassroomService;
import com.morkath.scan2class.service.UserService;
import com.morkath.scan2class.util.HashUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/classrooms")
public class ClientClassroomController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ClientClassroomController.class);

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model) {
        UserEntity currentUser = userService.getCurrent();
        List<ClassroomDto> dtos = classroomService.getByOwner(currentUser).stream().map(classroom -> {
            ClassroomDto dto = new ClassroomDto();
            dto.setId(classroom.getId());
            dto.setCode(classroom.getCode());
            dto.setName(classroom.getName());
            dto.setRoom(classroom.getRoom());
            dto.setStatus(classroom.getStatus());
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("classrooms", dtos);
        AssetDto assets = new AssetDto("My Classrooms");
        assets.addStylesheets("/assets/compiled/css/table-datatable.css");
        assets.addScripts("/assets/extensions/simple-datatables/umd/simple-datatables.js");
        assets.addScripts("/assets/static/js/pages/simple-datatables.js");
        preparePage(model, "pages/classroom/index", assets);
        return "layouts/horizontal";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("dto", new ClassroomDto());
        preparePage(model, "pages/classroom/create", "Thêm lớp học mới");
        return "layouts/horizontal";
    }

    @PostMapping("/create")
    public String doCreate(@Valid @ModelAttribute("dto") ClassroomDto dto, BindingResult result, Model model) {
        logger.info("USER - Starting Create Classroom: Code={}, Name={}", dto.getCode(), dto.getName());

        if (dto.getCode() != null && !dto.getCode().isEmpty()) {
            if (classroomService.getByCode(dto.getCode()) != null) {
                logger.warn("Duplicate Classroom Code detected: {}", dto.getCode());
                result.rejectValue("code", "error.code", "Mã lớp đã tồn tại");
            }
        }

        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            result.rejectValue("code", "error.code", "Mã lớp không được để trống");
        }

        if (result.hasErrors()) {
            logger.warn("Validation failed: {}", result.getAllErrors());
            model.addAttribute("dto", dto);
            preparePage(model, "pages/classroom/create", "Thêm lớp học mới");
            return "layouts/horizontal";
        }

        try {
            UserEntity currentUser = userService.getCurrent();
            logger.info("Current User: {}", (currentUser != null ? currentUser.getUsername() : "NULL"));

            if (currentUser == null) {
                logger.error("User not found in session!");
                return "redirect:/auth/login";
            }

            ClassroomEntity classroom = new ClassroomEntity();
            classroom.setCode(dto.getCode());
            classroom.setName(dto.getName());
            classroom.setRoom(dto.getRoom());
            classroom.setStatus(1);
            classroom.setOwner(currentUser);

            classroomService.save(classroom);
            logger.info("Classroom created successfully: {}", classroom.getId());

        } catch (Exception e) {
            logger.error("Error saving classroom: {}", e.getMessage(), e);
            e.printStackTrace();
            result.reject("error.global", "Lỗi hệ thống: " + e.getMessage());
            model.addAttribute("dto", dto);
            preparePage(model, "pages/classroom/create", "Thêm lớp học mới");
            return "layouts/horizontal";
        }

        return "redirect:/classrooms";
    }

    @GetMapping("/{id}")
    public String getDetail(@PathVariable("id") Long id, Model model) {
        ClassroomEntity classroom = classroomService.getById(id);
        UserEntity currentUser = userService.getCurrent();

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        // Initialize lazy collections
        classroom.getSessions().size();
        classroom.getParticipants().size();

        model.addAttribute("classroom", classroom);
        model.addAttribute("joinCode", HashUtils.encodeId(classroom.getId()));

        AssetDto assets = new AssetDto("Chi tiết lớp học");
        assets.addStylesheets("/assets/compiled/css/table-datatable.css");
        assets.addScripts("/assets/extensions/simple-datatables/umd/simple-datatables.js");
        assets.addScripts("/assets/static/js/pages/simple-datatables.js");

        preparePage(model, "pages/classroom/detail", assets);
        return "layouts/horizontal";
    }

    @GetMapping("/join")
    public String join(@RequestParam("code") String code, Model model, RedirectAttributes redirectAttributes) {
        Long classroomId = HashUtils.decodeId(code);

        if (classroomId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mã mời không hợp lệ.");
            return "redirect:/classrooms";
        }

        ClassroomEntity classroom = classroomService.getById(classroomId);
        if (classroom == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lớp học không tồn tại.");
            return "redirect:/classrooms";
        }

        model.addAttribute("classroom", classroom);
        model.addAttribute("code", code);
        preparePage(model, "pages/classroom/join", "Tham gia lớp học");
        return "layouts/horizontal";
    }

    @PostMapping("/join")
    @Transactional
    public ResponseEntity<Map<String, Object>> joinSubmit(@RequestParam("code") String code) {
        Map<String, Object> response = new HashMap<>();
        UserEntity currentUser = userService.getCurrent();
        Long classroomId = HashUtils.decodeId(code);

        if (classroomId == null) {
            response.put("message", "Mã mời không hợp lệ.");
            return ResponseEntity.badRequest().body(response);
        }

        ClassroomEntity classroom = classroomService.getById(classroomId);
        if (classroom == null) {
            response.put("message", "Lớp học không tồn tại.");
            return ResponseEntity.badRequest().body(response);
        }

        // Check if user is already a participant
        boolean alreadyJoined = classroom.getParticipants().stream()
                .anyMatch(p -> p.getUser().getId().equals(currentUser.getId()));

        if (alreadyJoined) {
            response.put("message", "Bạn đã là thành viên của lớp học này rồi.");
            return ResponseEntity.badRequest().body(response);
        }

        classroom.addParticipant(currentUser);
        classroomService.save(classroom);

        response.put("success", true);
        response.put("classroomId", classroom.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        ClassroomEntity classroom = classroomService.getById(id);
        UserEntity currentUser = userService.getCurrent();

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        ClassroomDto dto = new ClassroomDto();
        dto.setId(classroom.getId());
        dto.setCode(classroom.getCode());
        dto.setName(classroom.getName());
        dto.setRoom(classroom.getRoom());
        dto.setStatus(classroom.getStatus());

        model.addAttribute("dto", dto);
        preparePage(model, "pages/classroom/edit", "Chỉnh sửa lớp học");
        return "layouts/horizontal";
    }

    @PostMapping("/edit")
    public String doEdit(@Valid @ModelAttribute("dto") ClassroomDto dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("dto", dto);
            preparePage(model, "pages/classroom/edit", "Chỉnh sửa lớp học");
            return "layouts/horizontal";
        }

        ClassroomEntity classroom = classroomService.getById(dto.getId());
        UserEntity currentUser = userService.getCurrent();

        if (classroom == null || !classroom.getOwner().getId().equals(currentUser.getId())) {
            return "redirect:/classrooms";
        }

        classroom.setName(dto.getName());
        classroom.setRoom(dto.getRoom());
        classroom.setStatus(dto.getStatus());
        // Owner remains same
        classroomService.save(classroom);
        return "redirect:/classrooms/" + classroom.getId();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        ClassroomEntity classroom = classroomService.getById(id);
        UserEntity currentUser = userService.getCurrent();

        if (classroom != null && classroom.getOwner().getId().equals(currentUser.getId())) {
            classroomService.delete(id);
        }
        return "redirect:/classrooms";
    }

    @PostMapping("/{classroomId}/participants/{userId}/remove")
    public String removeStudent(
            @PathVariable("classroomId") Long classroomId,
            @PathVariable("userId") Long userId,
            org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {

        try {
            ClassroomEntity classroom = classroomService.getById(classroomId);
            UserEntity currentUser = userService.getCurrent();

            if (classroom == null) {
                ra.addFlashAttribute("errorMessage", "Lớp học không tồn tại.");
                return "redirect:/classrooms";
            }

            if (!classroom.getOwner().getId().equals(currentUser.getId())) {
                ra.addFlashAttribute("errorMessage", "Bạn không có quyền thực hiện thao tác này.");
                return "redirect:/classrooms/" + classroomId;
            }

            classroomService.removeStudentFromClass(userId, classroomId);
            ra.addFlashAttribute("message", "Đã mời sinh viên ra khỏi lớp thành công.");

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/classrooms/" + classroomId;
    }
}
