package com.example.demotrungtam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="teacher_class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="teacher_class_id")
    private int teacherClassId;
    @Column(name="join_date")
    private String joinDate;
    @Column(name="leave_date")
    private String leaveDate;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassInfo classInfo;

    public int getTeacherClassId() {
        return teacherClassId;
    }

    public void setTeacherClassId(int teacherClassId) {
        this.teacherClassId = teacherClassId;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }
}
