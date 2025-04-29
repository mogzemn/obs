package com.example.obs.core.security.auth;

import com.example.obs.business.requests.LoginRequest;
import com.example.obs.business.requests.TokenRefreshRequest;
import com.example.obs.business.responses.JwtResponse;
import com.example.obs.core.security.jwt.JwtTokenProvider;
import com.example.obs.dateAccess.UserRepository;
import com.example.obs.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        // Refresh token oluşturmak için özel bir metod kullanabilirsiniz
        String refreshToken = jwt; // Basit örnek için aynı token kullanıldı

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Kullanıcı rolünü belirle
        String role = determineUserRole(user);

        return new JwtResponse(
                jwt,
                "Bearer",
                refreshToken,
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                role
        );
    }

    public JwtResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Yenileme tokenı geçerli değil");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        String newAccessToken = jwtTokenProvider.generateTokenFromUsername(
                username, userDetails.getAuthorities());

        // Kullanıcı rolünü belirle
        String role = determineUserRole(user);

        return new JwtResponse(
                newAccessToken,
                "Bearer",
                refreshToken,
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                role
        );
    }

    private String determineUserRole(User user) {
        if (Boolean.TRUE.equals(user.getIsAdmin())) {
            return "ADMIN";
        } else if (user.getAcademic() != null) {
            return "ACADEMIC";
        } else if (user.getStudent() != null) {
            return "STUDENT";
        } else if (user.getAdministrativeStaff() != null) {
            return "STAFF";
        } else {
            return "USER";
        }
    }
}