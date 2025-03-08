package com.example.demotrungtam.classlist.service;

import com.example.demotrungtam.classlist.dto.ClassDto;
import com.example.demotrungtam.classlist.dto.classEditDto.ClassDetailDto;
import com.example.demotrungtam.classlist.dto.classEditDto.TeacherClassDetailDto;
import com.example.demotrungtam.classlist.dto.class_registration_dto.ClassRegistrationDto;
import com.example.demotrungtam.classlist.repository.*;
import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.entity.*;
import com.example.demotrungtam.exception.CustomException;
import com.example.demotrungtam.login.dto.UserDto;
import com.example.demotrungtam.login.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassListService {
    private final ClassListRepository classListRepository;
    private final ClassDetailListRepository classDetailRepository;
    private final AddressListRepository addressRepository;
    private final TeacherListRepository teacherRepository;
    private final TeacherClassListRepository teacherClassRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClassListService(
            ClassListRepository classListRepository,
            UserRepository userRepository,
            ClassDetailListRepository classDetailRepository,
            AddressListRepository addressRepository,
            TeacherListRepository teacherRepository,
            TeacherClassListRepository teacherClassRepository
    ) {
        this.classListRepository = classListRepository;
        this.userRepository = userRepository;
        this.classDetailRepository = classDetailRepository;
        this.addressRepository = addressRepository;
        this.teacherRepository = teacherRepository;
        this.teacherClassRepository = teacherClassRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ClassDto> getAllClass(String date, Integer teacherId, Integer addressId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return classListRepository.findAllClass(date, teacherId, addressId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public int countAllClass(String date, Integer teacherId, Integer addressId) {
        return classListRepository.countAllClass(date, teacherId, addressId);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public List<ClassDto> getTeacherClass(String date, int teacherId, Integer addressId, int pageNumber, int pageSize) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String teacherName = securityContext.getAuthentication().getPrincipal().toString();
        UserDto userDto = userRepository.findTeacher(teacherName);
        if (userDto.getId() == teacherId) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            return classListRepository.findTeacherClass(date, teacherId, addressId, pageable);
        } else {
            throw new CustomException(Constant.ACCESS_DENIED, Constant.ER004);
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<ClassDto> getStudentClass(String date, int studentId, Integer addressId, int pageNumber, int pageSize) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String studentName = securityContext.getAuthentication().getPrincipal().toString();
        UserDto userDto = userRepository.findStudent(studentName);
        if (userDto.getId() == studentId) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            return classListRepository.findStudentClass(date, studentId, addressId, pageable);
        } else {
            throw new CustomException(Constant.ACCESS_DENIED, Constant.ER004);
        }
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public int countTeacherClass(String date, int teacherId, Integer addressId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String teacherName = securityContext.getAuthentication().getPrincipal().toString();
        UserDto userDto = userRepository.findTeacher(teacherName);
        if (userDto.getId() == teacherId) {
            return classListRepository.countTeacherClass(date, teacherId, addressId);
        } else {
            throw new CustomException(Constant.ACCESS_DENIED, Constant.ER004);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void registerClass(ClassRegistrationDto classRegistrationDto) {
        // Validate classRegistrationDto

        // Add new class
        ClassInfo classInfo = classListRepository.save(new ClassInfo());

        // Add new ClassDetail
        ClassDetail classDetail = new ClassDetail();
        classDetail.setClassDetailId(0);
        classDetail.setClassInfo(classInfo);
        classDetail.setClassName(classRegistrationDto.getClassName());
        Optional<AddressInfo> addressInfo = addressRepository.findById(classRegistrationDto.getAddressId());
        if (addressInfo.isEmpty()) {
            throw new CustomException(Constant.NOT_FOUND, Constant.ER005);
        }
        classDetail.setAddressInfo(addressInfo.get());
        classDetail.setStartDate(classRegistrationDto.getStartDate());
        classDetail.setEndDate(null);
        classDetail.setSchedule(classRegistrationDto.getSchedule());
        classDetail.setTuitionSession(classRegistrationDto.getTuitionSession());
        classDetail.setStudyTime(classRegistrationDto.getStudyTime());
        classDetailRepository.save(classDetail);

        // Add new TeacherClass
        if(classRegistrationDto.getTeacherId() != null) {
            TeacherClass teacherClass = new TeacherClass();
            teacherClass.setTeacherClassId(0);
            teacherClass.setClassInfo(classInfo);
            teacherClass.setJoinDate(classRegistrationDto.getStartDate());
            teacherClass.setLeaveDate(null);
            Optional<Teacher> teacher = teacherRepository.findById(classRegistrationDto.getTeacherId());
            if (teacher.isEmpty()) {
                throw new CustomException(Constant.NOT_FOUND, Constant.ER005);
            }
            teacherClass.setTeacher(teacher.get());
            teacherClassRepository.save(teacherClass);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ClassDetailDto getClassDetail(Integer classId) {
        try {
            ClassDetailDto classDetailDto = classDetailRepository.getClassDetail(classId);
            TeacherClassDetailDto teacherClassDetailDto = teacherClassRepository.getTeacherClassDetail(classId);
            classDetailDto.setTeacherClassDetail(teacherClassDetailDto);
            return classDetailDto;
        } catch (Exception e) {
            throw new CustomException(Constant.NOT_FOUND, Constant.ER005);
        }
    }
}
