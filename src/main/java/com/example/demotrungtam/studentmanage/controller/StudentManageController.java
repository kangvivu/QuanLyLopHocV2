package com.example.demotrungtam.studentmanage.controller;

import com.example.demotrungtam.studentmanage.dto.StudentManageDto;
import com.example.demotrungtam.studentmanage.dto.StudentRegistrationDto;
import com.example.demotrungtam.studentmanage.service.StudentManageService;
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
public class StudentManageController {
    private final StudentManageService studentManageService;

    @Autowired
    public StudentManageController(StudentManageService studentManageService) {
        this.studentManageService = studentManageService;
    }

    @GetMapping("/get_students")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getStudentManage(
            @RequestParam(value = "student_name", required = false) String studentName,
            @RequestParam(value = "page_number", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "page_size", defaultValue = "5") Integer pageSize
    ) {
        // Xử lý kí tự "%" và "_"
        if (studentName != null) {
            if (studentName.contains("%")) {
                studentName = studentName.replace("%","\\%");
            } else if (studentName.contains("_")) {
                studentName = studentName.replace("_","\\_");
            }
        }

        List<StudentManageDto> students
                = studentManageService.getStudentManage(studentName, pageNumber, pageSize);
        int total = studentManageService.countStudentManage(studentName);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("total", total);
        response.put("pageNumber", pageNumber);
        response.put("pageSize", pageSize);
        response.put("students", students);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/save_student")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> saveStudent(
            @RequestBody StudentRegistrationDto studentRegistrationDto
    ) {
        int result = studentManageService.saveStudent(studentRegistrationDto);
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "Save student successfully");
        response.put("student_id", result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get_students_out_class")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getStudentOutClassManage(
            @RequestParam(value = "class_id", required = false) Integer classId
    ) {

        int check = classId;
//        List<StudentManageDto> students
//                = studentManageService.getStudentManage(studentName, pageNumber, pageSize);
//        int total = studentManageService.countStudentManage(studentName);
        Map<String, Object> response = new LinkedHashMap<>();
//        response.put("code", HttpStatus.OK.value());
//        response.put("total", total);
//        response.put("pageNumber", pageNumber);
//        response.put("pageSize", pageSize);
//        response.put("students", students);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
