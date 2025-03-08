package com.example.demotrungtam.login.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtProvider {
    static final String JWT_KEY = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER = "Authorization";
    static final long EXP_TIME = 1000*60*60;

    /**
     * Thuc hien tao token
     */
    public String generateToken(String username, Collection<? extends GrantedAuthority> grantedAuthority) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        // set thoi gian het han token: 120 minutes
        Date expiredDate = new Date(now.getTime() + EXP_TIME);
        return Jwts.builder()
                .setIssuer("English House")
                .setSubject("JWT Token")
                .claim("username", username)
                .claim("authorities", getAuthority(grantedAuthority))
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(key).compact();
    }

    /**
     * Thuc hien lay cac quyen cua user
     */
    private String getAuthority(Collection<? extends GrantedAuthority> grantedAuthority) {
        Set<String> authoritySet = new HashSet<>();
        for(GrantedAuthority authority: grantedAuthority){
            authoritySet.add(authority.getAuthority());
        }
        return String.join(",",authoritySet);
    }

    /**
     *  Trich xuat thong tin user tu token
     */
    public Authentication getAuthentication(HttpServletRequest request) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
        String token = request.getHeader(HEADER);
        if(token != null && token.startsWith(TOKEN_PREFIX)) {
            String jwtToken = token.substring(7);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
            String username = (String) claims.get("username");
            String authorities = (String) claims.get("authorities");
            Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
            return authentication;
        } else {
            return null;
        }
    }

}
