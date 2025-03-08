package com.example.demotrungtam.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String name;
    private String username;
    private String password;
    private String roleName;

    public UserDto(String username, String password, String roleName) {
        this.username = username;
        this.password = password;
        this.roleName = roleName;
    }

    public UserDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
