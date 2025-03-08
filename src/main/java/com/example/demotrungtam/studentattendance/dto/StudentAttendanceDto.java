package com.example.demotrungtam.studentattendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAttendanceDto {
    private int studentId;
    private String studentName;
    private Integer monthlyNoteId;
    private String monthComment;
    private String attendanceDate;
    private Integer isAttend;
    private String additionalAttendance;
}
