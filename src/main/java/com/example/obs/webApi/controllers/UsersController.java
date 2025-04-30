package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.requests.UserUpdateRequest;
import com.example.obs.business.responses.UserResponse;
import com.example.obs.business.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UsersController {

    private UserService userService;

    @GetMapping("/all")
    public List<UserResponse> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable int id) {
        return userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody UserCreateRequest userCreateRequest) {
        this.userService.add(userCreateRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody UserUpdateRequest userUpdateRequest) {
        userUpdateRequest.setId(id);
        this.userService.update(userUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        this.userService.delete(id);
    }
}