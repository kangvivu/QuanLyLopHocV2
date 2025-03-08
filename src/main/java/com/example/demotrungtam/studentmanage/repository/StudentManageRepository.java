package com.example.demotrungtam.studentmanage.repository;

import com.example.demotrungtam.entity.Student;
import com.example.demotrungtam.studentmanage.dto.StudentManageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentManageRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT new com.example.demotrungtam.studentmanage.dto.StudentManageDto( " +
            "s.studentId, " +
            "s.studentName, " +
            "s.dob, " +
            "u.username, " +
            "cd.className) " +
            "FROM Student s " +
            "INNER JOIN s.user u " +
            "LEFT JOIN ( " +
                "SELECT s1.studentId as studentId, MAX(sc.joinDate) as joinDate, MAX(sc.classInfo.classId) as classId " +
                "FROM Student s1 " +
                "LEFT JOIN s1.studentClasses sc " +
                    "ON (sc.student.studentId, sc.joinDate) IN ( " +
                        "SELECT sc1.student.studentId, MAX(sc1.joinDate) as joinDate " +
                        "FROM StudentClass sc1 " +
                        "GROUP BY sc1.student.studentId ) " +
                "GROUP BY s1.studentId " +
            ") sc2 " +
                "ON sc2.studentId = s.studentId " +
            "LEFT JOIN ClassInfo c " +
                "ON sc2.classId = c.classId " +
            "LEFT JOIN c.classDetails cd " +
                "ON (cd.classInfo.classId, cd.startDate) IN ( " +
                    "SELECT cd1.classInfo.classId, MAX(cd1.startDate) as startDate " +
                    "FROM ClassDetail cd1 " +
                    "GROUP BY cd1.classInfo.classId ) " +
            "WHERE s.studentStatus = 1 " +
            "AND (:studentName IS NULL OR LOWER(s.studentName) LIKE CONCAT('%', LOWER(:studentName), '%')) " +
            "ORDER BY FUNCTION('SUBSTRING_INDEX', s.studentName, ' ', -1), " +
            "FUNCTION('SUBSTRING_INDEX', s.studentName, ' ', 1), " +
            "s.studentName")
    List<StudentManageDto> getStudentManage(
            @Param("studentName") String studentName,
            Pageable pageable
    );

    @Query("SELECT COUNT(s.studentId) as total " +
            "FROM Student s " +
            "WHERE s.studentStatus = 1 " +
            "AND (:studentName IS NULL OR LOWER(s.studentName) LIKE CONCAT('%', LOWER(:studentName), '%'))")
    int countStudentManage(
            @Param("studentName") String studentName
    );
}
