package com.example.obs.business.service.implementation;

import com.example.obs.business.service.AuthService;
import com.example.obs.core.exceptions.AuthorizationException;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.security.dto.request.LoginRequest;
import com.example.obs.core.security.dto.request.RefreshTokenRequest;
import com.example.obs.core.security.dto.response.AuthResponse;
import com.example.obs.core.security.service.BruteForceProtectionService;
import com.example.obs.core.security.util.JwtUtil;
import com.example.obs.dataAccess.AcademicRepository;
import com.example.obs.dataAccess.AdministrativeStaffRepository;
import com.example.obs.dataAccess.StudentRepository;
import com.example.obs.dataAccess.UserRepository;
import com.example.obs.model.entity.Academic;
import com.example.obs.model.entity.AdministrativeStaff;
import com.example.obs.model.entity.Student;
import com.example.obs.model.entity.User;
import com.example.obs.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final StudentRepository studentRepository;
    private final AcademicRepository academicRepository;
    private final AdministrativeStaffRepository adminStaffRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final BruteForceProtectionService bruteForceProtectionService;

    @Override
    public AuthResponse authenticate(LoginRequest request) {
        log.info("AuthService.authenticate çağrıldı. Kullanıcı Tipi: {}", request.getUserType());
        if (request == null) {
            log.error("Giriş isteği null olarak geldi.");
            throw new BusinessException("Geçersiz istek: İstek boş olamaz");
        }
        
        final String userType = request.getUserType();
        final String username = request.getUsername();
        final String password = request.getPassword();
        
        if (userType == null || userType.isEmpty()) {
            log.error("Kullanıcı tipi belirtilmedi.");
            throw new BusinessException("Kullanıcı tipi belirtilmelidir. Geçerli tipler: STUDENT, ACADEMIC, ADMINISTRATIVE, ADMIN");
        }
        
        if (username == null || username.isEmpty()) {
            log.error("Kullanıcı adı belirtilmedi.");
            throw new BusinessException("Kullanıcı adı/numara/email belirtilmelidir");
        }
        
        if (password == null || password.isEmpty()) {
            log.error("Şifre belirtilmedi.");
            throw new BusinessException("Şifre belirtilmelidir");
        }
        
        User user;
        String responseUserType = userType;
        
        try {
            switch (userType.toUpperCase()) {
                case "STUDENT":
                    user = authenticateStudentUser(username, password);
                    break;
                    
                case "ACADEMIC":
                case "DEPARTMENT_CHAIR":
                    user = authenticateAcademicUser(username, password);
                    if (user.getRole() == Role.DEPARTMENT_CHAIR) {
                        responseUserType = "DEPARTMENT_CHAIR";
                    } else {
                        responseUserType = "ACADEMIC";
                    }
                    break;
                    
                case "ADMINISTRATIVE":
                case "ADMINISTRATIVE_MANAGER":
                case "STUDENT_AFFAIRS_STAFF":
                case "ACADEMIC_AFFAIRS_STAFF":
                    user = authenticateAdministrativeUser(username, password);
                    if (user.getRole() == Role.ADMINISTRATIVE_MANAGER) {
                        responseUserType = Role.ADMINISTRATIVE_MANAGER.name();
                    } else if (user.getRole() == Role.STUDENT_AFFAIRS_STAFF) {
                        responseUserType = Role.STUDENT_AFFAIRS_STAFF.name();
                    } else if (user.getRole() == Role.ACADEMIC_AFFAIRS_STAFF) {
                        responseUserType = Role.ACADEMIC_AFFAIRS_STAFF.name();
                    }
                    break;
                    
                case "ADMIN":
                    log.info("Admin kullanıcı tipi için authenticateAdminUser çağrılıyor. E-posta: {}", username);
                    user = authenticateAdminUser(username, password);
                    responseUserType = "ADMIN";
                    log.info("authenticateAdminUser başarıyla tamamlandı. User ID: {}", user.getId());
                    break;
                    
                default:
                    log.error("Geçersiz kullanıcı tipi: {}", userType);
                    throw new BusinessException("Geçersiz kullanıcı tipi: " + userType + ". Geçerli tipler: STUDENT, ACADEMIC, ADMINISTRATIVE, ADMIN");
            }
            
            log.info("Kullanıcı başarıyla doğrulandı: userType={}, username={}, userId={}", userType, username, user.getId());
            return createAuthResponse(user, responseUserType);
        } catch (AuthorizationException | NotFoundException | BadCredentialsException e) {
            log.error("Kimlik doğrulama hatası: userType={}, username={}, error={}", userType, username, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Beklenmeyen kimlik doğrulama hatası: userType={}, username={}", userType, username, e);
            throw new BusinessException("Kimlik doğrulama sırasında beklenmeyen bir hata oluştu: " + e.getMessage());
        }
    }

    private User authenticateStudentUser(String studentNumber, String password) {
        final String formattedStudentNumber = studentNumber.toLowerCase();
        
        Student student = studentRepository.findByStudentNumber(formattedStudentNumber)
                .orElseThrow(() -> new NotFoundException("Geçersiz öğrenci numarası: " + formattedStudentNumber + ". Lütfen öğrenci giriş ekranını kullandığınızdan emin olun."));

        User user = student.getUser();

        if (user.getRole() != Role.STUDENT) {
            throw new AuthorizationException("Bu kullanıcı öğrenci rolüne sahip değil. Lütfen doğru giriş ekranını kullanın.");
        }

        authenticateWithBruteForceCheck(user.getEmail(), password);
        
        return user;
    }

    private User authenticateAcademicUser(String academicNumber, String password) {
        final String formattedAcademicNumber = academicNumber.toUpperCase();
        
        Academic academic = academicRepository.findByAcademicNumber(formattedAcademicNumber)
                .orElseThrow(() -> new NotFoundException("Geçersiz akademisyen numarası: " + formattedAcademicNumber + ". Lütfen akademisyen giriş ekranını kullandığınızdan emin olun."));

        User user = academic.getUser();

        if (user.getRole() != Role.ACADEMIC && user.getRole() != Role.DEPARTMENT_CHAIR) {
            throw new AuthorizationException("Bu kullanıcı akademisyen veya bölüm başkanı rolüne sahip değil. Lütfen doğru giriş ekranını kullanın.");
        }

        authenticateWithBruteForceCheck(user.getEmail(), password);
        
        return user;
    }

    private User authenticateAdministrativeUser(String staffNumber, String password) {
        final String formattedStaffNumber = staffNumber.toUpperCase();
        
        AdministrativeStaff staff = adminStaffRepository.findByStaffNumber(formattedStaffNumber)
                .orElseThrow(() -> new NotFoundException("Geçersiz idari personel numarası: " + formattedStaffNumber + ". Lütfen idari personel giriş ekranını kullandığınızdan emin olun."));

        User user = staff.getUser();

        if (user.getRole() != Role.ADMINISTRATIVE_MANAGER && 
            user.getRole() != Role.STUDENT_AFFAIRS_STAFF && 
            user.getRole() != Role.ACADEMIC_AFFAIRS_STAFF) {
            throw new AuthorizationException("Bu kullanıcı idari personel girişi için uygun role sahip değil. Lütfen doğru giriş ekranını kullanın.");
        }

        authenticateWithBruteForceCheck(user.getEmail(), password);
        
        return user;
    }

    private User authenticateAdminUser(String email, String password) {
        log.info("authenticateAdminUser başlatıldı. E-posta: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Admin kullanıcısı bulunamadı. E-posta: {}", email);
                    return new NotFoundException("Geçersiz e-posta adresi: " + email + ". Lütfen admin giriş ekranını kullandığınızdan emin olun.");
                });
        log.info("Admin kullanıcısı bulundu. User ID: {}, Role: {}, isAdmin: {}", user.getId(), user.getRole(), user.getIsAdmin());
        log.info("UserDetails kontrolleri: isEnabled={}, isAccountNonLocked={}, isAccountNonExpired={}, isCredentialsNonExpired={}",
                user.isEnabled(), user.isAccountNonLocked(), user.isAccountNonExpired(), user.isCredentialsNonExpired());

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.ROLE_ADMIN && !user.getIsAdmin()) {
            log.warn("Admin yetki kontrolü başarısız. User ID: {}, Role: {}, isAdmin: {}", user.getId(), user.getRole(), user.getIsAdmin());
            throw new AuthorizationException("Bu kullanıcı admin yetkisine sahip değil. Lütfen doğru giriş ekranını kullanın.");
        }
        log.info("Admin yetki kontrolü başarılı. User ID: {}", user.getId());

        authenticateWithBruteForceCheck(user.getEmail(), password);
        log.info("authenticateWithBruteForceCheck tamamlandı (admin için). User ID: {}", user.getId());
        return user;
    }
    
    private void authenticateWithBruteForceCheck(String email, String password) {
        log.info("authenticateWithBruteForceCheck başlatıldı. E-posta: {}", email);
        if (bruteForceProtectionService.isAccountLocked(email)) {
            log.warn("Hesap kilitli. E-posta: {}", email);
            throw new AuthorizationException("Hesabınız kilitlendi. Lütfen daha sonra tekrar deneyin veya yönetici ile iletişime geçin.");
        }
        log.info("Hesap kilitli değil. E-posta: {}", email);

        try {
            log.info("AuthenticationManager.authenticate çağrılıyor. E-posta: {}", email);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            log.info("AuthenticationManager.authenticate başarılı. E-posta: {}", email);
            bruteForceProtectionService.resetFailedAttempts(email);
        } catch (BadCredentialsException e) {
            log.warn("BadCredentialsException yakalandı. E-posta: {}", email, e);
            boolean locked = bruteForceProtectionService.registerFailedLogin(email);
            if (locked) {
                log.warn("Çok fazla başarısız giriş denemesi sonucu hesap kilitlendi. E-posta: {}", email);
                throw new AuthorizationException("Çok fazla başarısız giriş denemesi. Hesabınız kilitlendi.");
            }
            throw new AuthorizationException("Geçersiz şifre");
        } catch (Exception e) {
            log.error("AuthenticationManager.authenticate sırasında beklenmedik bir hata oluştu. E-posta: {}", email, e);
            throw new AuthorizationException("Kimlik doğrulama sırasında bir hata oluştu: " + e.getMessage());
        }
        log.info("authenticateWithBruteForceCheck tamamlandı. E-posta: {}", email);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String userEmail = jwtUtil.extractUsername(request.getRefreshToken());
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Kullanıcı bulunamadı: " + userEmail));

        if (!jwtUtil.validateToken(request.getRefreshToken(), user)) {
            throw new AuthorizationException("Geçersiz yenileme token'ı");
        }

        String userType;
        if (user.getStudent() != null) {
            userType = "STUDENT";
        } else if (user.getAcademic() != null) {
            userType = "ACADEMIC";
        } else if (user.getAdministrativeStaff() != null) {
            userType = "ADMINISTRATIVE";
        } else {
            userType = "UNKNOWN";
        }
        if (user.getRole() == Role.ADMIN || user.getRole() == Role.ROLE_ADMIN) {
            userType = "ADMIN";
        }

        return createAuthResponse(user, userType);
    }

    private AuthResponse createAuthResponse(User user, String userType) {
        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userType(userType)
                .fullName(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole().name())
                .build();
    }
}
