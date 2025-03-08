package com.example.demotrungtam.studentattendance.service;

import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.exception.CustomException;
import com.example.demotrungtam.login.dto.UserDto;
import com.example.demotrungtam.login.repository.UserRepository;
import com.example.demotrungtam.studentattendance.repository.TeacherClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ValidateRoleTeacher {
    private final UserRepository userRepository;
    private final TeacherClassRepository teacherClassRepository;

    @Autowired
    public ValidateRoleTeacher(
            UserRepository userRepository,
            TeacherClassRepository teacherClassRepository) {
        this.userRepository = userRepository;
        this.teacherClassRepository = teacherClassRepository;
    }

    /**
     * Validate role teacher
     *
     * @param classId
     * @param date
     */
    public void validateRoleTeacher(int classId, String date) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String role = securityContext.getAuthentication().getAuthorities().toString();
        if (role.contains(Constant.ROLE_TEACHER)) {
            String teacherName = securityContext.getAuthentication().getPrincipal().toString();
            UserDto userDto = userRepository.findTeacher(teacherName);
            boolean isExitsTeacher = teacherClassRepository.existsTeacherClass(userDto.getId(), classId, date);
            if (!isExitsTeacher) {
                throw new CustomException(Constant.ACCESS_DENIED, Constant.ER004);
            }
        }
    }
}
