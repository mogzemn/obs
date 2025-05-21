package com.example.obs.core.security.service;

import com.example.obs.dataAccess.UserRepository;
import com.example.obs.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BruteForceProtectionService {

    @Value("${spring.security.max-failed-attempts:5}")
    private int maxFailedAttempts;

    private final UserRepository userRepository;
    
    @Transactional
    public boolean registerFailedLogin(String username) {
        Optional<User> userOpt = userRepository.findByEmail(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            int failedAttempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(failedAttempts);
            
            if (failedAttempts >= maxFailedAttempts) {
                lockAccount(user);
                return true;
            }
            
            userRepository.save(user);
        }
        
        return false;
    }
    
    @Transactional
    public void resetFailedAttempts(String username) {
        Optional<User> userOpt = userRepository.findByEmail(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFailedLoginAttempts(0);
            user.setAccountLocked(false);
            user.setLockTime(null);
            userRepository.save(user);
        }
    }
    
    private void lockAccount(User user) {
        user.setAccountLocked(true);
        user.setLockTime(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public boolean isAccountLocked(String username) {
        Optional<User> userOpt = userRepository.findByEmail(username);
        return userOpt.map(User::getAccountLocked).orElse(false);
    }
}
