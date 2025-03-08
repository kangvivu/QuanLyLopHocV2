package com.example.demotrungtam.studentattendance.repository;

import com.example.demotrungtam.entity.Student;
import com.example.demotrungtam.entity.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Integer> {
}
