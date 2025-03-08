package com.example.demotrungtam.config;

import com.example.demotrungtam.login.config.jwt.JwtFilter;
import com.example.demotrungtam.login.config.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    private JwtProvider jwtProvider;

    // Vi filter chạy ra truoc controller nen phai dung HandlerExceptionResolver de co the bat ngoai le ở Global exception
    // Phai co @Qualifier
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    // Bat ngoai le role
    @Qualifier("customAuthenticationEntryPoint")
    AuthenticationEntryPoint authEntryPoint;

    @Autowired
    public SecurityConfig(
            JwtProvider jwtProvider,
            HandlerExceptionResolver handlerExceptionResolver,
            AuthenticationEntryPoint authEntryPoint
    ) {
        this.jwtProvider = jwtProvider;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter(this.jwtProvider, this.handlerExceptionResolver);
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // setting cors
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    // cho phép http://localhost:4200 access
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                    // cho phép all method
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    // cho phép all header
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    // thời gian tồn tại cors 1h
                    config.setMaxAge(3600L);
                    return config;
                }))
                // enable csrf
                .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers(
                                "/contact",
                                "/api/login",
                                "/api/save-monthly-note",
                                "/api/save-student-attendance-daily",
                                "/api/register-class",
                                "/api/save_class_detail",
                                "/api/save_teacher_class",
                                "/api/save_student",
                                "/api/save_teacher",
                                "/api/remove_teacher",
                                "/api/save_student_class"

                        )
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        // pattern, role: có thể truyền vào string hoặc string[]
//                        // Nếu truyền vào thì chỉ phân quyền method đó, nếu không thì default allow all method
//                        .requestMatchers(HttpMethod.GET,"/teacher/**").hasAnyRole("ADMIN", "TEACHER")
//                        .requestMatchers("/teacher/**").authenticated()
//                        .requestMatchers("/api/login").permitAll())
//                        .requestMatchers("/api/user/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/**").authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(basic -> basic.authenticationEntryPoint(authEntryPoint));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}