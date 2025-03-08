package com.example.demotrungtam.studentmanage.repository;

import com.example.demotrungtam.entity.Telephone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TelephoneManageRepository extends JpaRepository<Telephone, Integer> {

    @Query("SELECT t.phone " +
            "FROM Telephone t " +
            "WHERE t.student.studentId = :studentId")
    List<String> getPhoneByStudentId(
            @Param("studentId") Integer studentId
    );
}
