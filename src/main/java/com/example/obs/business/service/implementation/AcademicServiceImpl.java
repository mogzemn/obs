package com.example.obs.business.service.implementation;

import com.example.obs.business.service.AcademicService;
import com.example.obs.business.service.UserService;
import com.example.obs.business.requests.AcademicCreateRequest;
import com.example.obs.business.requests.AcademicUpdateRequest;
import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.responses.AcademicResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.core.utilities.numbergeneration.AcademicNumberGenerator;
import com.example.obs.dataAccess.AcademicRepository;
import com.example.obs.dataAccess.DepartmentRepository;
import com.example.obs.dataAccess.UserRepository;
import com.example.obs.model.entity.Academic;
import com.example.obs.model.entity.Department;
import com.example.obs.model.entity.User;
import com.example.obs.model.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AcademicServiceImpl implements AcademicService {
    private final AcademicRepository academicRepository;
    private final ModelMapperService modelMapperService;
    private final AcademicNumberGenerator academicNumberGenerator;
    private final UserService userService;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AcademicResponse> getAll() {
        return academicRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AcademicResponse getById(Long id) {
        return this.academicRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Akademisyen bilgisi bulunamadı! ID: " + id));
    }

    @Override
    @Transactional
    public void add(AcademicCreateRequest academicCreateRequest) {
        try {
            UserCreateRequest userCreateRequest = academicCreateRequest.getUser();
            if (userCreateRequest == null) {
                throw new BusinessException("Kullanıcı bilgileri (UserCreateRequest) boş olamaz.");
            }
            
            userCreateRequest.setRole(Role.ACADEMIC);

            Department department = departmentRepository.findById(academicCreateRequest.getDepartmentId())
                    .orElseThrow(() -> new NotFoundException("Bölüm bulunamadı! ID: " + academicCreateRequest.getDepartmentId()));
            
            User user = this.userService.add(userCreateRequest);

            Academic academic = new Academic();
            academic.setDepartment(department);
            academic.setUser(user);
            
            academic.setAcademicNumber(this.academicNumberGenerator.generateRegistrationNumber(academicCreateRequest.getDepartmentId()));
            
            academicRepository.save(academic);

        } catch (Exception ex) {
            throw new BusinessException("Akademisyen ekleme işlemi sırasında bir hata oluştu.", ex);
        }
    }

    @Override
    @Transactional
    public void update(AcademicUpdateRequest academicUpdateRequest) {
        try {
            Academic existingAcademic = academicRepository.findById(academicUpdateRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Güncelleme yapılamadı. ID: " 
                            + academicUpdateRequest.getId() + " ile eşleşen bir akademisyen bulunamadı."));
            
            User existingUser = existingAcademic.getUser();
            
            if (academicUpdateRequest.getDepartmentId() != null) {
                existingAcademic.setDepartment(departmentRepository.findById(academicUpdateRequest.getDepartmentId())
                        .orElseThrow(() -> new NotFoundException("Bölüm bulunamadı! ID: " + academicUpdateRequest.getDepartmentId())));
            }
            
            if (academicUpdateRequest.getUser() != null) {
                if (existingUser == null) {
                    throw new BusinessException("Akademisyen kaydının kullanıcı bilgisi bulunamadı.");
                }
                
                userService.updateUserFromAcademic(existingUser.getId(), academicUpdateRequest.getUser());
            }
            
            academicRepository.save(existingAcademic);
            
        } catch (Exception ex) {
            throw new BusinessException("Akademisyen güncelleme işlemi sırasında bir hata oluştu.", ex);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Academic academic = academicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir akademisyen bulunamadı."));
        User userToDelete = academic.getUser();
        this.academicRepository.delete(academic);
        if (userToDelete != null) {
            userService.delete(userToDelete.getId());
        }
    }
    
    private AcademicResponse mapToResponse(Academic academic) {
        return modelMapperService.forResponse().map(academic, AcademicResponse.class);
    }
}
