package com.example.demotrungtam.studentattendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAttendanceDailyDto {
    private Integer studentId;
    private String studentName;
    private Integer attendanceId;
    private String attendanceDate;
    private Integer studentAttendanceId;
    private Integer isAttend;
    private Double score;
    private Integer doHomework;
    private String sessionComment;
    private String additionalAttendance;
}
