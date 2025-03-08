package com.example.demotrungtam.classlist.service;

import com.example.demotrungtam.classlist.dto.AddressDto;
import com.example.demotrungtam.classlist.repository.AddressListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressListService {
    private AddressListRepository addressListRepository;

    @Autowired
    public AddressListService(AddressListRepository addressListRepository) {
        this.addressListRepository = addressListRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public List<AddressDto> getAllAddress() {
        return addressListRepository.findAllAddress();
    }
}
