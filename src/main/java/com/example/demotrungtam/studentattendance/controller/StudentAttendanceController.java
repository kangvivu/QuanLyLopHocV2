package com.example.demotrungtam.studentattendance.controller;

import com.example.demotrungtam.studentattendance.dto.StudentAttendanceDailyDto;
import com.example.demotrungtam.studentattendance.dto.StudentClassDto;
import com.example.demotrungtam.studentattendance.dto.student_attendance_daily_post_dto.StudentAttendanceDailyPostDto;
import com.example.demotrungtam.studentattendance.service.ClassService;
import com.example.demotrungtam.studentattendance.service.StudentAttendanceService;
import com.example.demotrungtam.studentattendance.service.ValidateRoleTeacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StudentAttendanceController {
    private final StudentAttendanceService studentAttendanceService;
    private final ClassService classService;
    private final ValidateRoleTeacher validateRoleTeacher;

    @Autowired
    public StudentAttendanceController(
            StudentAttendanceService studentAttendanceService,
            ClassService classService,
            ValidateRoleTeacher validateRoleTeacher
    ) {
        this.studentAttendanceService = studentAttendanceService;
        this.classService = classService;
        this.validateRoleTeacher = validateRoleTeacher;
    }

    @GetMapping("/student-attendance")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getStudentAttendance(
            @RequestParam(value = "class_id") int classId,
            @RequestParam(value = "date") String date
    ) {
        this.validateRoleTeacher.validateRoleTeacher(classId, date);
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> studentAttendance = this.studentAttendanceService.getStudentAttendance(classId, date);
        Map<String, Object> classDetail = this.classService.findClassDetail(classId, date);
        int totalStudent = this.studentAttendanceService.countStudentAttendance(classId, date);
        List<Map<String, Object>> attendanceDate = this.studentAttendanceService.getAttendanceDate(classId, date);
        response.put("code", HttpStatus.OK.value());
        response.putAll(classDetail);
        response.put("attendanceDate", attendanceDate);
        response.put("totalStudent", totalStudent);
        response.put("studentAttendance", studentAttendance);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/student-attendance-daily")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getStudentAttendanceDaily(
            @RequestParam(value = "class_id") int classId,
            @RequestParam(value = "date") String date
    ) {
        this.validateRoleTeacher.validateRoleTeacher(classId, date.substring(0, 7));
        Map<String, Object> response = new LinkedHashMap<>();
        List<StudentAttendanceDailyDto> studentAttendanceDaily = this.studentAttendanceService.getStudentAttendanceDaily(classId, date);
        Map<String, Object> classDetail = this.classService.findClassDetail(classId, date.substring(0, 7));
        response.put("code", HttpStatus.OK.value());
        response.putAll(classDetail);
        response.put("studentAttendanceDaily", studentAttendanceDaily);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/save-student-attendance-daily")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> saveStudentAttendanceDaily(@RequestBody StudentAttendanceDailyPostDto studentAttendanceDailyPostDto) {
        this.validateRoleTeacher.validateRoleTeacher(
                studentAttendanceDailyPostDto.getAttendance().getClassId(),
                studentAttendanceDailyPostDto.getAttendance().getAttendanceDate().substring(0, 7)
        );
        this.studentAttendanceService.saveStudentAttendanceDaily(studentAttendanceDailyPostDto);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "Save student attendance successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PostMapping("/save_student_class")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> saveStudentClass(
            @RequestBody StudentClassDto studentClassDto
            ){
        int classId = studentClassDto.getClassId();
        String userName = studentClassDto.getUserName();
        this.studentAttendanceService.saveStudentClass(classId, userName);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "Save student attendance successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
