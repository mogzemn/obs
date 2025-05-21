package com.example.obs.core.security.util;

import com.example.obs.core.security.config.JwtProperties;
import com.example.obs.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("Token süresini kontrol ederken hata oluştu: {}", e.getMessage());
            return true;
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        claims.put("roles", userDetails.getAuthorities().stream()
                .filter(a -> a.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
                
        claims.put("permissions", userDetails.getAuthorities().stream()
                .filter(a -> !a.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            claims.put("userId", user.getId());
            claims.put("fullName", user.getFirstName() + " " + user.getLastName());
            claims.put("email", user.getEmail());
            
            if (user.getStudent() != null) {
                claims.put("userType", "STUDENT");
                claims.put("studentNumber", user.getStudent().getStudentNumber());
                claims.put("studentId", user.getStudent().getId());
            } else if (user.getAcademic() != null) {
                claims.put("userType", "ACADEMIC");
                claims.put("academicNumber", user.getAcademic().getAcademicNumber());
                claims.put("academicId", user.getAcademic().getId());
            } else if (user.getAdministrativeStaff() != null) {
                claims.put("userType", "ADMINISTRATIVE");
                claims.put("staffNumber", user.getAdministrativeStaff().getStaffNumber());
                claims.put("staffId", user.getAdministrativeStaff().getId());
            }
        }
        
        long expirationTimeInMs = jwtProperties.getAccessTokenExpirationMinutes() * 60 * 1000;
        return createToken(claims, userDetails.getUsername(), expirationTimeInMs);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        long expirationTimeInMs = jwtProperties.getRefreshTokenExpirationDays() * 24 * 60 * 60 * 1000;
        return createToken(claims, userDetails.getUsername(), expirationTimeInMs);
    }

    private String createToken(Map<String, Object> claims, String subject, long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            if (token == null || token.isEmpty()) {
                log.error("Token boş veya null");
                return false;
            }
            
            final String username = extractUsername(token);
            if (username == null || username.isEmpty()) {
                log.error("Token'dan kullanıcı adı çıkarılamadı");
                return false;
            }
            
            if (!username.equals(userDetails.getUsername())) {
                log.error("Token'daki kullanıcı adı ({}) ile verilen kullanıcı adı ({}) eşleşmiyor", 
                        username, userDetails.getUsername());
                return false;
            }
            
            if (isTokenExpired(token)) {
                log.error("Token süresi dolmuş");
                return false;
            }
            
            log.debug("Token başarıyla doğrulandı: {}", username);
            return true;
        } catch (Exception e) {
            log.error("Token doğrulanırken hata oluştu: {}", e.getMessage(), e);
            return false;
        }
    }
}
