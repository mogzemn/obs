package com.example.obs.business.service.implementation;

import com.example.obs.business.service.UserService;
import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.requests.UserUpdateRequest;
import com.example.obs.business.responses.UserResponse;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.UserRepository;
import com.example.obs.model.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapperService modelMapperService;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                          ModelMapperService modelMapperService,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapperService = modelMapperService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return this.userRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Kullanıcı bilgisi bulunamadı! ID: " + id));
    }

    @Override
    public User add(UserCreateRequest userCreateRequest) {
        try {
            Optional<User> existingUserByEmailOpt = userRepository.findByEmail(userCreateRequest.getEmail());
            Optional<User> existingUserByIdentityOpt = userRepository.findByIdentityNumber(userCreateRequest.getIdentityNumber());

            User userToSave;

            if (existingUserByEmailOpt.isPresent()) {
                User existingUser = existingUserByEmailOpt.get();
                if (!existingUser.getIdentityNumber().equals(userCreateRequest.getIdentityNumber())) {
                    throw new BusinessException("Bu e-posta adresi başka bir kullanıcı tarafından kullanılmaktadır: " + userCreateRequest.getEmail());
                }
                userToSave = existingUser;
                this.modelMapperService.forRequest().map(userCreateRequest, userToSave);
                if (userCreateRequest.getPassword() != null && !userCreateRequest.getPassword().isEmpty()) {
                    userToSave.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
                }
            } else if (existingUserByIdentityOpt.isPresent()) {
                User existingUser = existingUserByIdentityOpt.get();
                if (!existingUser.getEmail().equals(userCreateRequest.getEmail())) {
                    throw new BusinessException("Bu kimlik numarası başka bir kullanıcı tarafından kullanılmaktadır: " + userCreateRequest.getIdentityNumber());
                }
                userToSave = existingUser;
                this.modelMapperService.forRequest().map(userCreateRequest, userToSave);
                if (userCreateRequest.getPassword() != null && !userCreateRequest.getPassword().isEmpty()) {
                    userToSave.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
                }
            } else {
                userToSave = this.modelMapperService.forRequest().map(userCreateRequest, User.class);
                if (userToSave.getBirthDate() == null && userCreateRequest.getBirthDate() != null) {
                    userToSave.setBirthDate(userCreateRequest.getBirthDate());
                }
                userToSave.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
            }
            
            return userRepository.save(userToSave);

        } catch (Exception ex) {
            throw new BusinessException("Kullanıcı ekleme işlemi sırasında bir hata oluştu: " + ex.getMessage());
        }
    }

    @Override
    public void update(UserUpdateRequest userUpdateRequest) {
        User existingUser = userRepository.findById(userUpdateRequest.getId())
                .orElseThrow(() -> new NotFoundException("Güncelleme yapılamadı. ID: "
                        + userUpdateRequest.getId() + " ile eşleşen bir kullanıcı bulunamadı."));

        if (userUpdateRequest.getEmail() != null && 
            !existingUser.getEmail().equals(userUpdateRequest.getEmail()) &&
            userRepository.findByEmail(userUpdateRequest.getEmail()).isPresent()) {
            throw new BusinessException("Bu e-posta adresi zaten kullanımda: " + userUpdateRequest.getEmail());
        }

        updateNonNullFields(existingUser, userUpdateRequest);

        if (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }

        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void updateUserFromAcademic(Long userId, UserUpdateRequest userUpdateRequest) {
        try {
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Güncelleme yapılamadı. User ID: "
                            + userId + " ile eşleşen bir kullanıcı bulunamadı."));
            
            if (userUpdateRequest.getEmail() != null && 
                !existingUser.getEmail().equals(userUpdateRequest.getEmail())) {
                
                Optional<User> existingUserWithSameEmail = userRepository.findByEmail(userUpdateRequest.getEmail());
                if (existingUserWithSameEmail.isPresent() && !existingUserWithSameEmail.get().getId().equals(existingUser.getId())) {
                    throw new BusinessException("Bu e-posta adresi zaten kullanımda: " + userUpdateRequest.getEmail());
                }
            }
            
            updateNonNullFields(existingUser, userUpdateRequest);
            
            if (userUpdateRequest.getPassword() != null && !userUpdateRequest.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
            }
            
            userRepository.save(existingUser);
            
        } catch (Exception ex) {
            throw new BusinessException("Kullanıcı güncelleme işlemi sırasında bir hata oluştu: " + ex.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir kullanıcı bulunamadı.");
        }
        this.userRepository.deleteById(id);
    }
    
    private UserResponse mapToResponse(User user) {
        return modelMapperService.forResponse().map(user, UserResponse.class);
    }
    
    private void updateNonNullFields(User existingUser, UserUpdateRequest userUpdateRequest) {
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
        if (userUpdateRequest.getIsAdmin() != null) {
            existingUser.setIsAdmin(userUpdateRequest.getIsAdmin());
        }
        if (userUpdateRequest.getRole() != null) {
            existingUser.setRole(userUpdateRequest.getRole());
        }
    }
}
