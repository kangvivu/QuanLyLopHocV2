package com.example.demotrungtam.teachermanage.service;

import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.entity.Role;

import com.example.demotrungtam.entity.Teacher;
import com.example.demotrungtam.entity.User;

import com.example.demotrungtam.studentmanage.repository.UserManageRepository;
import com.example.demotrungtam.teachermanage.dto.TeacherManageDto;

import com.example.demotrungtam.teachermanage.dto.TeacherRegistrationDto;
import com.example.demotrungtam.teachermanage.repository.TeacherManageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class TeacherManageService {
    private final TeacherManageRepository teacherManageRepository;
    private final UserManageRepository userManageRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public TeacherManageService(TeacherManageRepository teacherManageRepository, UserManageRepository userManageRepository, BCryptPasswordEncoder passwordEncoder){
        this.teacherManageRepository = teacherManageRepository;
        this.userManageRepository = userManageRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<TeacherManageDto> getTeacherManage(String teacherName, int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<TeacherManageDto> teachers = teacherManageRepository.getTeacherManage(teacherName, pageable);
        return teachers;
    }

    public int countTeacherManage() {
        return teacherManageRepository.countTeacherManage();
//        return 3;
    }

    public int saveTeacher(TeacherRegistrationDto teacherRegistrationDto){
        // add user
        User user = addUser(teacherRegistrationDto.getTeacherName());

        Teacher teacher = addTeacher(user, teacherRegistrationDto);

        return  teacher.getTeacherId();

    }

    private User addUser(String teacherName) {
        // clear Vietnamese characters
        String normalized = Normalizer.normalize(teacherName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        // convert to lowercase
        String lowercase = normalized.toLowerCase();
        // remove spaces
        String result = lowercase.replaceAll("\\s+", "");

        // get unique username
        int count = 0;
        String finalUsername = result;
        // check if username exists
        while(userManageRepository.existsByUsername(finalUsername)) {
            if (count > 0) {
                finalUsername = result + count;
            }
            count++;
        }

        User user = new User();
        user.setUserId(0);
        user.setUsername(finalUsername);

        // set password with bcrypt
        String password = passwordEncoder.encode("123456");
        user.setPassword(password);
        user.setRole(new Role(2, Constant.ROLE_TEACHER));
        userManageRepository.save(user);
        return user;
    }

    private Teacher addTeacher(User user, TeacherRegistrationDto teacherRegistrationDto) {
        // add teacher
        Teacher teacher = new Teacher();
        teacher.setTeacherId(0);
        teacher.setTeacherName(teacherRegistrationDto.getTeacherName());
        teacher.setTeacherAddress(teacherRegistrationDto.getTeacherAddress());
        teacher.setTeacherPhone(teacherRegistrationDto.getTeacherPhone());
        teacher.setTeacherStatus(1);
        teacher.setUser(user);
        teacherManageRepository.save(teacher);
        return teacher;
    }

    public int deleteTeacher(String userName){
        // 1. Tìm userID từ userName
        User user = userManageRepository.findByUsername(userName);
        if (user == null) {
            throw new RuntimeException("User không tồn tại!");
        } else {
//            int userId = user.getUserId();
//            teacherManageRepository.deleteByTeacherName(userName);
//            userManageRepository.deleteById(userId);
            teacherManageRepository.updateTeacherStatusToZeroByTeacherName(userName);
            return user.getUserId();
        }

    }

}
