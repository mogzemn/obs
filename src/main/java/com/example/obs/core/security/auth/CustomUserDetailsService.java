package com.example.obs.core.security.auth;

import com.example.obs.dateAccess.UserRepository;
import com.example.obs.model.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Bu e-posta adresine sahip kullanıcı bulunamadı: " + email));

        List<SimpleGrantedAuthority> authorities;

        // Kullanıcı türüne göre rol ataması
        if (user.isAdmin()) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (user.getAcademic() != null) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ACADEMIC"));
        } else if (user.getStudent() != null) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT"));
        } else if (user.getAdministrativeStaff() != null) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_STAFF"));
        } else {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                true,
                true,
                user.hasLoginPermission(),
                authorities);
    }
}