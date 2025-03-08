package com.example.demotrungtam.login.repository;

import com.example.demotrungtam.login.dto.UserDto;
import com.example.demotrungtam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT new com.example.demotrungtam.login.dto.UserDto(u.username, u.password, r.roleName) " +
            "FROM User u " +
            "LEFT JOIN u.role r " +
            "WHERE u.username = :username")
    UserDto findByUsername(String username);

    @Query(value = "SELECT new com.example.demotrungtam.login.dto.UserDto(s.studentId, s.studentName) " +
            "FROM Student s " +
            "INNER JOIN s.user u " +
            "WHERE u.username = :username")
    UserDto findStudent(@Param("username") String username);

    @Query(value = "SELECT new com.example.demotrungtam.login.dto.UserDto(t.teacherId, t.teacherName) " +
            "FROM Teacher t " +
            "INNER JOIN t.user u " +
            "WHERE u.username = :username")
    UserDto findTeacher(@Param("username") String username);
}
