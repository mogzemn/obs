package com.example.obs.core.utilities.migration;

import com.example.obs.dataAccess.UserRepository;
import com.example.obs.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasswordMigrationService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[ayb]\\$.{56}$");

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Şifre migrasyon servisi çalışıyor...");
        
        List<User> allUsers = userRepository.findAll();
        int migratedCount = 0;
        
        for (User user : allUsers) {
            if (user.getPassword() != null && !isBCryptEncoded(user.getPassword())) {
                String plainPassword = user.getPassword();
                String encodedPassword = passwordEncoder.encode(plainPassword);
                
                user.setPassword(encodedPassword);
                userRepository.save(user);
                
                migratedCount++;
                log.info("Kullanıcı şifresi şifrelendi: {}", user.getEmail());
            }
        }
        
        log.info("Şifre migrasyonu tamamlandı. Toplam {} şifre şifrelendi.", migratedCount);
    }
    
    private boolean isBCryptEncoded(String password) {
        return BCRYPT_PATTERN.matcher(password).matches();
    }
}
