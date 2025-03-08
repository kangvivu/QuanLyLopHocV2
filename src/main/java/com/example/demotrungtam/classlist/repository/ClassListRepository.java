package com.example.demotrungtam.classlist.repository;

import com.example.demotrungtam.classlist.dto.ClassDto;
import com.example.demotrungtam.entity.ClassInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClassListRepository extends JpaRepository<ClassInfo, Integer> {
    @Query("SELECT new com.example.demotrungtam.classlist.dto.ClassDto(" +
                "c.classId, " +
                "cd.classDetailId, " +
                "cd.className, " +
                "a.addressId, " +
                "a.addressName, " +
                "cd.schedule, " +
                "cd.studyTime, " +
                "t.teacherId, " +
                "t.teacherName, " +
                "cd.startDate, " +
                "cd.endDate) " +
            "FROM ClassInfo c " +
            "INNER JOIN c.classDetails cd ON " +
                "(cd.classInfo.classId, cd.startDate) IN ( " +
                    "SELECT cd1.classInfo.classId, MAX(cd1.startDate) as startDate " +
                    "FROM ClassDetail cd1 " +
                    "WHERE (:date IS NULL OR FUNCTION('DATE_FORMAT', cd1.startDate, '%Y-%m') <= :date) " +
                    "AND (:date IS NULL OR (cd1.endDate IS NULL OR FUNCTION('DATE_FORMAT', cd1.endDate, '%Y-%m') >= :date)) " +
                    "AND (:addressId IS NULL OR cd1.addressInfo.addressId = :addressId) " +
                    "GROUP BY cd1.classInfo.classId) " +
            "INNER JOIN cd.addressInfo a " +
            "LEFT JOIN c.teacherClasses tc ON " +
                "(tc.classInfo.classId, tc.joinDate) IN ( " +
                    "SELECT tc1.classInfo.classId, MAX(tc1.joinDate) as joinDate " +
                    "FROM TeacherClass tc1 " +
                    "WHERE (:date IS NULL OR FUNCTION('DATE_FORMAT', tc1.joinDate, '%Y-%m') <= :date) " +
                    "AND (:date IS NULL OR (tc1.leaveDate IS NULL OR FUNCTION('DATE_FORMAT', tc1.leaveDate, '%Y-%m') >= :date)) " +
                    "AND (:teacherId IS NULL OR tc1.teacher.teacherId = :teacherId)" +
                    "GROUP BY tc1.classInfo.classId) " +
            "LEFT JOIN tc.teacher t " +
            "WHERE (:teacherId IS NULL OR t.teacherId = :teacherId) " +
            "ORDER BY cd.className")
    List<ClassDto> findAllClass(
            @Param("date") String date,
            @Param("teacherId") Integer teacherId,
            @Param("addressId") Integer addressId,
            Pageable pageable
    );

    @Query("SELECT COUNT(c.classId) as total " +
            "FROM ClassInfo c " +
            "INNER JOIN c.classDetails cd ON " +
            "(cd.classInfo.classId, cd.startDate) IN ( " +
            "SELECT cd1.classInfo.classId, MAX(cd1.startDate) as startDate " +
            "FROM ClassDetail cd1 " +
            "WHERE (:date IS NULL OR FUNCTION('DATE_FORMAT', cd1.startDate, '%Y-%m') <= :date) " +
            "AND (:date IS NULL OR (cd1.endDate IS NULL OR FUNCTION('DATE_FORMAT', cd1.endDate, '%Y-%m') >= :date)) " +
            "AND (:addressId IS NULL OR cd1.addressInfo.addressId = :addressId) " +
            "GROUP BY cd1.classInfo.classId) " +
            "INNER JOIN cd.addressInfo a " +
            "LEFT JOIN c.teacherClasses tc ON " +
            "(tc.classInfo.classId, tc.joinDate) IN ( " +
            "SELECT tc1.classInfo.classId, MAX(tc1.joinDate) as joinDate " +
            "FROM TeacherClass tc1 " +
            "WHERE (:date IS NULL OR FUNCTION('DATE_FORMAT', tc1.joinDate, '%Y-%m') <= :date) " +
            "AND (:date IS NULL OR (tc1.leaveDate IS NULL OR FUNCTION('DATE_FORMAT', tc1.leaveDate, '%Y-%m') >= :date)) " +
            "AND (:teacherId IS NULL OR tc1.teacher.teacherId = :teacherId)" +
            "GROUP BY tc1.classInfo.classId) " +
            "LEFT JOIN tc.teacher t " +
            "WHERE (:teacherId IS NULL OR t.teacherId = :teacherId) " +
            "ORDER BY cd.className")
    int countAllClass(
            @Param("date") String date,
            @Param("teacherId") Integer teacherId,
            @Param("addressId") Integer addressId
    );

    @Query("SELECT new com.example.demotrungtam.classlist.dto.ClassDto(" +
                "c.classId, " +
                "cd.classDetailId, " +
                "cd.className, " +
                "a.addressId, " +
                "a.addressName, " +
                "cd.schedule, " +
                "cd.studyTime, " +
                "t.teacherId, " +
                "t.teacherName, " +
                "cd.startDate, " +
                "cd.endDate) " +
            "FROM ClassInfo c " +
            "INNER JOIN c.classDetails cd ON " +
                "(cd.classInfo.classId, cd.startDate) IN ( " +
                    "SELECT cd1.classInfo.classId, MAX(cd1.startDate) as startDate " +
                    "FROM ClassDetail cd1 " +
                    "WHERE (:date IS NULL OR FUNCTION('DATE_FORMAT', cd1.startDate, '%Y-%m') <= :date) " +
                    "AND (:date IS NULL OR (cd1.endDate IS NULL OR FUNCTION('DATE_FORMAT', cd1.endDate, '%Y-%m') >= :date)) " +
                    "AND (:addressId IS NULL OR cd1.addressInfo.addressId = :addressId ) " +
                    "GROUP BY cd1.classInfo.classId) " +
            "INNER JOIN cd.addressInfo a " +
            "INNER JOIN c.teacherClasses tc ON " +
                "(tc.classInfo.classId, tc.joinDate) IN ( " +
                    "SELECT tc1.classInfo.classId, MAX(tc1.joinDate) as joinDate " +
                    "FROM TeacherClass tc1 " +
                    "WHERE tc1.teacher.teacherId = :teacherId " +
                    "GROUP BY tc1.classInfo.classId) " +
            "INNER JOIN tc.teacher t " +
            "WHERE " +
                "FUNCTION('DATE_FORMAT', tc.joinDate, '%Y-%m') <= :date " +
                "AND (tc.leaveDate IS NULL OR FUNCTION('DATE_FORMAT', tc.leaveDate, '%Y-%m') >= :date) " +
                "AND t.teacherId = :teacherId " +
            "ORDER BY cd.className")
    List<ClassDto> findTeacherClass(
            @Param("date") String date,
            @Param("teacherId") int teacherId,
            @Param("addressId") Integer addressId,
            Pageable pageable
    );

    @Query("SELECT COUNT(c.classId) as total " +
            "FROM ClassInfo c " +
            "INNER JOIN c.classDetails cd ON " +
            "(cd.classInfo.classId, cd.startDate) IN ( " +
            "SELECT cd1.classInfo.classId, MAX(cd1.startDate) as startDate " +
            "FROM ClassDetail cd1 " +
            "WHERE (:date IS NULL OR FUNCTION('DATE_FORMAT', cd1.startDate, '%Y-%m') <= :date) " +
            "AND (:date IS NULL OR (cd1.endDate IS NULL OR FUNCTION('DATE_FORMAT', cd1.endDate, '%Y-%m') >= :date)) " +
            "AND (:addressId IS NULL OR cd1.addressInfo.addressId = :addressId ) " +
            "GROUP BY cd1.classInfo.classId) " +
            "INNER JOIN cd.addressInfo a " +
            "INNER JOIN c.teacherClasses tc ON " +
            "(tc.classInfo.classId, tc.joinDate) IN ( " +
            "SELECT tc1.classInfo.classId, MAX(tc1.joinDate) as joinDate " +
            "FROM TeacherClass tc1 " +
            "WHERE tc1.teacher.teacherId = :teacherId " +
            "GROUP BY tc1.classInfo.classId) " +
            "INNER JOIN tc.teacher t " +
            "WHERE " +
            "FUNCTION('DATE_FORMAT', tc.joinDate, '%Y-%m') <= :date " +
            "AND (tc.leaveDate IS NULL OR FUNCTION('DATE_FORMAT', tc.leaveDate, '%Y-%m') >= :date) " +
            "AND t.teacherId = :teacherId " +
            "ORDER BY cd.className")
    int countTeacherClass(
            @Param("date") String date,
            @Param("teacherId") int teacherId,
            @Param("addressId") Integer addressId
    );

    @Query("SELECT new com.example.demotrungtam.classlist.dto.ClassDto(" +
            "c.classId, " +
            "cd.classDetailId, " +
            "cd.className, " +
            "a.addressId, " +
            "a.addressName, " +
            "cd.schedule, " +
            "cd.studyTime, " +
            "t.studentId, " +
            "t.studentName, " +
            "cd.startDate, " +
            "cd.endDate) " +
            "FROM ClassInfo c " +
            "INNER JOIN c.classDetails cd ON " +
            "(cd.classInfo.classId, cd.startDate) IN ( " +
            "SELECT cd1.classInfo.classId, MAX(cd1.startDate) as startDate " +
            "FROM ClassDetail cd1 " +
            "WHERE (:date IS NULL OR FUNCTION('DATE_FORMAT', cd1.startDate, '%Y-%m') <= :date) " +
            "AND (:date IS NULL OR (cd1.endDate IS NULL OR FUNCTION('DATE_FORMAT', cd1.endDate, '%Y-%m') >= :date)) " +
            "AND (:addressId IS NULL OR cd1.addressInfo.addressId = :addressId ) " +
            "GROUP BY cd1.classInfo.classId) " +
            "INNER JOIN cd.addressInfo a " +
            "INNER JOIN c.studentClasses tc ON " +
            "(tc.classInfo.classId, tc.joinDate) IN ( " +
            "SELECT tc1.classInfo.classId, MAX(tc1.joinDate) as joinDate " +
            "FROM StudentClass tc1 " +
            "WHERE tc1.student.studentId = :studentId " +
            "GROUP BY tc1.classInfo.classId) " +
            "INNER JOIN tc.student t " +
            "WHERE " +
            "FUNCTION('DATE_FORMAT', tc.joinDate, '%Y-%m') <= :date " +
            "AND (tc.leaveDate IS NULL OR FUNCTION('DATE_FORMAT', tc.leaveDate, '%Y-%m') >= :date) " +
            "AND t.studentId = :studentId " +
            "ORDER BY cd.className")
    List<ClassDto> findStudentClass(
            @Param("date") String date,
            @Param("studentId") int studentId,
            @Param("addressId") Integer addressId,
            Pageable pageable
    );
}
