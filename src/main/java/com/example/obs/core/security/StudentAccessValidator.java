package com.example.obs.core.security;

import com.example.obs.dateAccess.StudentRepository;
import com.example.obs.dateAccess.UserRepository;
import com.example.obs.model.entity.Student;
import com.example.obs.model.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Öğrencinin kendi bilgilerine erişimini denetleyen sınıf.
 * Bu sınıf, bir öğrencinin sadece kendi bilgilerine erişmesini sağlar.
 */
@Component
public class StudentAccessValidator {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentAccessValidator(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Kimliği doğrulanmış kullanıcının istenen öğrenci kaydına erişim yetkisi olup olmadığını kontrol eder.
     * 
     * @param authentication Kimliği doğrulanmış kullanıcı
     * @param studentId Erişilmek istenen öğrenci ID'si
     * @return Erişim izni varsa true, yoksa false
     */
    public boolean checkStudentAccess(Authentication authentication, Long studentId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String userNumber = authentication.getName();
        
        // Kullanıcı numarasından User objesi bulunuyor
        User user = userRepository.findByUserNumber(userNumber).orElse(null);
        if (user == null) {
            return false;
        }
        
        // ROLE_STUDENT rolüne sahip ve istenen öğrenci ID'sine sahip mi kontrol ediliyor
        if (user.getRoles().stream().anyMatch(role -> role.name().equals("ROLE_STUDENT"))) {
            Student student = user.getStudent();
            if (student != null) {
                return student.getId().equals(studentId);
            }
        }
        
        return false;
    }
} 