package com.example.obs.business.concretes;

import com.example.obs.business.abstracts.UserService;
import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.requests.UserUpdateRequest;
import com.example.obs.business.responses.UserResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserManager implements UserService {
    private UserRepository userRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<UserResponse> getAll() {
        return List.of();
    }

    @Override
    public UserResponse getById(Long id) {
        return null;
    }

    @Override
    public void add(UserCreateRequest userCreateRequest) {

    }

    @Override
    public void update(UserUpdateRequest userUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
