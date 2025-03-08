package com.example.demotrungtam.classlist.dto.classUpdateDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherClassUpdateDto {
    private Integer teacherClassId;
    private Integer teacherId;
    private Integer classId;
    private String latestAttendanceDate;
    private String applyDate;
}