package com.example.demotrungtam.classlist.repository;

import com.example.demotrungtam.classlist.dto.classEditDto.TeacherClassDetailDto;
import com.example.demotrungtam.entity.ClassDetail;
import com.example.demotrungtam.entity.TeacherClass;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherClassListRepository extends JpaRepository<TeacherClass, Integer> {

    @Query("SELECT new com.example.demotrungtam.classlist.dto.classEditDto.TeacherClassDetailDto( " +
            "tc.teacherClassId, " +
            "tc.teacher.teacherId, " +
            "tc.joinDate, " +
            "tc.leaveDate) " +
            "FROM TeacherClass tc " +
            "WHERE tc.classInfo.classId = :classId " +
            "AND (tc.classInfo.classId, tc.joinDate) IN ( " +
                "SELECT tc1.classInfo.classId, MAX(tc1.joinDate) as joinDate " +
                "FROM TeacherClass tc1 " +
                "GROUP BY tc1.classInfo.classId)")
    TeacherClassDetailDto getTeacherClassDetail(
            @Param("classId") Integer classId
    );

    @Query("SELECT tc " +
            "FROM TeacherClass tc " +
            "WHERE tc.classInfo.classId = :classId " +
            "ORDER BY tc.joinDate DESC")
    List<TeacherClass> getOldTeacherClass(
            @Param("classId") Integer classId,
            Pageable pageable
    );

    @Query("SELECT CASE WHEN COUNT(tc) > 0 THEN true ELSE false END " +
            "FROM TeacherClass tc " +
            "WHERE tc.classInfo.classId = :classId " +
            "AND tc.teacherClassId = :teacherClassId")
    boolean existTeacherClass(
            Integer classId,
            Integer teacherClassId
    );
}
