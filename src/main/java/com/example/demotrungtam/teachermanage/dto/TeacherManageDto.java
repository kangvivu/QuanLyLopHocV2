package com.example.demotrungtam.teachermanage.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherManageDto {
    private Integer teacherId;
    private String teacherName;
    private String teacherAddress;
    private String teacherPhone;
    private Integer teacherStatus;
    private String username;
//    private Double sessionFee;


    // Getter for teacherId
    public Integer getTeacherId() {
        return teacherId;
    }

    // Setter for teacherId
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    // Getter for teacherName
    public String getTeacherName() {
        return teacherName;
    }

    // Setter for teacherName
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    // Getter for address
    public String getTeacherAddress() {
        return teacherAddress;
    }

    // Setter for address
    public void setTeacherAddress(String address) {
        this.teacherAddress = address;
    }

    // Getter for address
    public String getTeacherPhone() {
        return teacherPhone;
    }

    // Setter for address
    public void setTeacherPhone(String phone) {
        this.teacherPhone = phone;
    }

    // Getter for
    public Integer getTeacherStatus() {
        return teacherStatus;
    }

    // Setter for
    public void setTeacherStatus(Integer status) {
        this.teacherStatus = status;
    }

    // Getter và Setter cho username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for teacherId
//    public Double getSessionFee() {
//        return sessionFee;
//    }
//
//    // Setter for teacherId
//    public void setSessionFee(Double sessionFee) {
//        this.sessionFee = sessionFee;
//    }
}
