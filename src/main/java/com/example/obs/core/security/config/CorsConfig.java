package com.example.obs.core.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowedOriginPatterns(Arrays.asList("*")); 
        
        config.setAllowCredentials(true);
        
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", 
                "Access-Control-Allow-Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers",
                "X-Requested-With"));
        
        config.setMaxAge(3600L);
        
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
