package com.example.obs.webApi.controllers;

import com.example.obs.business.service.AuthService;
import com.example.obs.core.exceptions.AuthorizationException;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.security.dto.request.*;
import com.example.obs.core.security.dto.response.AuthResponse;
import com.example.obs.core.utilities.results.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Genel login isteği alındı. Kullanıcı Tipi: {}, Kullanıcı Adı: {}", 
                loginRequest.getUserType(), loginRequest.getUsername());
        
        try {
        AuthResponse authResponse = authService.authenticate(loginRequest);
        
        String userType = loginRequest.getUserType().toUpperCase();
        String successMessage;
        
        switch (userType) {
            case "STUDENT":
                successMessage = "Öğrenci girişi başarılı";
                break;
            case "ACADEMIC":
            case "DEPARTMENT_CHAIR":
                successMessage = "Akademik personel girişi başarılı";
                break;
            case "ADMINISTRATIVE":
            case "ADMINISTRATIVE_MANAGER":
            case "STUDENT_AFFAIRS_STAFF":
            case "ACADEMIC_AFFAIRS_STAFF":
                successMessage = "İdari personel girişi başarılı";
                break;
            case "ADMIN":
                successMessage = "Yönetici girişi başarılı";
                break;
            default:
                successMessage = "Giriş başarılı";
        }
        
            log.info("Giriş başarılı. Kullanıcı Tipi: {}, Kullanıcı Adı: {}", 
                    loginRequest.getUserType(), loginRequest.getUsername());
        return ResponseEntity.ok(ApiResponse.success(authResponse, successMessage));
        } catch (NotFoundException | AuthorizationException | BadCredentialsException e) {
            log.warn("Giriş hatası. Kullanıcı Tipi: {}, Kullanıcı Adı: {}, Hata: {}", 
                    loginRequest.getUserType(), loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (BusinessException e) {
            log.warn("İş kuralı hatası. Kullanıcı Tipi: {}, Kullanıcı Adı: {}, Hata: {}", 
                    loginRequest.getUserType(), loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Beklenmeyen hata. Kullanıcı Tipi: {}, Kullanıcı Adı: {}, Hata: {}", 
                    loginRequest.getUserType(), loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Giriş sırasında beklenmeyen bir hata oluştu"));
        }
    }
    

    @PostMapping("/login/student")
    public ResponseEntity<ApiResponse<AuthResponse>> studentLogin(@Valid @RequestBody StudentLoginRequest request) {
        log.info("Öğrenci login isteği alındı. Kullanıcı Adı: {}", request.getUsername());
        try {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());
        loginRequest.setUserType("STUDENT");
        
        AuthResponse authResponse = authService.authenticate(loginRequest);
            log.info("Öğrenci girişi başarılı. Kullanıcı Adı: {}", request.getUsername());
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Öğrenci girişi başarılı"));
        } catch (NotFoundException | AuthorizationException | BadCredentialsException e) {
            log.warn("Öğrenci girişi hatası. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (BusinessException e) {
            log.warn("Öğrenci girişi iş kuralı hatası. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Öğrenci girişi beklenmeyen hata. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Giriş sırasında beklenmeyen bir hata oluştu"));
        }
    }
    

    @PostMapping("/login/academic")
    public ResponseEntity<ApiResponse<AuthResponse>> academicLogin(@Valid @RequestBody AcademicLoginRequest request) {
        log.info("Akademisyen login isteği alındı. Kullanıcı Adı: {}", request.getUsername());
        try {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());
        loginRequest.setUserType("ACADEMIC");
        
        AuthResponse authResponse = authService.authenticate(loginRequest);
            log.info("Akademisyen girişi başarılı. Kullanıcı Adı: {}", request.getUsername());
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Akademik personel girişi başarılı"));
        } catch (NotFoundException | AuthorizationException | BadCredentialsException e) {
            log.warn("Akademisyen girişi hatası. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (BusinessException e) {
            log.warn("Akademisyen girişi iş kuralı hatası. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Akademisyen girişi beklenmeyen hata. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Giriş sırasında beklenmeyen bir hata oluştu"));
        }
    }
    

    @PostMapping("/login/administrative")
    public ResponseEntity<ApiResponse<AuthResponse>> administrativeLogin(@Valid @RequestBody AdministrativeLoginRequest request) {
        log.info("İdari personel login isteği alındı. Kullanıcı Adı: {}", request.getUsername());
        try {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());
        loginRequest.setUserType("ADMINISTRATIVE");
        
        AuthResponse authResponse = authService.authenticate(loginRequest);
            log.info("İdari personel girişi başarılı. Kullanıcı Adı: {}", request.getUsername());
        return ResponseEntity.ok(ApiResponse.success(authResponse, "İdari personel girişi başarılı"));
        } catch (NotFoundException | AuthorizationException | BadCredentialsException e) {
            log.warn("İdari personel girişi hatası. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (BusinessException e) {
            log.warn("İdari personel girişi iş kuralı hatası. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("İdari personel girişi beklenmeyen hata. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Giriş sırasında beklenmeyen bir hata oluştu"));
        }
    }
    

    @PostMapping("/login/admin") 
    public ResponseEntity<ApiResponse<AuthResponse>> adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        log.info("Admin login isteği alındı. Kullanıcı Adı: {}", request.getUsername());
        try {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());
        loginRequest.setUserType("ADMIN");
        
        AuthResponse authResponse = authService.authenticate(loginRequest);
            log.info("Admin girişi başarılı. Kullanıcı Adı: {}", request.getUsername());
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Yönetici girişi başarılı"));
        } catch (NotFoundException | AuthorizationException | BadCredentialsException e) {
            log.warn("Admin girişi hatası. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (BusinessException e) {
            log.warn("Admin girişi iş kuralı hatası. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Admin girişi beklenmeyen hata. Kullanıcı Adı: {}, Hata: {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Giriş sırasında beklenmeyen bir hata oluştu"));
        }
    }

    @PostMapping("/refresh")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Token yenileme isteği alındı");
        try {
        AuthResponse authResponse = authService.refreshToken(refreshTokenRequest);
            log.info("Token yenileme başarılı");
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Token yenileme başarılı"));
        } catch (NotFoundException | AuthorizationException e) {
            log.warn("Token yenileme hatası: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (BusinessException e) {
            log.warn("Token yenileme iş kuralı hatası: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Token yenileme beklenmeyen hata: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Token yenileme sırasında beklenmeyen bir hata oluştu"));
        }
    }
}
