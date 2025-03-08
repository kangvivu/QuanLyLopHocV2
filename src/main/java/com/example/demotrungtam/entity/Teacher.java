package com.example.demotrungtam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="teacher")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="teacher_id")
    private int teacherId;
    @Column(name="teacher_name")
    private String teacherName;
    @Column(name="teacher_address")
    private String teacherAddress;
    @Column(name="teacher_phone")
    private String teacherPhone;
    @Column(name="teacher_status")
    private Integer teacherStatus;
    @Column(name="session_fee")
    private Double sessionFee;
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
    @OneToMany(mappedBy = "teacher")
    private List<TeacherClass> teacherClasses;

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherAddress() {
        return teacherAddress;
    }

    public void setTeacherAddress(String teacherAddress) {
        this.teacherAddress = teacherAddress;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public Integer getTeacherStatus() {
        return teacherStatus;
    }

    public void setTeacherStatus(Integer teacherStatus) {
        this.teacherStatus = teacherStatus;
    }

    public Double getSessionFee() {
        return sessionFee;
    }

    public void setSessionFee(Double sessionFee) {
        this.sessionFee = sessionFee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TeacherClass> getTeacherClasses() {
        return teacherClasses;
    }

    public void setTeacherClasses(List<TeacherClass> teacherClasses) {
        this.teacherClasses = teacherClasses;
    }
}
