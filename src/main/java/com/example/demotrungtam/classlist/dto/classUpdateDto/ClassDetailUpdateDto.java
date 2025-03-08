package com.example.demotrungtam.classlist.dto.classUpdateDto;

import com.example.demotrungtam.entity.AddressInfo;
import com.example.demotrungtam.entity.ClassInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassDetailUpdateDto {
    private Integer classDetailId;
    private Integer classId;
    private String className;
    private Integer addressId;
    private String schedule;
    private Double tuitionSession;
    private String studyTime;
    private String latestAttendanceDate;
    private String applyDate;
}
