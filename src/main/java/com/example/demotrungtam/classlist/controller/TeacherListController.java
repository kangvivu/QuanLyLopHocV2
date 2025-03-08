package com.example.demotrungtam.classlist.controller;

import com.example.demotrungtam.classlist.dto.TeacherDto;
import com.example.demotrungtam.classlist.service.TeacherListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TeacherListController {
    private TeacherListService teacherListService;

    @Autowired
    public TeacherListController(TeacherListService teacherListService) {
        this.teacherListService = teacherListService;
    }

    @GetMapping("/getAllTeacher")
    public ResponseEntity<Object> getAllTeacher() {
        List<TeacherDto> teachers = this.teacherListService.getAllTeacher();
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("teachers", teachers);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
