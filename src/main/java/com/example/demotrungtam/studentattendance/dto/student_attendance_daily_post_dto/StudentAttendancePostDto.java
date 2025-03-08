package com.example.demotrungtam.studentattendance.dto.student_attendance_daily_post_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAttendancePostDto {
    private Integer studentAttendanceId;
    private Integer studentId;
    private Integer isAttend;
    private Double score;
    private Integer doHomework;
    private String sessionComment;
    private String additionalAttendance;
}
