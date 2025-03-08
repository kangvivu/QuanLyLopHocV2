package com.example.demotrungtam.classlist.repository;

import com.example.demotrungtam.classlist.dto.AddressDto;
import com.example.demotrungtam.entity.AddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressListRepository extends JpaRepository<AddressInfo, Integer> {
    @Query("SELECT new com.example.demotrungtam.classlist.dto.AddressDto(" +
            "a.addressId, " +
            "a.addressName) " +
            "FROM AddressInfo a")
    List<AddressDto> findAllAddress();
}
