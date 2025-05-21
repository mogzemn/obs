package com.example.obs.core.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class RateLimitFilter extends OncePerRequestFilter {
    
    private final Map<String, RequestCount> requestCounts = new ConcurrentHashMap<>();
    
    private static final long WINDOW_SIZE_MS = 60 * 1000;
    
    private static final int MAX_REQUESTS_PER_WINDOW = 100;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        String requestUri = request.getRequestURI();
        
        if (isExcludedUrl(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String clientIp = getClientIp(request);
        
        if (isRateLimitExceeded(clientIp)) {
            log.warn("Rate limit exceeded for IP: {}", clientIp);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Çok fazla istek yapıldı. Lütfen daha sonra tekrar deneyin.");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean isRateLimitExceeded(String clientIp) {
        RequestCount requestCount = requestCounts.computeIfAbsent(clientIp, k -> new RequestCount());
        
        if (System.currentTimeMillis() - requestCount.getWindowStartTime() > WINDOW_SIZE_MS) {
            requestCount.resetWindow();
        }
        
        int currentCount = requestCount.incrementAndGet();
        return currentCount > MAX_REQUESTS_PER_WINDOW;
    }
    
    private static class RequestCount {
        private final AtomicInteger count = new AtomicInteger(0);
        private long windowStartTime = System.currentTimeMillis();
        
        public int incrementAndGet() {
            return count.incrementAndGet();
        }
        
        public long getWindowStartTime() {
            return windowStartTime;
        }
        
        public void resetWindow() {
            count.set(0);
            windowStartTime = System.currentTimeMillis();
        }
    }
    
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
    
    private boolean isExcludedUrl(String requestUri) {
        return requestUri.startsWith("/actuator/health") || 
               requestUri.startsWith("/swagger-ui") || 
               requestUri.startsWith("/v3/api-docs");
    }
}
