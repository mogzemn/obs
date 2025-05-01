package com.example.obs.core.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Bu metot, kimlik doğrulama gerektiren bir kaynağa erişmeye çalışan kimliği doğrulanmamış bir kullanıcı için çağrılır
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Yetkisiz erişim: " + authException.getMessage());
    }
} 