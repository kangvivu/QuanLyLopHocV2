package com.example.demotrungtam.classlist.controller;

import com.example.demotrungtam.classlist.dto.ClassDto;
import com.example.demotrungtam.classlist.dto.classEditDto.ClassDetailDto;
import com.example.demotrungtam.classlist.dto.classUpdateDto.ClassDetailUpdateDto;
import com.example.demotrungtam.classlist.dto.classUpdateDto.TeacherClassUpdateDto;
import com.example.demotrungtam.classlist.dto.class_registration_dto.ClassRegistrationDto;
import com.example.demotrungtam.classlist.service.ClassDetailClassListService;
import com.example.demotrungtam.classlist.service.ClassListService;
import com.example.demotrungtam.classlist.service.TeacherClassClassListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClassListController {
    private final ClassListService classListService;
    private final ClassDetailClassListService classDetailClassListService;
    private final TeacherClassClassListService teacherClassClassListService;

    @Autowired
    public ClassListController(
            ClassListService classListService,
            ClassDetailClassListService classDetailClassListService,
            TeacherClassClassListService teacherClassClassListService
    ) {
        this.classListService = classListService;
        this.classDetailClassListService = classDetailClassListService;
        this.teacherClassClassListService = teacherClassClassListService;
    }

    @GetMapping("/getAllClass")
    public ResponseEntity<Object> getAllClass(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "teacher_id", required = false) Integer teacherId,
            @RequestParam(value = "address_id", required = false) Integer addressId,
            @RequestParam(value = "page_number", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "page_size", defaultValue = "12") Integer pageSize
    ) {
        List<ClassDto> classes = this.classListService.getAllClass(date, teacherId, addressId, pageNumber, pageSize);
        int total = this.classListService.countAllClass(date, teacherId, addressId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("total", total);
        response.put("classes", classes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getTeacherClass")
    public ResponseEntity<Object> getTeacherClass(
            @RequestParam(value = "date") String date,
            @RequestParam(value = "id") int teacherId,
            @RequestParam(value = "address_id", required = false) Integer addressId,
            @RequestParam(value = "page_number", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "page_size", defaultValue = "12") Integer pageSize
    ) {
        List<ClassDto> classes = this.classListService.getTeacherClass(date, teacherId, addressId, pageNumber, pageSize);
        int total = this.classListService.countTeacherClass(date, teacherId, addressId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("total", total);
        response.put("classes", classes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getStudentClass")
    public ResponseEntity<Object> getStudentClass(
            @RequestParam(value = "date") String date,
            @RequestParam(value = "id") int studentId,
            @RequestParam(value = "address_id", required = false) Integer addressId,
            @RequestParam(value = "page_number", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "page_size", defaultValue = "12") Integer pageSize
    ) {
        List<ClassDto> classes = this.classListService.getStudentClass(date, studentId, addressId, pageNumber, pageSize);
        int total = 1;
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("total", total);
        response.put("classes", classes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register-class")
    public ResponseEntity<Object> registerClass(
            @RequestBody ClassRegistrationDto classRegistrationDto
    ) {
        this.classListService.registerClass(classRegistrationDto);
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "Register class successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("get_class_detail")
    public ResponseEntity<Object> getClassDetail(
            @RequestParam(value = "class_id") Integer classId
    ) {
        ClassDetailDto classDetail = this.classListService.getClassDetail(classId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("classDetail", classDetail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("save_class_detail")
    public ResponseEntity<Object> saveClassDetail(
            @RequestBody ClassDetailUpdateDto classDetailUpdateDto
    ) {
        this.classDetailClassListService.saveClassDetail(classDetailUpdateDto);
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "Save class successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("save_teacher_class")
    public ResponseEntity<Object> saveTeacherClass(
            @RequestBody TeacherClassUpdateDto teacherClassUpdateDto
    ) {
        this.teacherClassClassListService.saveTeacherClass(teacherClassUpdateDto);
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "Save teacherClass successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
