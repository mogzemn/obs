package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.UserCreateRequest;
import com.example.obs.business.requests.UserUpdateRequest;
import com.example.obs.business.responses.UserResponse;
import com.example.obs.business.service.UserService;
import com.example.obs.core.utilities.results.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UsersController {

    private UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        List<UserResponse> users = userService.getAll();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all') or @securityUtils.isOwnUserId(#id)")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) {
        UserResponse user = userService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public ResponseEntity<ApiResponse<Void>> add(@RequestBody UserCreateRequest userCreateRequest) {
        this.userService.add(userCreateRequest);
        return new ResponseEntity<>(ApiResponse.success(null, "Kullanıcı başarıyla eklendi"), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all') or @securityUtils.isOwnUserId(#id)")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        userUpdateRequest.setId(id);
        this.userService.update(userUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(null, "Kullanıcı başarıyla güncellendi"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINISTRATIVE_MANAGER') or hasAuthority('system:manage_all')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        this.userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Kullanıcı başarıyla silindi"));
    }
}
