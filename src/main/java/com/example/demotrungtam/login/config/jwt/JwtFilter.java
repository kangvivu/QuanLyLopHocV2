package com.example.demotrungtam.login.config.jwt;

import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.exception.CustomException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;

    // xu ly de bat duoc ngoai le o global exception
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public JwtFilter(JwtProvider jwtProvider, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtProvider = jwtProvider;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // trich xuat thong tin tu jwt
            Authentication authentication = jwtProvider.getAuthentication(request);
            // set authentication
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            handlerExceptionResolver.resolveException(request, response, null, new CustomException(Constant.INVALID_TOKEN, Constant.ER001));
        }
    }

    // khong filer voi path "/api/login"
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/api/login");
    }
}
