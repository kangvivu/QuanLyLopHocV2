package com.example.demotrungtam.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="class_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="class_detail_id")
    private int classDetailId;
    @Column(name="class_name")
    private String className;
    @Column(name="start_date")
    private String startDate;
    @Column(name="end_date")
    private String endDate;
    @Column(name="schedule")
    private String schedule;
    @Column(name="tuition_session")
    private double tuitionSession;
    @Column(name="time")
    private String studyTime;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassInfo classInfo;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressInfo addressInfo;

    public int getClassDetailId() {
        return classDetailId;
    }

    public void setClassDetailId(int classDetailId) {
        this.classDetailId = classDetailId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public double getTuitionSession() {
        return tuitionSession;
    }

    public void setTuitionSession(double tuitionSession) {
        this.tuitionSession = tuitionSession;
    }

    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }
}
