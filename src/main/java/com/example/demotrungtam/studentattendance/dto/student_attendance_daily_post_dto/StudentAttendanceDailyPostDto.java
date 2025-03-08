package com.example.demotrungtam.studentattendance.dto.student_attendance_daily_post_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAttendanceDailyPostDto {
    private AttendancePostDto attendance;
    private List<StudentAttendancePostDto> studentAttendance;
}
