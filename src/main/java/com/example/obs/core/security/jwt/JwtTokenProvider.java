package com.example.obs.core.security.jwt;

import com.example.obs.core.security.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final JwtProperties jwtProperties;
    private final Key key;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date validity = new Date(now + jwtProperties.getAccessTokenExpirationMs());

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateTokenFromUsername(String username, Collection<? extends GrantedAuthority> authorities) {
        String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date validity = new Date(now + jwtProperties.getAccessTokenExpirationMs());

        return Jwts.builder()
                .setSubject(username)
                .claim("auth", authoritiesString)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .filter(auth -> !auth.trim().isEmpty())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Geçersiz JWT token formatı: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token süresi dolmuş: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Desteklenmeyen JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT içerik dizesi boş: {}", e.getMessage());
        }
        return false;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}