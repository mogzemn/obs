package com.example.obs.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/faculties/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/departments/all").permitAll()
                        
                        // Admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        
                        // Idari personel yetkileri
                        .requestMatchers(HttpMethod.POST, "/api/students").hasAnyRole("ADMIN", "ADMINISTRATIVE_STAFF")
                        .requestMatchers(HttpMethod.PUT, "/api/students/**").hasAnyRole("ADMIN", "ADMINISTRATIVE_STAFF")
                        
                        // Academic endpoints
                        .requestMatchers("/api/academics/**").hasAnyRole("ADMIN", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN")
                        
                        // Student endpoints - dikkat: öğrenciler sadece kendi bilgilerine erişebilir (controller'da kontrol edilecek)
                        .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("ADMIN", "STUDENT", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN", "ADMINISTRATIVE_STAFF")
                        
                        // Course endpoints
                        .requestMatchers(HttpMethod.POST, "/api/courses/**").hasAnyRole("ADMIN", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN")
                        .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasAnyRole("ADMIN", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasAnyRole("ADMIN", "DEPARTMENT_HEAD", "DEAN")
                        .requestMatchers(HttpMethod.GET, "/api/courses/**").hasAnyRole("ADMIN", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN", "STUDENT", "ADMINISTRATIVE_STAFF")
                        
                        // Attendance endpoints
                        .requestMatchers(HttpMethod.POST, "/api/attendances/**").hasAnyRole("ADMIN", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN")
                        .requestMatchers(HttpMethod.PUT, "/api/attendances/**").hasAnyRole("ADMIN", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN")
                        .requestMatchers(HttpMethod.GET, "/api/attendances/**").hasAnyRole("ADMIN", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN", "STUDENT", "ADMINISTRATIVE_STAFF")
                        
                        // Grade endpoints
                        .requestMatchers(HttpMethod.POST, "/api/grades/**").hasAnyRole("ADMIN", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN") 
                        .requestMatchers(HttpMethod.PUT, "/api/grades/**").hasAnyRole("ADMIN", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN")
                        .requestMatchers(HttpMethod.GET, "/api/grades/**").hasAnyRole("ADMIN", "ACADEMIC", "DEPARTMENT_HEAD", "DEAN", "STUDENT", "ADMINISTRATIVE_STAFF")
                        
                        // Default for other endpoints
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

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
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Üretim ortamında bu daha spesifik olmalı
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 