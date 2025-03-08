package com.example.demotrungtam.classlist.service;

import com.example.demotrungtam.classlist.dto.classUpdateDto.TeacherClassUpdateDto;
import com.example.demotrungtam.classlist.repository.AttendanceClassListRepository;
import com.example.demotrungtam.classlist.repository.ClassListRepository;
import com.example.demotrungtam.classlist.repository.TeacherClassListRepository;
import com.example.demotrungtam.classlist.repository.TeacherListRepository;
import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.entity.ClassInfo;
import com.example.demotrungtam.entity.Teacher;
import com.example.demotrungtam.entity.TeacherClass;
import com.example.demotrungtam.exception.CustomException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TeacherClassClassListService {
    private final TeacherClassListRepository teacherClassListRepository;
    private final AttendanceClassListRepository attendanceRepository;
    private final TeacherListRepository teacherListRepository;
    private final ClassListRepository classListRepository;

    @Autowired
    public TeacherClassClassListService(
            TeacherClassListRepository teacherClassListRepository,
            AttendanceClassListRepository attendanceRepository,
            TeacherListRepository teacherListRepository,
            ClassListRepository classListRepository
    ) {
        this.teacherClassListRepository = teacherClassListRepository;
        this.attendanceRepository = attendanceRepository;
        this.teacherListRepository = teacherListRepository;
        this.classListRepository = classListRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void saveTeacherClass(TeacherClassUpdateDto teacherClassUpdateDto) {
        // check valid data
        boolean isValidData = teacherClassListRepository.existTeacherClass(
                teacherClassUpdateDto.getClassId(),
                teacherClassUpdateDto.getTeacherClassId()
        );
        if (!isValidData) {
            throw new CustomException(Constant.INVALID_DATA, Constant.ER007);
        }

        TeacherClass teacherClass = teacherClassListRepository.findById(teacherClassUpdateDto.getTeacherClassId())
                .orElseThrow(() -> new CustomException(Constant.INVALID_DATA, Constant.ER007));
        String latestAttendanceDate = attendanceRepository.findLatestAttendanceDate(teacherClassUpdateDto.getClassId());

        // kiem tra data co phai moi nhat khong
        if (latestAttendanceDate != null && !latestAttendanceDate.equals(teacherClassUpdateDto.getLatestAttendanceDate())) {
            throw new CustomException(Constant.OLD_DATA, Constant.ER006);
        }

        // neu ngay diem danh cuoi la null
        if (latestAttendanceDate == null) {
            Teacher teacher = teacherListRepository.findById(teacherClassUpdateDto.getTeacherId())
                    .orElseThrow(() -> new CustomException(Constant.INVALID_DATA, Constant.ER007));
            teacherClass.setTeacher(teacher);
            teacherClassListRepository.save(teacherClass);
            return;
        }

        LocalDate applyDate = LocalDate.parse(teacherClassUpdateDto.getApplyDate());
        LocalDate latestAttendance = LocalDate.parse(latestAttendanceDate);
        LocalDate joinDate = LocalDate.parse(teacherClass.getJoinDate());

        // validate applyDate
        if (!applyDate.isAfter(latestAttendance)) {
            throw new CustomException(Constant.INVALID_DATA, Constant.ER007);
        }

        // neu joinDate > latestAttendanceDate
        if (joinDate.isAfter(latestAttendance)) {
            // update leaveDate cua teacherClass cu
            Pageable pageable = PageRequest.of(1, 1);
            List<TeacherClass> teacherClasses = teacherClassListRepository.getOldTeacherClass(teacherClassUpdateDto.getClassId(), pageable);
            TeacherClass oldTeacherClass = teacherClasses.get(0);
            oldTeacherClass.setLeaveDate(applyDate.minusDays(1).toString());
            teacherClassListRepository.save(oldTeacherClass);

            // update thong tin cho teacherClass moi
            Teacher teacher = teacherListRepository.findById(teacherClassUpdateDto.getTeacherId())
                    .orElseThrow(() -> new CustomException(Constant.INVALID_DATA, Constant.ER007));
            teacherClass.setTeacher(teacher);
            teacherClass.setJoinDate(applyDate.toString());
            teacherClassListRepository.save(teacherClass);
        } else {
            // neu joinDate <= latestAttendanceDate
            // update leaveDate cua teacherClass cu
            teacherClass.setLeaveDate(applyDate.minusDays(1).toString());
            teacherClassListRepository.save(teacherClass);

            // Tao teacherClass moi
            TeacherClass newTeacherClass = new TeacherClass();
            Teacher teacher = teacherListRepository.findById(teacherClassUpdateDto.getTeacherId())
                    .orElseThrow(() -> new CustomException(Constant.INVALID_DATA, Constant.ER007));
            newTeacherClass.setTeacher(teacher);
            ClassInfo classInfo = classListRepository.findById(teacherClassUpdateDto.getClassId())
                    .orElseThrow(() -> new CustomException(Constant.INVALID_DATA, Constant.ER007));
            newTeacherClass.setClassInfo(classInfo);
            newTeacherClass.setJoinDate(applyDate.toString());
            newTeacherClass.setLeaveDate(null);
            teacherClassListRepository.save(newTeacherClass);
        }
    }
}
