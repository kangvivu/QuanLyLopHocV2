package com.example.demotrungtam.config;

import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Xu ly exception role
 */
@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public CustomAuthenticationEntryPoint(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        this.handlerExceptionResolver.resolveException(request, response, null, new CustomException(Constant.ACCESS_DENIED, Constant.ER004));
    }
}