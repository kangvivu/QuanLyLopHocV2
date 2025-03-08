package com.example.demotrungtam.studentattendance.repository;

import com.example.demotrungtam.entity.Attendance;
import com.example.demotrungtam.studentattendance.dto.AttendanceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    @Query("SELECT new com.example.demotrungtam.studentattendance.dto.AttendanceDto( " +
            "a.attendanceId, " +
            "a.attendanceDate) " +
            "FROM Attendance a " +
            "WHERE FUNCTION('DATE_FORMAT', a.attendanceDate, '%Y-%m') = :date " +
            "AND a.classInfo.classId = :classId")
    List<AttendanceDto> findAttendanceByDateAndClassId(int classId, String date);

    @Query("SELECT CASE WHEN COUNT(a.attendanceId) > 0 THEN true ELSE false END " +
            "FROM Attendance a " +
            "WHERE a.classInfo.classId = :classId " +
            "AND a.attendanceDate = :date")
    boolean existsAttendanceByClassIdAndAttendanceDate(
            @Param("classId")Integer classId,
            @Param("date")String date
    );
}
