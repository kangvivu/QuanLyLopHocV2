package com.example.demotrungtam.classlist.dto.classEditDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherClassDetailDto {
    private Integer teacherClassId;
    private Integer teacherId;
    private String joinDate;
    private String leaveDate;
}
