package com.example.demotrungtam.classlist.dto.classEditDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassDetailDto {
    private Integer classDetailId;
    private Integer classId;
    private String className;
    private Integer addressId;
    private String startDate;
    private String endDate;
    private String schedule;
    private Double tuitionSession;
    private String studyTime;
    private String latestAttendanceDate;
    private TeacherClassDetailDto teacherClassDetail;

    public ClassDetailDto(
            Integer classDetailId,
            Integer classId,
            String className,
            Integer addressId,
            String startDate,
            String endDate,
            String schedule,
            Double tuitionSession,
            String studyTime,
            String latestAttendanceDate
    ) {
        this.classDetailId = classDetailId;
        this.classId = classId;
        this.className = className;
        this.addressId = addressId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.schedule = schedule;
        this.tuitionSession = tuitionSession;
        this.studyTime = studyTime;
        this.latestAttendanceDate = latestAttendanceDate;
    }
}
