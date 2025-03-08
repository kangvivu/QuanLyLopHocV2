package com.example.demotrungtam.login.controller;

import com.example.demotrungtam.login.config.jwt.JwtProvider;
import com.example.demotrungtam.login.dto.UserDto;
import com.example.demotrungtam.login.payload.LoginRequest;
import com.example.demotrungtam.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest) {
        // Lay thong tin tu loginRequest va gan vao authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        Map<String, Object> response = new HashMap<>();
        // Lay username tu securityContext
        String username = authentication.getName();
        // Lay cac quyen cua user
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = userService.getAuthority(authorities);
        UserDto userDto = userService.getUserInfo(role, username);
        // Tao ma jwt
        String token = jwtProvider.generateToken(username, authorities);
        // Them jwt vao response
        response.put("id", userDto.getId());
        response.put("name", userDto.getName());
        response.put("role", role);
        response.put("accessToken", token);
        return response;
    }

    /**
     * Test
     */
//    @GetMapping("/user")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public UserDto findAll() {
//        return userService.getUserInfo(Constant.ROLE_ADMIN, "admin");
//    }

    // Phat trien dang ki
}