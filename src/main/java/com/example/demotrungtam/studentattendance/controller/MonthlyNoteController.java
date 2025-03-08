package com.example.demotrungtam.studentattendance.controller;

import com.example.demotrungtam.studentattendance.dto.MonthlyNoteDto;
import com.example.demotrungtam.studentattendance.service.MonthlyNoteService;
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
public class MonthlyNoteController {
    private final MonthlyNoteService monthlyNoteService;
    private final ValidateRoleTeacher validateRoleTeacher;

    @Autowired
    public MonthlyNoteController(
            MonthlyNoteService monthlyNoteService,
            ValidateRoleTeacher validateRoleTeacher
    ) {
        this.monthlyNoteService = monthlyNoteService;
        this.validateRoleTeacher = validateRoleTeacher;
    }

    @PostMapping("/save-monthly-note")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<Object> changeMonthlyNote(@RequestBody List<MonthlyNoteDto> listMonthlyNoteDto) {
        validateRoleTeacher.validateRoleTeacher(
                listMonthlyNoteDto.get(0).getClassId(),
                listMonthlyNoteDto.get(0).getNoteMonth().substring(0, 7)
        );

        monthlyNoteService.saveMonthlyNote(listMonthlyNoteDto);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("message", "Save monthly note success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
