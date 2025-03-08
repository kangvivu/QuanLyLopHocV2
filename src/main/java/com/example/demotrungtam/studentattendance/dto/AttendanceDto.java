package com.example.demotrungtam.studentattendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private Integer attendanceId;
    private String attendanceDate;
}
