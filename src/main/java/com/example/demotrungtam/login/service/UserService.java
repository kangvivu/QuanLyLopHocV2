package com.example.demotrungtam.login.service;

import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.login.dto.UserDto;
import com.example.demotrungtam.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getAuthority(Collection<? extends GrantedAuthority> grantedAuthority) {
        Set<String> authoritySet = new HashSet<>();
        for(GrantedAuthority authority: grantedAuthority){
            authoritySet.add(authority.getAuthority());
        }
        return String.join(",",authoritySet);
    }

    public UserDto getUserInfo(String role, String name) {
        if(role.equals(Constant.ROLE_ADMIN) || role.equals(Constant.ROLE_TEACHER)) {
            return userRepository.findTeacher(name);
        } else {
            return userRepository.findStudent(name);
        }
    }
}
