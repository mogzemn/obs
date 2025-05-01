package com.example.obs.core.security;

import com.example.obs.dateAccess.UserRepository;
import com.example.obs.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userNumber) throws UsernameNotFoundException {
        // Username olarak gelen değer şimdi kullanıcı numarası olacak
        User user = userRepository.findByUserNumber(userNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + userNumber));

        // Aktif olmayan kullanıcılar giriş yapamaz
        if (!user.getIsActive() || !user.getHasLoginPermission()) {
            throw new UsernameNotFoundException("Kullanıcı aktif değil veya giriş yetkisi yok: " + userNumber);
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                getUserIdentifier(user),  // Kullanıcı kimliği olarak numarasını kullanıyoruz
                user.getPassword(),
                user.getIsActive(),
                true, // hesap süre sonu kontrolü için bu değer değiştirilebilir
                true, // kimlik bilgileri süre sonu kontrolü için bu değer değiştirilebilir
                user.getHasLoginPermission(),
                authorities
        );
    }
    
    // Kullanıcı tipine göre doğru kullanıcı numarasını döndür
    private String getUserIdentifier(User user) {
        if (user.getStudent() != null) {
            return user.getStudent().getStudentNumber();
        } else if (user.getAcademic() != null) {
            return user.getAcademic().getRegistrationNumber();
        } else if (user.getAdministrativeStaff() != null) {
            return user.getAdministrativeStaff().getStaffNumber();
        } else {
            // Eğer hiçbir özel tip yoksa, kimlik numarasını kullan
            return user.getIdentityNumber();
        }
    }
} 