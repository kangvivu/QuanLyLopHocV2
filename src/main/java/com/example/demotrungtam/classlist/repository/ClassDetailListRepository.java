package com.example.demotrungtam.classlist.repository;

import com.example.demotrungtam.classlist.dto.classEditDto.ClassDetailDto;
import com.example.demotrungtam.entity.ClassDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassDetailListRepository extends JpaRepository<ClassDetail, Integer> {
    @Query("SELECT new com.example.demotrungtam.classlist.dto.classEditDto.ClassDetailDto(" +
            "cd.classDetailId, " +
            "cd.classInfo.classId, " +
            "cd.className, " +
            "cd.addressInfo.addressId, " +
            "cd.startDate, " +
            "cd.endDate, " +
            "cd.schedule, " +
            "cd.tuitionSession, " +
            "cd.studyTime, " +
            "a.attendanceDate) " +
            "FROM ClassDetail cd " +
            "INNER JOIN cd.classInfo c " +
                "ON c.classId = :classId " +
            "LEFT JOIN c.attendances a " +
                "ON (a.classInfo.classId, a.attendanceDate) IN ( " +
                    "SELECT a1.classInfo.classId, MAX(a1.attendanceDate) as attendanceDate " +
                    "FROM Attendance a1 " +
                    "GROUP BY a1.classInfo.classId) " +
            "WHERE (cd.classInfo.classId, cd.startDate) IN ( " +
                "SELECT cd1.classInfo.classId, MAX(cd1.startDate) as startDate " +
                "FROM ClassDetail cd1 " +
                "GROUP BY cd1.classInfo.classId)")
    ClassDetailDto getClassDetail(@Param("classId") Integer classId);

    @Query("SELECT cd " +
            "FROM ClassDetail cd " +
            "WHERE cd.classInfo.classId = :classId " +
            "ORDER BY cd.startDate DESC")
    List<ClassDetail> getOldClassDetail(
            @Param("classId") Integer classId,
            Pageable pageable
    );

    @Query("SELECT CASE WHEN COUNT(cd) > 0 THEN true ELSE false END " +
            "FROM ClassDetail cd " +
            "WHERE cd.classInfo.classId = :classId " +
            "AND cd.classDetailId =  :classDetailId")
    boolean existClassDetail(
            Integer classId,
            Integer classDetailId
    );
}