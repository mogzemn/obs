package com.example.obs.core.config;

import com.example.obs.dateAccess.*;
import com.example.obs.model.entity.*;
import com.example.obs.model.enums.UserRole;
import com.example.obs.model.enums.UserStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Uygulama başlangıcında test verilerini oluşturan sınıf
 * Bu sınıf sadece "dev" profilinde çalışır
 */
@Configuration
@Profile("dev") // Sadece dev profilinde çalışması için
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository, 
            FacultyRepository facultyRepository,
            DepartmentRepository departmentRepository,
            StudentRepository studentRepository,
            AcademicRepository academicRepository,
            AdministrativeStaffRepository administrativeStaffRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            // Veritabanı boş değilse çıkış yap
            if (userRepository.count() > 0) {
                System.out.println("Veritabanında zaten kullanıcılar var, test verileri oluşturulmayacak.");
                return;
            }
            
            System.out.println("Test verileri oluşturuluyor...");
            
            // 1. Fakülte oluştur
            Faculty faculty = new Faculty();
            faculty.setFacultyName("Mühendislik Fakültesi");
            faculty.setFacultyCode("MF");
            faculty.setNumericCode(1);
            faculty.setIsActive(true);
            facultyRepository.save(faculty);

            // 2. Bölüm oluştur
            Department department = new Department();
            department.setDepartmentName("Bilgisayar Mühendisliği");
            department.setDepartmentCode("BM");
            department.setNumericCode(1);
            department.setFaculty(faculty);
            department.setIsActive(true);
            departmentRepository.save(department);
            
            // 3. Admin oluştur
            User adminUser = new User();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setIdentityNumber("11111111111");
            adminUser.setBirthDate(LocalDate.of(1990, 1, 1));
            adminUser.setPhone("5551112233");
            adminUser.setStatus(UserStatus.ACTIVE);
            adminUser.setIsActive(true);
            adminUser.setHasLoginPermission(true);
            adminUser.setIsAdmin(true);
            adminUser.setCreatedAt(LocalDateTime.now());
            
            Set<UserRole> adminRoles = new HashSet<>();
            adminRoles.add(UserRole.ROLE_ADMIN);
            adminUser.setRoles(adminRoles);
            
            userRepository.save(adminUser);
            
            // 4. İdari personel oluştur
            User staffUser = new User();
            staffUser.setFirstName("İdari");
            staffUser.setLastName("Personel");
            staffUser.setEmail("personel@example.com");
            staffUser.setPassword(passwordEncoder.encode("personel123"));
            staffUser.setIdentityNumber("22222222222");
            staffUser.setBirthDate(LocalDate.of(1985, 5, 15));
            staffUser.setPhone("5552223344");
            staffUser.setStatus(UserStatus.ACTIVE);
            staffUser.setIsActive(true);
            staffUser.setHasLoginPermission(true);
            staffUser.setIsAdmin(false);
            staffUser.setCreatedAt(LocalDateTime.now());
            
            Set<UserRole> staffRoles = new HashSet<>();
            staffRoles.add(UserRole.ROLE_ADMINISTRATIVE_STAFF);
            staffUser.setRoles(staffRoles);
            
            userRepository.save(staffUser);
            
            AdministrativeStaff staff = new AdministrativeStaff();
            staff.setUser(staffUser);
            staff.setStaffNumber("P240201");
            staff.setJobTitle("Öğrenci İşleri Uzmanı");
            
            administrativeStaffRepository.save(staff);
            
            // 5. Akademisyen oluştur
            User academicUser = new User();
            academicUser.setFirstName("Öğretim");
            academicUser.setLastName("Üyesi");
            academicUser.setEmail("akademik@example.com");
            academicUser.setPassword(passwordEncoder.encode("akademik123"));
            academicUser.setIdentityNumber("33333333333");
            academicUser.setBirthDate(LocalDate.of(1980, 3, 10));
            academicUser.setPhone("5553334455");
            academicUser.setStatus(UserStatus.ACTIVE);
            academicUser.setIsActive(true);
            academicUser.setHasLoginPermission(true);
            academicUser.setIsAdmin(false);
            academicUser.setCreatedAt(LocalDateTime.now());
            
            Set<UserRole> academicRoles = new HashSet<>();
            academicRoles.add(UserRole.ROLE_ACADEMIC);
            academicUser.setRoles(academicRoles);
            
            userRepository.save(academicUser);
            
            Academic academic = new Academic();
            academic.setUser(academicUser);
            academic.setDepartment(department);
            academic.setRegistrationNumber("A240101");
            
            academicRepository.save(academic);
            
            // 6. Öğrenci oluştur
            User studentUser = new User();
            studentUser.setFirstName("Öğrenci");
            studentUser.setLastName("Kullanıcı");
            studentUser.setEmail("ogrenci@example.com");
            studentUser.setPassword(passwordEncoder.encode("ogrenci123"));
            studentUser.setIdentityNumber("44444444444");
            studentUser.setBirthDate(LocalDate.of(2000, 7, 20));
            studentUser.setPhone("5554445566");
            studentUser.setStatus(UserStatus.ACTIVE);
            studentUser.setIsActive(true);
            studentUser.setHasLoginPermission(true);
            studentUser.setIsAdmin(false);
            studentUser.setCreatedAt(LocalDateTime.now());
            
            Set<UserRole> studentRoles = new HashSet<>();
            studentRoles.add(UserRole.ROLE_STUDENT);
            studentUser.setRoles(studentRoles);
            
            userRepository.save(studentUser);
            
            Student student = new Student();
            student.setUser(studentUser);
            student.setDepartment(department);
            student.setStudentNumber("240101001");
            student.setAdvisor(academic);
            
            studentRepository.save(student);
            
            System.out.println("Test verileri başarıyla oluşturuldu!");
        };
    }
} 