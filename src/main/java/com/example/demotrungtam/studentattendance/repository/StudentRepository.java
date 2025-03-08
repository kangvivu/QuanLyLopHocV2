package com.example.demotrungtam.studentattendance.repository;

import com.example.demotrungtam.entity.Student;
import com.example.demotrungtam.entity.User;
import com.example.demotrungtam.studentattendance.dto.StudentAttendanceDailyDto;
import com.example.demotrungtam.studentattendance.dto.StudentAttendanceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT new com.example.demotrungtam.studentattendance.dto.StudentAttendanceDto(" +
            "s.studentId, " +
            "s.studentName, " +
            "mn.monthlyNoteId, " +
            "mn.monthComment, " +
            "a.attendanceDate, " +
            "sa.isAttend, " +
            "sa.additionalAttendance) " +
            "FROM Student s " +
            "INNER JOIN s.studentClasses sc " +
            "ON (FUNCTION('DATE_FORMAT', sc.joinDate, '%Y-%m') <= :date " +
            "AND (sc.leaveDate IS NULL OR FUNCTION('DATE_FORMAT', sc.leaveDate, '%Y-%m') >= :date)) " +
            "LEFT JOIN s.monthlyNotes mn " +
            "ON (sc.classInfo.classId = mn.classInfo.classId " +
            "AND FUNCTION('DATE_FORMAT', mn.noteMonth, '%Y-%m') = :date) " +
            "LEFT JOIN Attendance a " +
            "ON sc.classInfo.classId = a.classInfo.classId " +
            "AND FUNCTION('DATE_FORMAT', a.attendanceDate, '%Y-%m') = :date " +
            "LEFT JOIN a.studentAttendances sa " +
            "ON s.studentId = sa.student.studentId " +
            "WHERE sc.classInfo.classId = :classId " +
            "ORDER BY FUNCTION('SUBSTRING_INDEX', s.studentName, ' ', -1), " +
            "FUNCTION('SUBSTRING_INDEX', s.studentName, ' ', 1), " +
            "s.studentName")
    List<StudentAttendanceDto> getStudentAttendance(
            @Param("classId")Integer classId,
            @Param("date") String date
    );

    @Query("SELECT COUNT(s.studentId) as total " +
            "FROM Student s " +
            "INNER JOIN s.studentClasses sc " +
            "ON (FUNCTION('DATE_FORMAT', sc.joinDate, '%Y-%m') <= :date " +
            "AND (sc.leaveDate IS NULL OR FUNCTION('DATE_FORMAT', sc.leaveDate, '%Y-%m') >= :date)) " +
            "WHERE sc.classInfo.classId = :classId")
    int countStudentAttendance(
            @Param("classId")Integer classId,
            @Param("date") String date
    );

    @Query("SELECT new com.example.demotrungtam.studentattendance.dto.StudentAttendanceDailyDto(" +
            "s.studentId, " +
            "s.studentName, " +
            "a.attendanceId, " +
            "a.attendanceDate, " +
            "sa.studentAttendanceId, " +
            "sa.isAttend, " +
            "sa.score, " +
            "sa.doHomework, " +
            "sa.sessionComment, " +
            "sa.additionalAttendance) " +
            "FROM Student s " +
            "INNER JOIN s.studentClasses sc " +
            "ON sc.joinDate <= :date " +
            "AND (sc.leaveDate IS NULL OR sc.leaveDate >= :date) " +
            "LEFT JOIN Attendance a " +
            "ON sc.classInfo.classId = a.classInfo.classId " +
            "AND a.attendanceDate = :date " +
            "LEFT JOIN a.studentAttendances sa " +
            "ON s.studentId = sa.student.studentId " +
            "WHERE sc.classInfo.classId = :classId " +
            "ORDER BY FUNCTION('SUBSTRING_INDEX', s.studentName, ' ', -1), " +
            "FUNCTION('SUBSTRING_INDEX', s.studentName, ' ', 1), " +
            "s.studentName")
    List<StudentAttendanceDailyDto> getStudentAttendanceDaily(
            @Param("classId")Integer classId,
            @Param("date") String date
    );

    Student findByUser(User user);

}
