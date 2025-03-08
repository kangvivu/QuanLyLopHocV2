package com.example.demotrungtam.login.config;

import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.exception.CustomException;
import com.example.demotrungtam.login.dto.UserDto;
import com.example.demotrungtam.login.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
/**
 * Thuc hien xac thuc user, password tu client va db
 */
public class UsernamePasswordAuthenProvider implements AuthenticationProvider {

    private UserRepository userRepository;

    // Giai ma password
    private PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthenProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Lay username tu client
        String username = authentication.getName();
        // Lay password tu client
        String password = authentication.getCredentials().toString();
        // Tim kiem teacher theo username
        UserDto user = userRepository.findByUsername(username);
        if (user != null) {
            // Check password tu client va db
            if (passwordEncoder.matches(password, user.getPassword())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(user.getRoleName()));
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            } else {
                throw new CustomException(Constant.INVALID_PASSWORD, Constant.ER003);
            }
        } else {
            throw new CustomException(Constant.INVALID_USERNAME, Constant.ER002);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
