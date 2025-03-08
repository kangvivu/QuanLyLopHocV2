package com.example.demotrungtam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="student_attendance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_attendance_id")
    private int studentAttendanceId;
    @Column(name="is_attend")
    private int isAttend;
    @Column(name="score")
    private Double score;
    @Column(name="do_homework")
    private Integer doHomework;
    @Column(name="session_comment")
    private String sessionComment;
    @Column(name="additional_attendance")
    private String additionalAttendance;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    public int getStudentAttendanceId() {
        return studentAttendanceId;
    }

    public void setStudentAttendanceId(int studentAttendanceId) {
        this.studentAttendanceId = studentAttendanceId;
    }

    public int getIsAttend() {
        return isAttend;
    }

    public void setIsAttend(int isAttend) {
        this.isAttend = isAttend;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getDoHomework() {
        return doHomework;
    }

    public void setDoHomework(Integer doHomework) {
        this.doHomework = doHomework;
    }

    public String getSessionComment() {
        return sessionComment;
    }

    public void setSessionComment(String sessionComment) {
        this.sessionComment = sessionComment;
    }

    public String getAdditionalAttendance() {
        return additionalAttendance;
    }

    public void setAdditionalAttendance(String additionalAttendance) {
        this.additionalAttendance = additionalAttendance;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }
}
