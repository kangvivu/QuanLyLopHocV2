package com.example.demotrungtam.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id")
    private int studentId;
    @Column(name="student_name")
    private String studentName;
    @Column(name="dob")
    private String dob;
    @Column(name="student_address")
    private String studentAddress;
    @Column(name="student_status")
    private int studentStatus;
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
    @OneToMany(mappedBy = "student")
    private List<Telephone> phones;
    @OneToMany(mappedBy = "student")
    private List<MonthlyNote> monthlyNotes;
    @OneToMany(mappedBy = "student")
    private List<StudentClass> studentClasses;
    @OneToMany(mappedBy = "student")
    private List<StudentAttendance> studentAttendances;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public int getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(int studentStatus) {
        this.studentStatus = studentStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Telephone> getPhones() {
        return phones;
    }

    public void setPhones(List<Telephone> phones) {
        this.phones = phones;
    }

    public List<MonthlyNote> getMonthlyNotes() {
        return monthlyNotes;
    }

    public void setMonthlyNotes(List<MonthlyNote> monthlyNotes) {
        this.monthlyNotes = monthlyNotes;
    }

    public List<StudentClass> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(List<StudentClass> studentClasses) {
        this.studentClasses = studentClasses;
    }

    public List<StudentAttendance> getStudentAttendances() {
        return studentAttendances;
    }

    public void setStudentAttendances(List<StudentAttendance> studentAttendances) {
        this.studentAttendances = studentAttendances;
    }
}
