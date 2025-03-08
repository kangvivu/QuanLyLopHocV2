package com.example.demotrungtam.classlist.dto.class_registration_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClassRegistrationDto {
    private String className;
    private Integer addressId;
    private String startDate;
    private String schedule;
    private Double tuitionSession;
    private String studyTime;
    private Integer teacherId;
}
