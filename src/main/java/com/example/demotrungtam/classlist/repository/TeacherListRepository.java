package com.example.demotrungtam.classlist.repository;

import com.example.demotrungtam.classlist.dto.TeacherDto;
import com.example.demotrungtam.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeacherListRepository extends JpaRepository<Teacher, Integer> {
    @Query("SELECT new com.example.demotrungtam.classlist.dto.TeacherDto(" +
                "t.teacherId, " +
                "t.teacherName) " +
            "FROM Teacher t")
    List<TeacherDto> findAllTeacher();
}
