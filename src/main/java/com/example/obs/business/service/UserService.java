package com.example.obs.business.service;

import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.requests.UserUpdateRequest;
import com.example.obs.business.responses.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAll();
    UserResponse getById(int id);
    void add(UserCreateRequest userCreateRequest);
    void update(UserUpdateRequest userUpdateRequest);
    void delete(int id);

}
