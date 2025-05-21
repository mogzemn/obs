package com.example.obs.business.service;

import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.requests.UserUpdateRequest;
import com.example.obs.business.responses.UserResponse;
import com.example.obs.model.entity.User;

import java.util.List;

public interface UserService {

    List<UserResponse> getAll();
    UserResponse getById(Long id);
    User add(UserCreateRequest userCreateRequest);
    void update(UserUpdateRequest userUpdateRequest);
    void delete(Long id);
    
    void updateUserFromAcademic(Long userId, UserUpdateRequest userUpdateRequest);
}
