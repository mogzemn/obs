package com.example.obs.business.service.implementation;

import com.example.obs.business.service.StudentService;
import com.example.obs.business.requests.StudentCreateRequest;
import com.example.obs.business.requests.StudentUpdateRequest;
import com.example.obs.business.responses.StudentResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.core.utilities.numbergeneration.StudentNumberGenerator;
import com.example.obs.dataAccess.AcademicRepository;
import com.example.obs.dataAccess.DepartmentRepository;
import com.example.obs.dataAccess.StudentRepository;
import com.example.obs.dataAccess.UserRepository;
import com.example.obs.model.entity.Academic;
import com.example.obs.model.entity.Department;
import com.example.obs.model.entity.Student;
import com.example.obs.model.entity.User;
import com.example.obs.business.requests.UserUpdateRequest;
import com.example.obs.model.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapperService modelMapperService;
    private final StudentNumberGenerator studentNumberGenerator;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository; 
    private final AcademicRepository academicRepository;    

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return this.studentRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Öğrenci bilgisi bulunamadı! ID: " + id));
    }

    @Override
    @Transactional
    public void add(StudentCreateRequest studentCreateRequest) {
        try {
            if (studentCreateRequest.getUser() == null) {
                throw new BusinessException("Kullanıcı bilgileri (user) alanı boş olamaz.");
            }
            if (studentCreateRequest.getUser().getBirthDate() == null) {
                throw new BusinessException("Doğum tarihi (user.birthDate) alanı boş olamaz. Lütfen geçerli bir doğum tarihi girin.");
            }
            
            if (studentCreateRequest.getUser().getEmail() == null || studentCreateRequest.getUser().getEmail().isEmpty()) {
                throw new BusinessException("E-posta adresi boş olamaz.");
            }
            
            String requestEmail = studentCreateRequest.getUser().getEmail();
            userRepository.findByEmail(requestEmail)
                .ifPresent(existingUser -> {
                    if (!existingUser.getIdentityNumber().equals(studentCreateRequest.getUser().getIdentityNumber())) {
                        throw new BusinessException("Bu e-posta adresi zaten kullanılıyor: " + requestEmail);
                    }
                });

            studentCreateRequest.getUser().setRole(Role.STUDENT);

            User userToSave = this.modelMapperService.forRequest()
                    .map(studentCreateRequest.getUser(), User.class);
            
            if (userToSave.getBirthDate() == null && studentCreateRequest.getUser().getBirthDate() != null) {
                userToSave.setBirthDate(studentCreateRequest.getUser().getBirthDate());
            }
            
            userToSave = userRepository.save(userToSave);
            
            Student student = new Student();
            
            student.setUser(userToSave);
            
            if (studentCreateRequest.getDepartmentId() != null) {
                Department department = departmentRepository.findById(studentCreateRequest.getDepartmentId())
                        .orElseThrow(() -> new NotFoundException("Departman bulunamadı: ID " + studentCreateRequest.getDepartmentId()));
                student.setDepartment(department);
            } else {
                throw new BusinessException("Öğrenci için departman ID'si belirtilmelidir.");
            }

            String studentNumber = studentNumberGenerator.generateStudentNumber(
                    studentCreateRequest.getDepartmentId());
            student.setStudentNumber(studentNumber);

            studentRepository.save(student);
        } catch (Exception ex) {
            throw new BusinessException("Öğrenci ekleme işlemi sırasında bir hata oluştu: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public void update(StudentUpdateRequest studentUpdateRequest) {
        try {
            Student existingStudent = studentRepository.findById(studentUpdateRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Güncelleme yapılamadı. ID: "
                            + studentUpdateRequest.getId() + " ile eşleşen bir öğrenci bulunamadı."));
            
            if (studentUpdateRequest.getUser() != null && studentUpdateRequest.getUser().getEmail() != null) {
                String newEmail = studentUpdateRequest.getUser().getEmail();
                String existingEmail = existingStudent.getUser().getEmail();
                
                if (!existingEmail.equals(newEmail)) {
                    Optional<User> existingUserWithSameEmail = userRepository.findByEmail(newEmail);
                    if (existingUserWithSameEmail.isPresent() && 
                        !existingUserWithSameEmail.get().getId().equals(existingStudent.getUser().getId())) {
                        throw new BusinessException("Bu e-posta adresi zaten kullanılıyor: " + newEmail);
                    }
                }
            }
            
            if (studentUpdateRequest.getDepartmentId() != null) {
                Department department = departmentRepository.findById(studentUpdateRequest.getDepartmentId())
                        .orElseThrow(() -> new NotFoundException("Departman bulunamadı: ID " + studentUpdateRequest.getDepartmentId()));
                existingStudent.setDepartment(department);
            }
            
            if (studentUpdateRequest.getAdvisorId() != null) {
                Academic advisor = academicRepository.findById(studentUpdateRequest.getAdvisorId())
                        .orElseThrow(() -> new NotFoundException("Danışman akademisyen bulunamadı: ID " + studentUpdateRequest.getAdvisorId()));
                existingStudent.setAdvisor(advisor);
            } else { 
                if (existingStudent.getAdvisor() != null) {
                    existingStudent.setAdvisor(null);
                }
            }
            
            if (studentUpdateRequest.getUser() != null) {
                User existingUser = existingStudent.getUser();
                if (existingUser != null) {
                    updateUserFields(existingUser, studentUpdateRequest.getUser());
                    userRepository.save(existingUser);
                }
            }
            
            studentRepository.save(existingStudent);
            
        } catch (Exception ex) {
            throw new BusinessException("Öğrenci güncelleme işlemi sırasında bir hata oluştu: " + ex.getMessage());
        }
    }
    
    private void updateUserFields(User existingUser, UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest.getFirstName() != null) {
            existingUser.setFirstName(userUpdateRequest.getFirstName());
        }
        if (userUpdateRequest.getLastName() != null) {
            existingUser.setLastName(userUpdateRequest.getLastName());
        }
        if (userUpdateRequest.getBirthDate() != null) {
            existingUser.setBirthDate(userUpdateRequest.getBirthDate());
        }
        if (userUpdateRequest.getPhone() != null) {
            existingUser.setPhone(userUpdateRequest.getPhone());
        }
        if (userUpdateRequest.getEmail() != null) {
            existingUser.setEmail(userUpdateRequest.getEmail());
        }
        if (userUpdateRequest.getStatus() != null) {
            existingUser.setStatus(userUpdateRequest.getStatus());
        }
        if (userUpdateRequest.getIsActive() != null) {
            existingUser.setIsActive(userUpdateRequest.getIsActive());
        }
        if (userUpdateRequest.getHasLoginPermission() != null) {
            existingUser.setHasLoginPermission(userUpdateRequest.getHasLoginPermission());
        }
        if (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty()) {
            existingUser.setPassword(userUpdateRequest.getPassword());
        }
    }

    @Override
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir öğrenci bulunamadı.");
        }
        
        this.studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getByStudentNumber(String studentNumber) {
        return this.studentRepository.findByStudentNumber(studentNumber)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Öğrenci bilgisi bulunamadı! Öğrenci Numarası: " + studentNumber));
    }

    @Override
    public void updateByStudentNumber(String studentNumber, StudentUpdateRequest studentUpdateRequest) {
        Student studentToUpdate = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new NotFoundException("Güncelleme yapılamadı. Öğrenci Numarası: "
                        + studentNumber + " ile eşleşen bir öğrenci bulunamadı."));

        studentUpdateRequest.setId(studentToUpdate.getId());
        
        if (studentUpdateRequest.getUser() != null && studentUpdateRequest.getUser().getEmail() != null) {
            if (!studentToUpdate.getUser().getEmail().equals(studentUpdateRequest.getUser().getEmail()) && 
                userRepository.existsByEmail(studentUpdateRequest.getUser().getEmail())) {
                throw new BusinessException("Bu e-posta adresi zaten kullanılıyor: " + studentUpdateRequest.getUser().getEmail());
            }
        }
        
        if (studentUpdateRequest.getUser() != null && studentToUpdate.getUser() != null) {
            studentUpdateRequest.getUser().setId(studentToUpdate.getUser().getId());
        }

        modelMapperService.forRequest().map(studentUpdateRequest, studentToUpdate);
        
        studentToUpdate.setStudentNumber(studentNumber); 

        if (studentUpdateRequest.getDepartmentId() != null) {
            Department department = departmentRepository.findById(studentUpdateRequest.getDepartmentId())
                    .orElseThrow(() -> new NotFoundException("Departman bulunamadı: ID " + studentUpdateRequest.getDepartmentId()));
            studentToUpdate.setDepartment(department);
        }

        if (studentUpdateRequest.getAdvisorId() != null) {
            Academic advisor = academicRepository.findById(studentUpdateRequest.getAdvisorId())
                    .orElseThrow(() -> new NotFoundException("Danışman akademisyen bulunamadı: ID " + studentUpdateRequest.getAdvisorId()));
            studentToUpdate.setAdvisor(advisor);
        } else { 
             if (studentToUpdate.getAdvisor() != null) {
                studentToUpdate.setAdvisor(null);
            }
        }
        
        if (studentUpdateRequest.getUser() != null) {
            User existingUser = studentToUpdate.getUser();
            if (existingUser != null) {
                updateUserFields(existingUser, studentUpdateRequest.getUser());
            } else {
                throw new BusinessException("Öğrenciye ait kullanıcı bulunamadı, kullanıcı bilgileri güncellenemiyor.");
            }
        }
        
        studentRepository.save(studentToUpdate);
    }
    
    private StudentResponse mapToResponse(Student student) {
        return modelMapperService.forResponse().map(student, StudentResponse.class);
    }
}
