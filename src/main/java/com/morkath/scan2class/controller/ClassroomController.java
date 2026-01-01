package com.morkath.scan2class.controller;

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

@Controller
@RequestMapping("/admin/classrooms")
public class ClassroomController extends BaseController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("classrooms", classroomService.getAll());
        AssetDto assets = new AssetDto("Classroom Management");
        assets.addStylesheets("/assets/compiled/css/table-datatable.css");
        assets.addScripts("/assets/extensions/simple-datatables/umd/simple-datatables.js");
        assets.addScripts("/assets/static/js/pages/simple-datatables.js");
        preparePage(model, "pages/admin/classroom/index", assets);
        return "layouts/vertical";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("dto", new ClassroomDto());
        model.addAttribute("users", userService.getAll());
        preparePage(model, "pages/admin/classroom/create", "Create New Classroom");
        return "layouts/vertical";
    }

    @PostMapping("/create")
    public String doCreate(@Valid @ModelAttribute("dto") ClassroomDto dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("dto", dto);
            model.addAttribute("users", userService.getAll());
            preparePage(model, "pages/admin/classroom/create", "Create New Classroom");
            return "layouts/vertical";
        }

        // Check for duplicate code
        if (classroomService.getByCode(dto.getCode()) != null) {
            result.rejectValue("code", "error.dto", "Mã lớp học đã tồn tại");
            model.addAttribute("dto", dto);
            model.addAttribute("users", userService.getAll());
            preparePage(model, "pages/admin/classroom/create", "Create New Classroom");
            return "layouts/vertical";
        }

        // Manual validation for ownerId
        if (dto.getOwnerId() == null) {
            result.rejectValue("ownerId", "error.dto", "Giảng viên là bắt buộc");
            model.addAttribute("dto", dto);
            model.addAttribute("users", userService.getAll());
            preparePage(model, "pages/admin/classroom/create", "Create New Classroom");
            return "layouts/vertical";
        }

        ClassroomEntity classroom = new ClassroomEntity();
        classroom.setCode(dto.getCode());
        classroom.setName(dto.getName());
        classroom.setRoom(dto.getRoom());
        classroom.setStatus(dto.getStatus());

        UserEntity owner = userService.getById(dto.getOwnerId());
        classroom.setOwner(owner);

        classroomService.save(classroom);
        return "redirect:/admin/classrooms";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        ClassroomEntity classroom = classroomService.getById(id);
        ClassroomDto dto = new ClassroomDto();
        dto.setId(classroom.getId());
        dto.setCode(classroom.getCode());
        dto.setName(classroom.getName());
        dto.setRoom(classroom.getRoom());
        dto.setStatus(classroom.getStatus());
        if (classroom.getOwner() != null) {
            dto.setOwnerId(classroom.getOwner().getId());
        }
        model.addAttribute("dto", dto);
        model.addAttribute("users", userService.getAll());
        preparePage(model, "pages/admin/classroom/edit", "Edit Classroom");
        return "layouts/vertical";
    }

    @PostMapping("/edit")
    public String doEdit(@Valid @ModelAttribute("dto") ClassroomDto dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("dto", dto);
            model.addAttribute("users", userService.getAll());
            preparePage(model, "pages/admin/classroom/edit", "Edit Classroom");
            return "layouts/vertical";
        }
        ClassroomEntity classroom = classroomService.getById(dto.getId());
        if (classroom == null) {
            // Handle error, maybe redirect with message
            return "redirect:/admin/classrooms";
        }
        dto.setCode(classroom.getCode());
        classroom.setName(dto.getName());
        classroom.setRoom(dto.getRoom());
        classroom.setStatus(dto.getStatus());
        classroom.setOwner(userService.getById(dto.getOwnerId()));
        classroomService.save(classroom);
        return "redirect:/admin/classrooms";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        classroomService.delete(id);
        return "redirect:/admin/classrooms";
    }
}
