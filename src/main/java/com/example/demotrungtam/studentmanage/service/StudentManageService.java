package com.example.demotrungtam.studentmanage.service;

import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.entity.Role;
import com.example.demotrungtam.entity.Student;
import com.example.demotrungtam.entity.Telephone;
import com.example.demotrungtam.studentmanage.dto.StudentManageDto;
import com.example.demotrungtam.studentmanage.dto.StudentRegistrationDto;
import com.example.demotrungtam.studentmanage.repository.StudentManageRepository;
import com.example.demotrungtam.studentmanage.repository.TelephoneManageRepository;
import com.example.demotrungtam.studentmanage.repository.UserManageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demotrungtam.entity.User;

import java.text.Normalizer;
import java.util.List;

@Service
public class StudentManageService {
    private final StudentManageRepository studentManageRepository;
    private final TelephoneManageRepository telephoneManageRepository;
    private final UserManageRepository userManageRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public StudentManageService(
            StudentManageRepository studentManageRepository,
            TelephoneManageRepository telephoneManageRepository,
            UserManageRepository userManageRepository,
            BCryptPasswordEncoder passwordEncode
    ) {
        this.studentManageRepository = studentManageRepository;
        this.telephoneManageRepository = telephoneManageRepository;
        this.userManageRepository = userManageRepository;
        this.passwordEncoder = passwordEncode;
    }

    public List<StudentManageDto> getStudentManage(
            String studentName,
            int pageNumber,
            int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<StudentManageDto> students = studentManageRepository.getStudentManage(studentName, pageable);
        for(StudentManageDto student : students) {
            List<String> phones = telephoneManageRepository.getPhoneByStudentId(student.getStudentId());
            student.setPhones(phones);
        }
        return students;
    }

    public int countStudentManage(String studentName) {
        return studentManageRepository.countStudentManage(studentName);
    }

    public int saveStudent(StudentRegistrationDto studentRegistrationDto) {
        // add user
        User user = addUser(studentRegistrationDto.getStudentName());
        // add student
        Student student = addStudent(user, studentRegistrationDto);
        // add telephone
        addTelephone(student, studentRegistrationDto.getPhones());
        return student.getStudentId();
    }

    private User addUser(String studentName) {
        // clear Vietnamese characters
        String normalized = Normalizer.normalize(studentName, Normalizer.Form.NFD)
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
        user.setRole(new Role(3, Constant.ROLE_STUDENT));
        userManageRepository.save(user);
        return user;
    }

    private Student addStudent(User user,StudentRegistrationDto studentRegistrationDto) {
        // add student
        Student student = new Student();
        student.setStudentId(0);
        student.setStudentName(studentRegistrationDto.getStudentName());
        student.setDob(studentRegistrationDto.getDateOfBirth());
        student.setStudentAddress(studentRegistrationDto.getStudentAddress());
        student.setStudentStatus(1);
        student.setUser(user);
        studentManageRepository.save(student);
        return student;
    }

    private void addTelephone(Student student, List<String> phones) {
        for(String phone : phones) {
            Telephone telephone = new Telephone();
            telephone.setTelephoneId(0);
            telephone.setStudent(student);
            telephone.setPhone(phone);
            telephoneManageRepository.save(telephone);
        }
    }
}