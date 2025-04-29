package com.example.obs.core.security.auth;

import com.example.obs.business.requests.LoginRequest;
import com.example.obs.business.responses.LoginResponse;
import com.example.obs.business.requests.TokenRefreshRequest;
import com.example.obs.core.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                       CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(jwt, "Bearer");
    }

    public LoginResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(requestRefreshToken)) {
            throw new RuntimeException("Yenileme tokenı geçerli değil. Lütfen tekrar giriş yapınız.");
        }

        String username = jwtTokenProvider.getUsernameFromToken(requestRefreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String newToken = jwtTokenProvider.generateTokenFromUsername(
                username, userDetails.getAuthorities());

        return new LoginResponse(newToken, "Bearer");
    }
}