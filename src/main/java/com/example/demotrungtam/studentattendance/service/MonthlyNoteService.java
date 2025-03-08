package com.example.demotrungtam.studentattendance.service;

import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.entity.ClassInfo;
import com.example.demotrungtam.entity.MonthlyNote;
import com.example.demotrungtam.entity.Student;
import com.example.demotrungtam.exception.CustomNotFoundException;
import com.example.demotrungtam.studentattendance.dto.MonthlyNoteDto;
import com.example.demotrungtam.studentattendance.repository.ClassRepository;
import com.example.demotrungtam.studentattendance.repository.MonthlyNoteRepository;
import com.example.demotrungtam.studentattendance.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonthlyNoteService {
    private final MonthlyNoteRepository monthlyNoteRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;

    @Autowired
    public MonthlyNoteService(
            MonthlyNoteRepository monthlyNoteRepository,
            StudentRepository studentRepository,
            ClassRepository classRepository
    ) {
        this.monthlyNoteRepository = monthlyNoteRepository;
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
    }

    @Transactional
    public void saveMonthlyNote(List<MonthlyNoteDto> listMonthlyNoteDto) {
        for (MonthlyNoteDto monthlyNoteDto : listMonthlyNoteDto) {
            MonthlyNote monthlyNote = new MonthlyNote();
            monthlyNote.setMonthlyNoteId(monthlyNoteDto.getMonthlyNoteId());
            monthlyNote.setNoteMonth(monthlyNoteDto.getNoteMonth());
            monthlyNote.setMonthComment(monthlyNoteDto.getMonthComment());
            Optional<Student> student = studentRepository.findById(monthlyNoteDto.getStudentId());
            Optional<ClassInfo> classInfo = classRepository.findById(monthlyNoteDto.getClassId());
            if (student.isPresent() && classInfo.isPresent()) {
                monthlyNote.setStudent(student.get());
                monthlyNote.setClassInfo(classInfo.get());
            } else {
                throw new CustomNotFoundException("Student or Class not found", Constant.ER005);
            }
            monthlyNoteRepository.save(monthlyNote);
        }
    }
}
