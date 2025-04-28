package com.example.obs.business.service.impl;

import com.example.obs.business.service.UserService;
import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.requests.UserUpdateRequest;
import com.example.obs.business.responses.UserResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.UserRepository;
import com.example.obs.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserManager implements UserService {
    private UserRepository userRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<UserResponse> getAll() {

        List<User> users = userRepository.findAll();

        List<UserResponse> responses = users.stream()
                .map(user -> this.modelMapperService.forResponse()
                        .map(user ,UserResponse.class)).collect(Collectors.toList());

        return responses;
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElseThrow();

        UserResponse response = this.modelMapperService.forResponse()
                .map(user, UserResponse.class);

        return response;
    }

    @Override
    public void add(UserCreateRequest userCreateRequest) {
        User user = this.modelMapperService.forRequest()
                .map(userCreateRequest, User.class);

        userRepository.save(user);
    }

    @Override
    public void update(UserUpdateRequest userUpdateRequest) {
        User user = this.modelMapperService.forRequest()
                .map(userUpdateRequest, User.class);

        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }
}