package com.example.demotrungtam.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private int addressId;
    @Column(name="address_name")
    private String addressName;
    @OneToMany(mappedBy = "addressInfo")
    @JsonBackReference
    private List<ClassDetail> classDetails;

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public List<ClassDetail> getClassDetails() {
        return classDetails;
    }

    public void setClassDetails(List<ClassDetail> classDetails) {
        this.classDetails = classDetails;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
