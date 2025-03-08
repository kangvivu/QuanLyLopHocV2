package com.example.demotrungtam.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="class_id")
    private int classId;
    @OneToMany(mappedBy = "classInfo")
    private List<ClassDetail> classDetails;
    @OneToMany(mappedBy = "classInfo")
    private List<MonthlyNote> monthlyNotes;
    @OneToMany(mappedBy = "classInfo")
    private List<StudentClass> studentClasses;
    @OneToMany(mappedBy = "classInfo")
    private List<Attendance> attendances;
    @OneToMany(mappedBy = "classInfo")
    private List<TeacherClass> teacherClasses;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public List<ClassDetail> getClassDetails() {
        return classDetails;
    }

    public void setClassDetails(List<ClassDetail> classDetails) {
        this.classDetails = classDetails;
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

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public List<TeacherClass> getTeacherClasses() {
        return teacherClasses;
    }

    public void setTeacherClasses(List<TeacherClass> teacherClasses) {
        this.teacherClasses = teacherClasses;
    }
}
