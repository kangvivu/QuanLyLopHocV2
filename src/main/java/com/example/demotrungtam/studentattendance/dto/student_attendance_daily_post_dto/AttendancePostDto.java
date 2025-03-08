package com.example.demotrungtam.studentattendance.dto.student_attendance_daily_post_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendancePostDto {
    private Integer attendanceId;
    private Integer classId;
    private String attendanceDate;
}
