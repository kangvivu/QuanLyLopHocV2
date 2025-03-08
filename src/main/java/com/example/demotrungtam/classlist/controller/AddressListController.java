package com.example.demotrungtam.classlist.controller;

import com.example.demotrungtam.classlist.dto.AddressDto;
import com.example.demotrungtam.classlist.service.AddressListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AddressListController {
    private final AddressListService addressService;

    @Autowired
    public AddressListController(AddressListService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/getAllAddress")
    public ResponseEntity<Object> getAllAddress() {
        List<AddressDto> addresses = this.addressService.getAllAddress();
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.OK.value());
        response.put("addresses", addresses);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
