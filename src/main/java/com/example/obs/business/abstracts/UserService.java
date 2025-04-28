package com.example.obs.business.abstracts;

import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.requests.UserUpdateRequest;
import com.example.obs.business.responses.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAll();
    UserResponse getById(Long id);
    void add(UserCreateRequest userCreateRequest);
    void update(UserUpdateRequest userUpdateRequest);
    void delete(Long id);

}
