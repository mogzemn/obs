package com.example.obs.business.service.implementation;

import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.service.AdministrativeStaffService;
import com.example.obs.business.requests.AdministrativeStaffCreateRequest;
import com.example.obs.business.requests.AdministrativeStaffUpdateRequest;
import com.example.obs.business.responses.AdministrativeStaffResponse;
import com.example.obs.business.service.UserService;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.core.utilities.numbergeneration.AdministrativeStaffNumberGenerator;
import com.example.obs.dataAccess.AdministrativeStaffRepository;
import com.example.obs.dataAccess.UserRepository;
import com.example.obs.model.entity.AdministrativeStaff;
import com.example.obs.model.entity.User;
import com.example.obs.model.enums.AdministrativeUnit;
import com.example.obs.model.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AdministrativeStaffServiceImpl implements AdministrativeStaffService {
    private final AdministrativeStaffRepository administrativeStaffRepository;
    private final ModelMapperService modelMapperService;
    private final AdministrativeStaffNumberGenerator administrativeStaffNumberGenerator;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdministrativeStaffResponse> getAll() {
        List<Role> adminRoles = Arrays.asList(
                Role.ADMINISTRATIVE_MANAGER,
                Role.STUDENT_AFFAIRS_STAFF,
                Role.ACADEMIC_AFFAIRS_STAFF,
                Role.ADMINISTRATIVE_STAFF
        );

        List<User> staffUsers = userRepository.findAll().stream()
                .filter(user -> adminRoles.contains(user.getRole()))
                .collect(Collectors.toList());

        List<AdministrativeStaff> staffs = staffUsers.stream()
                .flatMap(user -> administrativeStaffRepository.findByUser_Id(user.getId()).stream())
                .collect(Collectors.toList());

        return staffs.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdministrativeStaffResponse getById(Long id) {
        return this.administrativeStaffRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("İdari personel bilgisi bulunamadı! ID: " + id));
    }

    @Override
    @Transactional
    public void add(AdministrativeStaffCreateRequest administrativeStaffCreateRequest) {
        int maxAttempts = 5;
        int attempt = 0;
        boolean success = false;
        
        while (!success && attempt < maxAttempts) {
            try {
                UserCreateRequest userCreateRequest = administrativeStaffCreateRequest.getUser();
                if (userCreateRequest == null) {
                    throw new BusinessException("Kullanıcı bilgileri (UserCreateRequest) boş olamaz.");
                }

                if (userCreateRequest.getRole() == null || 
                    !userCreateRequest.getRole().isAdministrativeRole()) {
                    Role roleToSet = determineRoleFromUnit(administrativeStaffCreateRequest.getUnitName());
                    userCreateRequest.setRole(roleToSet);
                }

                User user = this.userService.add(userCreateRequest);

                Optional<AdministrativeStaff> existingAdminStaffOpt = administrativeStaffRepository.findByUser_Id(user.getId());

                AdministrativeStaff adminStaffToSave;
                if (existingAdminStaffOpt.isPresent()) {
                    adminStaffToSave = existingAdminStaffOpt.get();
                    adminStaffToSave.setUnitName(administrativeStaffCreateRequest.getUnitName());
                } else {
                    adminStaffToSave = new AdministrativeStaff();
                    adminStaffToSave.setUser(user);
                    adminStaffToSave.setUnitName(administrativeStaffCreateRequest.getUnitName());

                    String generatedStaffNumber = this.administrativeStaffNumberGenerator.generateStaffNumber(
                            administrativeStaffCreateRequest.getUnitName().name());
                    adminStaffToSave.setStaffNumber(generatedStaffNumber);
                    adminStaffToSave.setAdministrativeNumber(generatedStaffNumber);
                }

                administrativeStaffRepository.save(adminStaffToSave);
                success = true;
                
            } catch (org.springframework.dao.OptimisticLockingFailureException ex) {
                attempt++;
                if (attempt >= maxAttempts) {
                    throw new com.example.obs.core.exceptions.OptimisticLockingException("İyimser kilitleme hatası: İdari personel oluşturulamadı. Lütfen daha sonra tekrar deneyiniz.", ex);
                }
                try {
                    Thread.sleep(300 * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new BusinessException("İdari personel ekleme işlemi kesintiye uğradı.", ie);
                }
            } catch (com.example.obs.core.exceptions.OptimisticLockingException ex) {
                 attempt++;
                 if (attempt >= maxAttempts) {
                    throw new com.example.obs.core.exceptions.OptimisticLockingException("İyimser kilitleme hatası: İdari personel oluşturulamadı (kullanıcı servisinden gelen hata). Lütfen daha sonra tekrar deneyiniz.", ex);
                 }
                 try {
                    Thread.sleep(300 * attempt);
                 } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new BusinessException("İdari personel ekleme işlemi kesintiye uğradı.", ie);
                 }
            }
        }
        
        if (!success) {
            throw new com.example.obs.core.exceptions.OptimisticLockingException("İdari personel oluşturulamadı. Maksimum deneme sayısına ulaşıldı. Lütfen daha sonra tekrar deneyiniz.");
        }
    }

    @Override
    @Transactional
    public void update(AdministrativeStaffUpdateRequest administrativeStaffUpdateRequest) {
        int maxAttempts = 5;
        int attempt = 0;
        boolean success = false;
        
        while (!success && attempt < maxAttempts) {
            try {
                AdministrativeStaff existingStaff = administrativeStaffRepository.findById(administrativeStaffUpdateRequest.getId())
                        .orElseThrow(() -> new NotFoundException("Güncelleme yapılamadı. ID: "
                                + administrativeStaffUpdateRequest.getId() + " ile eşleşen bir idari personel bulunamadı."));
                
                if (administrativeStaffUpdateRequest.getUser() != null) {
                    administrativeStaffUpdateRequest.getUser().setId(existingStaff.getUser().getId());
                    try {
                        userService.update(administrativeStaffUpdateRequest.getUser());
                    } catch (com.example.obs.core.exceptions.OptimisticLockingException ex) {
                        throw new org.springframework.dao.OptimisticLockingFailureException("Kullanıcı güncellenirken iyimser kilitleme hatası", ex);
                    }
                }

                if (administrativeStaffUpdateRequest.getUnitName() != null &&
                        administrativeStaffUpdateRequest.getUnitName() != existingStaff.getUnitName()) {

                    boolean userSpecifiedRole = administrativeStaffUpdateRequest.getUser() != null && 
                                              administrativeStaffUpdateRequest.getUser().getRole() != null &&
                                              administrativeStaffUpdateRequest.getUser().getRole().isAdministrativeRole();
                    
                    if (!userSpecifiedRole) {
                        Role newRole = determineRoleFromUnit(administrativeStaffUpdateRequest.getUnitName());
    
                        User user = existingStaff.getUser();
                        if (user != null && user.getRole() != newRole) {
                            user.setRole(newRole);
                            userRepository.save(user);
                        }
                    }

                    existingStaff.setUnitName(administrativeStaffUpdateRequest.getUnitName());
                }

                administrativeStaffRepository.save(existingStaff);
                success = true;
                
            } catch (org.springframework.dao.OptimisticLockingFailureException ex) {
                attempt++;
                if (attempt >= maxAttempts) {
                    throw new com.example.obs.core.exceptions.OptimisticLockingException("İyimser kilitleme hatası: İdari personel güncellenemedi. Lütfen daha sonra tekrar deneyiniz.", ex);
                }
                try {
                    Thread.sleep(300 * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new BusinessException("İdari personel güncelleme işlemi kesintiye uğradı.", ie);
                }
            }
        }
        
        if (!success) {
            throw new com.example.obs.core.exceptions.OptimisticLockingException("İdari personel güncellenemedi. Maksimum deneme sayısına ulaşıldı. Lütfen daha sonra tekrar deneyiniz.");
        }
    }

    @Override
    public void delete(Long id) {
        AdministrativeStaff adminStaff = administrativeStaffRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir idari personel bulunamadı."));

        User userToDelete = adminStaff.getUser();

        this.administrativeStaffRepository.delete(adminStaff);

        if (userToDelete != null) {
            userService.delete(userToDelete.getId());
        }
    }

    private Role determineRoleFromUnit(AdministrativeUnit unit) {
        switch (unit) {
            case ADMINISTRATIVE_MANAGEMENT:
                return Role.ADMINISTRATIVE_MANAGER;
            case STUDENT_AFFAIRS:
                return Role.STUDENT_AFFAIRS_STAFF;
            case ACADEMIC_AFFAIRS:
                return Role.ACADEMIC_AFFAIRS_STAFF;
            default:
                return Role.ADMINISTRATIVE_STAFF;
        }
    }
    
    private AdministrativeStaffResponse mapToResponse(AdministrativeStaff staff) {
        return modelMapperService.forResponse().map(staff, AdministrativeStaffResponse.class);
    }
}
