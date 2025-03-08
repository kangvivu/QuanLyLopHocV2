package com.example.demotrungtam.teachermanage.controller;

import com.example.demotrungtam.studentmanage.dto.StudentRegistrationDto;
import com.example.demotrungtam.teachermanage.dto.TeacherManageDto;
import com.example.demotrungtam.teachermanage.dto.TeacherRegistrationDto;
import com.example.demotrungtam.teachermanage.service.TeacherManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TeacherManageController {
    private final TeacherManageService teacherManageService;

    @Autowired
    public TeacherManageController(TeacherManageService teacherManageService) {
        this.teacherManageService = teacherManageService;
    }

    @GetMapping("/get_teachers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getTeacherManage(
            @RequestParam(value = "teacher_name", required = false) String teacherName,
           @RequestParam(value = "page_number", defaultValue = "0") Integer pageNumber,
           @RequestParam(value = "page_size", defaultValue = "5") Integer pageSize){
        if (teacherName != null) {
            if (teacherName.contains("%")) {
                teacherName = teacherName.replace("%","\\%");
            } else if (teacherName.contains("_")) {
                teacherName = teacherName.replace("_","\\_");
            }
        }
        List<TeacherManageDto> teachers
                = teacherManageService.getTeacherManage(teacherName, pageNumber, pageSize);
        int total = teacherManageService.countTeacherManage();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("total", total);
        response.put("pageNumber", pageNumber);
        response.put("pageSize", pageSize);
        response.put("teachers", teachers);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/save_teacher")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> saveTeacher(
            @RequestBody TeacherRegistrationDto teacherRegistrationDto
    ) {
        int result = teacherManageService.saveTeacher(teacherRegistrationDto);
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "Save teacher successfully");
        response.put("teacher_id", result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Delete giáo viên
    @PostMapping("/remove_teacher")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteTeacher(
            @RequestParam (value = "user_name") String userName
    ) {
        int result = teacherManageService.deleteTeacher(userName);
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "Delete teacher successfully");
        response.put("teacher_id", result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
