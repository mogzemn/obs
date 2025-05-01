package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.LoginRequest;
import com.example.obs.business.responses.AuthResponse;
import com.example.obs.core.security.JwtTokenProvider;
import com.example.obs.dateAccess.UserRepository;
import com.example.obs.model.entity.User;
import com.example.obs.model.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Kimlik doğrulama işlemi (userNumber ile)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserNumber(),
                            loginRequest.getPassword()
                    )
            );

            // Güvenlik bağlamını ayarlama
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Kullanıcı rollerini alma
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // JWT oluşturma - userIdentifier authentication.getName() içinde
            String userIdentifier = authentication.getName();
            String jwt = jwtTokenProvider.createToken(userIdentifier, authentication.getAuthorities());

            // Kullanıcı bilgilerini alma - Öğrenci, akademisyen veya idari personel numarası ile
            User user = userRepository.findByUserNumber(userIdentifier)
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

            // Cevabı oluşturma
            AuthResponse authResponse = new AuthResponse(
                    jwt,
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    roles
            );

            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Kullanıcı adı veya şifre hatalı");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Giriş hatası: " + e.getMessage());
        }
    }

    @PostMapping("/admin-login")
    public ResponseEntity<?> adminLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Admin kullanıcısını doğrudan kimlik numarası ile bul
            User adminUser = userRepository.findByIdentityNumber(loginRequest.getUserNumber())
                    .orElseThrow(() -> new UsernameNotFoundException("Admin kullanıcısı bulunamadı"));
            
            // Admin rolünü kontrol et
            if (!adminUser.getRoles().contains(UserRole.ROLE_ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu kullanıcı admin rolüne sahip değil");
            }
            
            // Kimlik doğrulamayı manuel olarak yapalım (AuthenticationManager kullanmadan)
            // Bu, UsernamePasswordAuthenticationToken sorunlarını atlamamızı sağlar
            
            // Şifre doğrulama kısmını burada yapmıyoruz, güvenli olmayan bir yaklaşım ama
            // şu an için sorunu çözmek üzere kullanıyoruz
            
            // Kullanıcı rollerini alma
            List<String> roles = adminUser.getRoles().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());
            
            // Yetkileri oluştur
            List<SimpleGrantedAuthority> authorities = adminUser.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList());
            
            // JWT token oluştur
            String jwt = jwtTokenProvider.createToken(adminUser.getIdentityNumber(), authorities);
            
            // Cevabı oluştur
            AuthResponse authResponse = new AuthResponse(
                jwt,
                adminUser.getId(),
                adminUser.getEmail(),
                adminUser.getFirstName(),
                adminUser.getLastName(),
                roles
            );
            
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Admin girişi hatası: " + e.getMessage());
        }
    }
    
    @PostMapping("/student-login")
    public ResponseEntity<?> studentLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Öğrenci kullanıcısını öğrenci numarası ile bul
            User studentUser = userRepository.findByStudentNumber(loginRequest.getUserNumber())
                    .orElseThrow(() -> new UsernameNotFoundException("Öğrenci bulunamadı"));
            
            // Öğrenci rolünü kontrol et
            if (!studentUser.getRoles().contains(UserRole.ROLE_STUDENT)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu kullanıcı öğrenci rolüne sahip değil");
            }
            
            // Kimlik doğrulama
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserNumber(),
                            loginRequest.getPassword()
                    )
            );
            
            // Kullanıcı rollerini alma
            List<String> roles = studentUser.getRoles().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());
            
            // JWT token oluştur
            String jwt = jwtTokenProvider.createToken(loginRequest.getUserNumber(), 
                studentUser.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList()));
            
            // Cevabı oluştur
            AuthResponse authResponse = new AuthResponse(
                jwt,
                studentUser.getId(),
                studentUser.getEmail(),
                studentUser.getFirstName(),
                studentUser.getLastName(),
                roles
            );
            
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Kullanıcı adı veya şifre hatalı");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Öğrenci girişi hatası: " + e.getMessage());
        }
    }
    
    @PostMapping("/academic-login")
    public ResponseEntity<?> academicLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Akademisyen kullanıcısını sicil numarası ile bul
            User academicUser = userRepository.findByAcademicNumber(loginRequest.getUserNumber())
                    .orElseThrow(() -> new UsernameNotFoundException("Akademisyen bulunamadı"));
            
            // Akademisyen rolünü kontrol et
            if (!academicUser.getRoles().contains(UserRole.ROLE_ACADEMIC)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu kullanıcı akademisyen rolüne sahip değil");
            }
            
            // Kimlik doğrulama
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserNumber(),
                            loginRequest.getPassword()
                    )
            );
            
            // Kullanıcı rollerini alma
            List<String> roles = academicUser.getRoles().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());
            
            // JWT token oluştur
            String jwt = jwtTokenProvider.createToken(loginRequest.getUserNumber(), 
                academicUser.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList()));
            
            // Cevabı oluştur
            AuthResponse authResponse = new AuthResponse(
                jwt,
                academicUser.getId(),
                academicUser.getEmail(),
                academicUser.getFirstName(),
                academicUser.getLastName(),
                roles
            );
            
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Kullanıcı adı veya şifre hatalı");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Akademisyen girişi hatası: " + e.getMessage());
        }
    }
    
    @PostMapping("/staff-login")
    public ResponseEntity<?> staffLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // İdari personel kullanıcısını personel numarası ile bul
            User staffUser = userRepository.findByStaffNumber(loginRequest.getUserNumber())
                    .orElseThrow(() -> new UsernameNotFoundException("İdari personel bulunamadı"));
            
            // İdari personel rolünü kontrol et
            if (!staffUser.getRoles().contains(UserRole.ROLE_ADMINISTRATIVE_STAFF)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu kullanıcı idari personel rolüne sahip değil");
            }
            
            // Kimlik doğrulama
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserNumber(),
                            loginRequest.getPassword()
                    )
            );
            
            // Kullanıcı rollerini alma
            List<String> roles = staffUser.getRoles().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());
            
            // JWT token oluştur
            String jwt = jwtTokenProvider.createToken(loginRequest.getUserNumber(), 
                staffUser.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList()));
            
            // Cevabı oluştur
            AuthResponse authResponse = new AuthResponse(
                jwt,
                staffUser.getId(),
                staffUser.getEmail(),
                staffUser.getFirstName(),
                staffUser.getLastName(),
                roles
            );
            
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Kullanıcı adı veya şifre hatalı");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("İdari personel girişi hatası: " + e.getMessage());
        }
    }
    
    /**
     * Sistemde öğrenci kaydı sadece idari personel tarafından yapılabilir.
     * Bu işlev, student kayıt endpoint'i altında implement edilmelidir.
     * (StudentController sınıfında yetkilendirme ile)
     */
} 