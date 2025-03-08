package com.example.demotrungtam.classlist.service;

import com.example.demotrungtam.classlist.dto.TeacherDto;
import com.example.demotrungtam.classlist.repository.TeacherListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherListService {
    private TeacherListRepository teacherListRepository;

    @Autowired
    public TeacherListService(TeacherListRepository teacherListRepository) {
        this.teacherListRepository = teacherListRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TeacherDto> getAllTeacher() {
        return teacherListRepository.findAllTeacher();
    }
}
