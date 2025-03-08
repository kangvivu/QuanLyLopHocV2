package com.example.demotrungtam.studentattendance.repository;

import com.example.demotrungtam.entity.ClassInfo;
import com.example.demotrungtam.studentattendance.dto.ClassDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassInfo, Integer> {
    @Query("SELECT new com.example.demotrungtam.studentattendance.dto.ClassDto(" +
            "cd.classInfo.classId, " +
            "cd.className, " +
            "cd.schedule, " +
            "cd.startDate, " +
            "cd.endDate) " +
            "FROM ClassDetail cd " +
            "WHERE cd.classInfo.classId = :classId " +
            "AND FUNCTION('DATE_FORMAT', cd.startDate, '%Y-%m') <= :date " +
            "AND (cd.endDate IS NULL OR FUNCTION('DATE_FORMAT', cd.endDate, '%Y-%m') >= :date) " +
            "ORDER BY cd.startDate ASC")
    List<ClassDto> findClassDetail(
            @Param("classId") Integer classId,
            @Param("date") String date
    );

    ClassInfo findByClassId(Integer classId);
}
