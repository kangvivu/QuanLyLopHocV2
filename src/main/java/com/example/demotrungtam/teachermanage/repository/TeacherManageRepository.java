package com.example.demotrungtam.teachermanage.repository;


import com.example.demotrungtam.entity.Teacher;
import com.example.demotrungtam.teachermanage.dto.TeacherManageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherManageRepository extends JpaRepository<Teacher, Integer> {
    @Query("SELECT new com.example.demotrungtam.teachermanage.dto.TeacherManageDto( " +
            "t.teacherId, t.teacherName, t.teacherAddress, t.teacherPhone, t.teacherStatus, " +
            "u.username ) " +
            "FROM Teacher t " +
            "INNER JOIN t.user u " +
            "WHERE (:teacherName IS NULL OR LOWER(t.teacherName) LIKE CONCAT('%', LOWER(:teacherName), '%'))")
    List<TeacherManageDto> getTeacherManage(
            @Param("teacherName") String teacherName,
            Pageable pageable
    );


    @Query("SELECT COUNT(t.teacherId) FROM Teacher t WHERE t.teacherStatus = 1")
    int countTeacherManage();

    void deleteByTeacherName(String teacherName);

    // xóa teacher: set role = 0
    @Query("UPDATE Teacher t SET t.teacherStatus = 0 WHERE t.teacherName = :teacherName")
    void updateTeacherStatusToZeroByTeacherName(@Param("teacherName") String teacherName);

}
