package com.example.obs.core.security.jwt;

import com.example.obs.core.security.config.JwtProperties;
import com.example.obs.core.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String path = request.getRequestURI();
        
        if (path.contains("/api/auth/login") || path.contains("/api/auth/refresh") || 
            path.contains("/v3/api-docs") || path.contains("/swagger-ui") || 
            path.contains("/actuator/health") || path.contains("/actuator/info")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("JWT auth filter çalışıyor: {}", path);
        
        final String authHeader = request.getHeader(jwtProperties.getHeaderName());
        log.debug("Authorization header: {}", authHeader != null ? "Mevcut" : "Yok");
        
        if (authHeader == null || !authHeader.startsWith(jwtProperties.getTokenPrefix())) {
            log.warn("Authorization header eksik veya geçersiz format: {}", authHeader);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(jwtProperties.getTokenPrefix().length()).trim();
            log.debug("JWT token çıkarıldı, uzunluk: {}", jwt.length());
            
            if (jwt.isEmpty()) {
                log.warn("JWT token boş");
                filterChain.doFilter(request, response);
                return;
            }
            
            final String userEmail = jwtUtil.extractUsername(jwt);
            log.debug("JWT token'dan kullanıcı e-postası çıkarıldı: {}", userEmail);
            
            if (userEmail != null && !userEmail.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("JWT token için kullanıcı yükleniyor: {}", userEmail);
                
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                    log.debug("Kullanıcı detayları başarıyla yüklendi: {}", userDetails.getUsername());
                    
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        log.debug("JWT token geçerli, kullanıcı doğrulanıyor: {}", userEmail);
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.debug("Kullanıcı doğrulandı ve güvenlik bağlamına eklendi: {}, roller: {}", 
                                userEmail, userDetails.getAuthorities());
                    } else {
                        log.warn("JWT token doğrulaması başarısız: {}", userEmail);
                    }
                } catch (Exception e) {
                    log.error("Kullanıcı detaylarını yükleme hatası: {}", e.getMessage(), e);
                }
            }
            
            filterChain.doFilter(request, response);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("JWT token süresi dolmuş: {}", e.getMessage());
            handleJwtException(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token süresi dolmuş", e);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.warn("JWT imzası geçersiz: {}", e.getMessage());
            handleJwtException(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT imzası geçersiz", e);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.warn("JWT token formatı bozuk: {}", e.getMessage());
            handleJwtException(response, HttpServletResponse.SC_BAD_REQUEST, "JWT token formatı bozuk", e);
        } catch (Exception e) {
            log.error("JWT token işlenirken beklenmeyen hata oluştu: {}", e.getMessage(), e);
            handleJwtException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT token işlenirken hata oluştu: " + e.getMessage(), e);
        }
    }
    
    private void handleJwtException(HttpServletResponse response, int status, String message, Exception e) throws IOException {
        log.warn(message + ": {}", e.getMessage());
        
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", status);
        errorDetails.put("error", "Yetkilendirme Hatası");
        errorDetails.put("message", message);
        errorDetails.put("path", "/api");
        
        objectMapper.writeValue(response.getOutputStream(), errorDetails);
    }
}
