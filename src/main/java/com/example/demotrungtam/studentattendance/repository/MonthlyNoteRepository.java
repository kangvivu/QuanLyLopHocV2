package com.example.demotrungtam.studentattendance.repository;

import com.example.demotrungtam.entity.MonthlyNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyNoteRepository extends JpaRepository<MonthlyNote, Integer> {
}
