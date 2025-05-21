package com.example.obs.config;

import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.service.UserService;
import com.example.obs.dataAccess.UserRepository;
import com.example.obs.model.entity.User;
import com.example.obs.model.enums.Role;
import com.example.obs.model.enums.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Autowired      
    public AdminInitializer(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Veritabanı kısıtlamaları güncelleniyor...");
        try {
            StringBuilder rolesBuilder = new StringBuilder();
            for (Role role : Role.values()) {
                rolesBuilder.append("'").append(role.name()).append("',");
            }
            String rolesString = rolesBuilder.substring(0, rolesBuilder.length() - 1);

            String dropConstraintSql = "ALTER TABLE users DROP CONSTRAINT IF EXISTS users_role_check;";
            String addConstraintSql = String.format("ALTER TABLE users ADD CONSTRAINT users_role_check CHECK (role IN (%s));", rolesString);

            jdbcTemplate.execute(dropConstraintSql);
            log.info("users_role_check kısıtlaması kaldırıldı (eğer varsa).");

            jdbcTemplate.execute(addConstraintSql);
            log.info("users_role_check kısıtlaması güncel rollerle yeniden oluşturuldu: {}", rolesString);

        } catch (Exception e) {
            log.error("Veritabanı kısıtlamaları güncellenirken hata oluştu: {}", e.getMessage(), e);
        }
        log.info("Veritabanı kısıtlamaları güncelleme işlemi tamamlandı.");

        String adminEmail = "admin@obs.com";
        String adminPassword = "Admin12345!";

        log.info("AdminInitializer başladı. Admin kullanıcısı başlatılıyor: {}", adminEmail);

        try {
            Optional<User> existingAdminOpt = userRepository.findByEmail(adminEmail);

            if (existingAdminOpt.isEmpty()) {
                log.info("Admin kullanıcısı '{}' bulunamadı. Yeni admin kullanıcısı oluşturuluyor.", adminEmail);
                UserCreateRequest adminUserRequest = new UserCreateRequest();
                adminUserRequest.setFirstName("Admin");
                adminUserRequest.setLastName("User");
                adminUserRequest.setEmail(adminEmail);
                adminUserRequest.setPassword(adminPassword);
                adminUserRequest.setIdentityNumber("11111111111");
                adminUserRequest.setPhone("+905555555555");
                adminUserRequest.setBirthDate(LocalDate.of(1990, 1, 1));
                adminUserRequest.setStatus(UserStatus.ACTIVE);
                adminUserRequest.setIsActive(true);
                adminUserRequest.setHasLoginPermission(true);
                adminUserRequest.setIsAdmin(true);
                adminUserRequest.setRole(com.example.obs.model.enums.Role.ROLE_ADMIN);

                userService.add(adminUserRequest);
                log.info("Yeni admin kullanıcısı '{}' başarıyla oluşturuldu.", adminEmail);
            } else {
                log.info("Admin kullanıcısı '{}' bulundu. Şifre ve detaylar güncelleniyor.", adminEmail);
                User existingAdmin = existingAdminOpt.get();
                
                String newEncodedPassword = passwordEncoder.encode(adminPassword);
                log.info("'{}' admin kullanıcısı için '{}' şifresi hashleniyor. Yeni hashlenmiş şifre: {}", adminEmail, adminPassword, newEncodedPassword);
                
                existingAdmin.setPassword(newEncodedPassword);
                existingAdmin.setRole(com.example.obs.model.enums.Role.ROLE_ADMIN);
                existingAdmin.setIsAdmin(true);
                existingAdmin.setIsActive(true);
                existingAdmin.setHasLoginPermission(true);
                existingAdmin.setStatus(UserStatus.ACTIVE);
                
                if (existingAdmin.getPhone() == null || !existingAdmin.getPhone().startsWith("+90")) {
                    existingAdmin.setPhone("+905555555555");
                    log.info("Admin kullanıcısı '{}' için telefon numarası geçerli formata güncellendi: {}", adminEmail, existingAdmin.getPhone());
                }
                
                if (existingAdmin.getBirthDate() == null) {
                    existingAdmin.setBirthDate(LocalDate.of(1990, 1, 1));
                    log.info("Admin kullanıcısı '{}' için null olan doğum tarihi varsayılan olarak ayarlandı: {}", adminEmail, existingAdmin.getBirthDate());
                }
                
                log.info("Güncellenmiş admin kullanıcısı '{}' kaydedilmeye çalışılıyor...", adminEmail);
                userRepository.save(existingAdmin);
                log.info("Admin kullanıcısı '{}' kaydedildi. Şifre güncellemesi doğrulanıyor...", adminEmail);

                Optional<User> updatedAdminOpt = userRepository.findByEmail(adminEmail);
                if (updatedAdminOpt.isPresent()) {
                    User updatedAdmin = updatedAdminOpt.get();
                    log.info("Güncelleme sonrası '{}' admin kullanıcısı çekildi. Veritabanındaki mevcut hashlenmiş şifre: {}", adminEmail, updatedAdmin.getPassword());
                    if (passwordEncoder.matches(adminPassword, updatedAdmin.getPassword())) {
                        log.info("Doğrulama başarılı: '{}' admin kullanıcısının şifresi yeni şifreyle eşleşiyor.", adminEmail);
                    } else {
                        log.error("DOĞRULAMA BAŞARISIZ: '{}' admin kullanıcısının şifresi güncelleme sonrası YENİ ŞİFREYLE EŞLEŞMİYOR. Kayıtlı hash: {}", adminEmail, updatedAdmin.getPassword());
                    }
                } else {
                    log.error("KRİTİK HATA: '{}' admin kullanıcısı güncelleme sonrası tekrar çekilemedi.", adminEmail);
                }
                log.info("Admin kullanıcısı '{}' detayları başarıyla güncellendi/doğrulandı.", adminEmail);
            }
        } catch (Exception e) {
            log.error("'{}' admin kullanıcısının başlatılması sırasında hata oluştu: {}", adminEmail, e.getMessage(), e);
            throw e;
        }
        log.info("AdminInitializer, '{}' admin kullanıcısı için tamamlandı.", adminEmail);
    }
}
