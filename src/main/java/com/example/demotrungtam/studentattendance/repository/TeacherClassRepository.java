package com.example.demotrungtam.studentattendance.repository;

import com.example.demotrungtam.entity.TeacherClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherClassRepository extends JpaRepository<TeacherClass, Integer> {
    @Query("SELECT CASE WHEN COUNT(tc.teacherClassId) > 0 THEN true ELSE false END " +
            "FROM TeacherClass tc " +
            "WHERE tc.classInfo.classId = :classId " +
            "AND tc.teacher.teacherId = :teacherId " +
            "AND FUNCTION('DATE_FORMAT', tc.joinDate, '%Y-%m') <= :date " +
            "AND (tc.leaveDate IS NULL OR FUNCTION('DATE_FORMAT', tc.leaveDate, '%Y-%m') >= :date)")
    boolean existsTeacherClass(
            @Param("teacherId") Integer teacherId,
            @Param("classId") Integer classId,
            @Param("date") String date
    );
}
