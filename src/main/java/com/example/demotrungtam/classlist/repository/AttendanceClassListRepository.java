package com.example.demotrungtam.classlist.repository;

import com.example.demotrungtam.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceClassListRepository extends JpaRepository<Attendance, Integer> {
    @Query("SELECT MAX(a1.attendanceDate) as attendanceDate " +
            "FROM Attendance a1 " +
            "WHERE a1.classInfo.classId = :classId " +
            "GROUP BY a1.classInfo.classId")
    String findLatestAttendanceDate(
            @Param("classId") Integer classId
    );
}
