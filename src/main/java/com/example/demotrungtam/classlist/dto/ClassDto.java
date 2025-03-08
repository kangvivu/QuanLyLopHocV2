package com.example.demotrungtam.classlist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDto {
    private Integer classId;
    private Integer classDetailId;
    private String className;
    private Integer addressId;
    private String addressName;
    private String schedule;
    private String classTime;
    private Integer teacherId;
    private String teacherName;
    private String startDate;
    private String endDate;

}
