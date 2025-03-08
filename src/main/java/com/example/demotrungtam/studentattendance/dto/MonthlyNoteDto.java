package com.example.demotrungtam.studentattendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyNoteDto {
    private Integer monthlyNoteId;
    private String noteMonth;
    private String monthComment;
    private Integer studentId;
    private Integer classId;
}
