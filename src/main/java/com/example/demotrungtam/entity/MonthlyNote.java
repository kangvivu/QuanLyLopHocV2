package com.example.demotrungtam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="monthly_note")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="monthly_note_id")
    private int monthlyNoteId;
    @Column(name="note_month")
    private String noteMonth;
    @Column(name="month_comment")
    private String monthComment;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassInfo classInfo;

    public int getMonthlyNoteId() {
        return monthlyNoteId;
    }

    public void setMonthlyNoteId(int monthlyNoteId) {
        this.monthlyNoteId = monthlyNoteId;
    }

    public String getNoteMonth() {
        return noteMonth;
    }

    public void setNoteMonth(String noteMonth) {
        this.noteMonth = noteMonth;
    }

    public String getMonthComment() {
        return monthComment;
    }

    public void setMonthComment(String monthComment) {
        this.monthComment = monthComment;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }
}